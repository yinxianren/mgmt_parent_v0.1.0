package com.rxh.spring.security.details.service;

import com.rxh.pojo.sys.SysPrivileges;
import com.rxh.pojo.sys.SysRole;
import com.rxh.pojo.sys.SysUser;
import com.rxh.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetailsService implements UserDetailsService {

    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser user = userService.getUserAndRoleAndPrivilege(s);
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), user.getAvailable(),
                true,
                true,
                true,
                getAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(SysRole role){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getRole()));
        if (role.getPrivileges() != null) {
            for (SysPrivileges p :
                    role.getPrivileges()) {
                authorities.add(new SimpleGrantedAuthority(p.getName()));
            }
        }
        return authorities;
    }
}