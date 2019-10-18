package com.rxh.spring.security.config;

import com.rxh.pojo.sys.SysLog;
import com.rxh.pojo.sys.SysUser;
import com.rxh.service.SystemService;
import com.rxh.service.UserService;
import com.rxh.utils.IpUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/4/24
 * Time: 10:04
 * Project: Management
 * Package: com.rxh.spring.security.config
 * <p>
 * 登录成功后更新用户表对应用户最后登录IP
 */
public class MySuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private UserService userService;

    private SystemService systemService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        // setUseReferer(true);
        String remoteAddr = IpUtils.getReallyIpForRequest(request);
        SysUser user = new SysUser();
        user.setUserName(authentication.getName());
        user.setLastLogonIp(remoteAddr);
        userService.updateUserByUserName(user);
        SysLog log = new SysLog();
        log.setType((short) 0);
        log.setOperator(authentication.getName());
        log.setStartTime(new Date());
        log.setRequestIp(remoteAddr);
        log.setRequestUri(request.getRequestURI());
        log.setMessage("登录成功！");
        systemService.saveSystemLog(log);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public SystemService getSystemService() {
        return systemService;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }
}
