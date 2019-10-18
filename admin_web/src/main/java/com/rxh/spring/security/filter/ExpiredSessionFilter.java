package com.rxh.spring.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ExpiredSessionFilter extends GenericFilterBean {

    private static final String ANONYMOUS_USER = "anonymousUser";
    private static final String ANGULAR_HEADER_VALUE = "XMLHttpRequest";
    private static final String ANGULAR_HEADER = "X-Requested-With";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String header = httpServletRequest.getHeader(ANGULAR_HEADER);
        if (authentication == null && ANGULAR_HEADER_VALUE.equals(header)) {
            send401(httpServletResponse);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void send401(HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Ajax REquest Denied (Session Expired)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
