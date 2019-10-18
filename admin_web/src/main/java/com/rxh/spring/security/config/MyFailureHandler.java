package com.rxh.spring.security.config;

import com.rxh.pojo.sys.SysLog;
import com.rxh.service.MerchantSystemService;
import com.rxh.service.SystemService;
import com.rxh.utils.IpUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


public class MyFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private SystemService systemService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, exception);
        SysLog log = new SysLog();
        log.setType((short) 0);
        log.setOperator(request.getParameter("username"));
        log.setStartTime(new Date());
        log.setRequestIp(IpUtils.getReallyIpForRequest(request));
        log.setRequestUri(request.getRequestURI());
        log.setMessage("登录失败！");
        systemService.saveSystemLog(log);
    }


    public SystemService getSystemService() {
        return systemService;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }
}
