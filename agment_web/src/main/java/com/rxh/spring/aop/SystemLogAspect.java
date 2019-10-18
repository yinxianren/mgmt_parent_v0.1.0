package com.rxh.spring.aop;

import com.rxh.pojo.sys.SysLog;
import com.rxh.service.AgentSystemService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.IpUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.SystemConstant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component
public class SystemLogAspect {

    private AgentSystemService agentSystemService;
    // 插入方法名开头字符串数组
    private String[] addMethodList;
    // 删除方法名开头字符串数组
    private String[] delMethodList;
    // 修改方法名开头字符串数组
    private String[] updateMethodList;
    // 读法名开头字符串数组
    private String[] readMethodList;
    // 方法开始时间
    private long startTime = 0L;

    @Pointcut("@annotation(com.rxh.spring.annotation.SystemLogInfo)")
    public void controllerAspect() {
    }

    @Before("controllerAspect()")
    public void doBefore() {
        startTime = System.currentTimeMillis();
    }

    @After("controllerAspect() && @annotation(systemLogInfo)")
    public void doAfter(JoinPoint joinPoint, SystemLogInfo systemLogInfo) {
        SysLog log = new SysLog();
        setSysLogInfo(log, joinPoint, systemLogInfo);
        String[][] AllMethodList = {addMethodList, delMethodList, updateMethodList, readMethodList};
        for (int i = 0; i < AllMethodList.length; i++) {
            if (PatternMatchUtils.simpleMatch(AllMethodList[i], joinPoint.getSignature().getName())) {
                log.setType((short) (i + 1));
                break;
            }
        }
        if (log.getType() == null) {
            log.setType(SystemConstant.LOG_UNKNOWN_METHOD);
        }
        Object[] objects = joinPoint.getArgs();
        log.setMessage(objects.length > 0 ? JsonUtils.objectToJsonNonNull(objects) : null);
        agentSystemService.saveSystemLog(log);
    }

    @AfterThrowing(pointcut = "controllerAspect() && @annotation(systemLogInfo)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, SystemLogInfo systemLogInfo, Throwable e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(e.getMessage());
        stringBuilder.append("\r\n");
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(stackTraceElements[i]);
            stringBuilder.append("\r\n");
        }
        SysLog log = new SysLog();
        setSysLogInfo(log, joinPoint, systemLogInfo);
        log.setType(SystemConstant.LOG_ERROR);
        log.setMessage(stringBuilder.toString());
        agentSystemService.saveSystemLog(log);
    }

    private void setSysLogInfo(SysLog log, JoinPoint joinPoint, SystemLogInfo systemLogInfo) {
        String ip = null;
        String uri = null;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            request.getRequestURI();
            ip = IpUtils.getReallyIpForRequest(request);
            uri = request.getRequestURI();
        }
        log.setOperator(UserInfoUtils.getName());
        log.setStartTime(new Date(startTime));
        log.setSpendTime(System.currentTimeMillis() - startTime);
        log.setRequestIp(ip);
        log.setRequestUri(uri);
        log.setMethodName(joinPoint.getSignature().getName());
        log.setMethodDescription(systemLogInfo.description());
    }

    // 非必需不使用
    /*@Around(value = "controllerAspect() && @annotation(systemLogInfo)", argNames = "pjp,systemLogInfo")
    public Object doArount(ProceedingJoinPoint pjp, SystemLogInfo systemLogInfo) throws Throwable {
        return pjp.proceed();
    }*/

    public AgentSystemService getAgentSystemService() {
        return agentSystemService;
    }

    public void setAgentSystemService(AgentSystemService agentSystemService) {
        this.agentSystemService = agentSystemService;
    }

    public String[] getAddMethodList() {
        return addMethodList;
    }

    public void setAddMethodList(String[] addMethodList) {
        this.addMethodList = addMethodList;
    }

    public String[] getDelMethodList() {
        return delMethodList;
    }

    public void setDelMethodList(String[] delMethodList) {
        this.delMethodList = delMethodList;
    }

    public String[] getUpdateMethodList() {
        return updateMethodList;
    }

    public void setUpdateMethodList(String[] updateMethodList) {
        this.updateMethodList = updateMethodList;
    }

    public String[] getReadMethodList() {
        return readMethodList;
    }

    public void setReadMethodList(String[] readMethodList) {
        this.readMethodList = readMethodList;
    }
}