package com.rxh.spring.security.filter;

import com.internal.playment.common.table.system.SysPrivilegesTable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;


public class MyFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {


    /*private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    public MyFilterSecurityMetadataSource(UserService userService) {
        List<SysPrivilegesTable> privilegesList = userService.selectAllPrivileges();
        Map<RequestMatcher, Collection<ConfigAttribute>> map = new HashMap<>();

        requestMap = map;
    }*/

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) o;
        String url = fi.getRequestUrl();
        String httpMethod = fi.getRequest().getMethod();
        List<ConfigAttribute> attributes = new ArrayList<>();
        // Lookup your database (or other source) using this information and populate the
        // list of attributes
        SecurityConfig.createList("");
        return attributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
