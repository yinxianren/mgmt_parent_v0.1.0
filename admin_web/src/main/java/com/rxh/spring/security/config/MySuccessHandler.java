package com.rxh.spring.security.config;

import com.internal.playment.api.db.system.ApiSysLogService;
import com.internal.playment.api.db.system.ApiSysUserServie;
import com.internal.playment.common.inner.IpUtils;
import com.internal.playment.common.table.system.SysLogTable;
import com.internal.playment.common.table.system.SysUserTable;
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


    private ApiSysUserServie userService;

    private ApiSysLogService systemService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        // setUseReferer(true);
        String remoteAddr = IpUtils.getReallyIpForRequest(request);
        SysUserTable user = new SysUserTable();
        user.setUserName(authentication.getName());
        user.setLastLogonIp(remoteAddr);
        userService.savaOrUpdate(user);
        SysLogTable log = new SysLogTable();
        log.setType(0);
        log.setOperator(authentication.getName());
        log.setStartTime(new Date());
        log.setRequestIp(remoteAddr);
        log.setRequestUri(request.getRequestURI());
        log.setMessage("登录成功！");
        systemService.saveOrUpdate(log);
    }

    public ApiSysUserServie getUserService() {
        return userService;
    }

    public void setUserService(ApiSysUserServie userService) {
        this.userService = userService;
    }

    public ApiSysLogService getSystemService() {
        return systemService;
    }

    public void setSystemService(ApiSysLogService systemService) {
        this.systemService = systemService;
    }
}
