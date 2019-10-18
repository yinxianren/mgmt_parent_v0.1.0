package com.rxh.spring.security.config;

import com.rxh.pojo.sys.SysLog;
import com.rxh.pojo.sys.SysUser;
import com.rxh.service.AgentSystemService;
import com.rxh.service.SystemService;
import com.rxh.service.UserService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.square.pojo.AgentMerchantInfo;
import com.rxh.utils.IpUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


public class MySuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private UserService userService;

    private AgentSystemService agentSystemService;

    private AgentMerchantInfoService agentMerchantInfoService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 将用户名储存至session中，key值为userName
        String merchantId = request.getParameter("merchantId");
        AgentMerchantInfo merchantInfo = agentMerchantInfoService.getMerchantInfo(merchantId);
        request.getSession().setAttribute("merchantInfo", merchantInfo);
        request.getSession().setAttribute("userName", request.getParameter("username"));
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
        agentSystemService.saveSystemLog(log);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public AgentSystemService getAgentSystemService() {
        return agentSystemService;
    }

    public void setAgentSystemService(AgentSystemService agentSystemService) {
        this.agentSystemService = agentSystemService;
    }

    public AgentMerchantInfoService getAgentMerchantInfoService() {
        return agentMerchantInfoService;
    }

    public void setAgentMerchantInfoService(AgentMerchantInfoService agentMerchantInfoService) {
        this.agentMerchantInfoService = agentMerchantInfoService;
    }
}
