package com.rxh.spring.security.details.service;

import com.rxh.pojo.merchant.MerchantPrivileges;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.MerchantUserService;
import com.rxh.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private HttpServletRequest request;

    private MerchantUserService merchantUserService;

    public MerchantUserService getMerchantUserService() {
        return merchantUserService;
    }

    public void setMerchantUserService(MerchantUserService merchantUserService) {
        this.merchantUserService = merchantUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String ip = IpUtils.getReallyIpForRequest(request);
        MerchantUser user = merchantUserService.getUserAndRoleAndPrivilege(s, request.getParameter("merchantId"));
        Boolean flag= merchantUserService.checkUserIp(ip,request.getParameter("merchantId"));
        if (!flag){
            user=null;
        }
        // 因商户用户名可重复，所以将唯一值用户ID作为spring security里的用户ID，登录成功后在MySuccessHandler类中将用户ID储存至Session中
        return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()), user.getPassword(), user.getAvailable(),
                true,
                true,
                true,
                getAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(MerchantRole role){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getRole()));
        if (role.getPrivileges() != null) {
            for (MerchantPrivileges p :
                    role.getPrivileges()) {
                authorities.add(new SimpleGrantedAuthority(p.getName()));
            }
        }
        return authorities;
    }
}