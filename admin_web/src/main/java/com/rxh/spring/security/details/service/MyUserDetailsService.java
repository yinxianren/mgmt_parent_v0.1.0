package com.rxh.spring.security.details.service;

import com.internal.playment.api.db.system.ApiSysPrivilegesService;
import com.internal.playment.api.db.system.ApiSysRoleService;
import com.internal.playment.api.db.system.ApiSysUserServie;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SysPrivilegesTable;
import com.internal.playment.common.table.system.SysRoleTable;
import com.internal.playment.common.table.system.SysUserTable;
import com.rxh.pojo.sys.SysPrivileges;
import com.rxh.pojo.sys.SysRole;
import com.rxh.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUserDetailsService implements UserDetailsService {

    private ApiSysUserServie apiUserService;
    private ApiSysRoleService roleService;
    private ApiSysPrivilegesService privilegesService;
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public ApiSysUserServie getApiUserService() {
        return apiUserService;
    }
    public void setApiUserService(ApiSysUserServie apiUserService) {
        this.apiUserService = apiUserService;
    }
    public ApiSysRoleService getRoleService() {
        return roleService;
    }
    public void setRoleService(ApiSysRoleService roleService) {
        this.roleService = roleService;
    }
    public ApiSysPrivilegesService getPrivilegesService() {
        return privilegesService;
    }
    public void setPrivilegesService(ApiSysPrivilegesService privilegesService) {
        this.privilegesService = privilegesService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        SysUser user = userService.getUserAndRoleAndPrivilege(s);
        SysUserTable user = getUserAndRoleAndPrivilege(s);
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), user.getAvailable(),
                true,
                true,
                true,
                getAuthorities(user.getRole()));
    }

    private SysUserTable getUserAndRoleAndPrivilege(String username) {
        SysUserTable user = new SysUserTable();
        user.setUserName(username);
        user = apiUserService.getList(user).get(0);
        if (user == null) {
            return null;
        }
        if (user.getStatus() == StatusEnum._0.getStatus()) user.setAvailable(true);
        else user.setAvailable(false);
        SysRoleTable role = new SysRoleTable();
        role.setId(user.getRoleId());
        role = roleService.getList(role).get(0);
        if (null != (role.getStatus())) {
            List<Long> roleIdList = new ArrayList<>();
            Pattern r = Pattern.compile("\\d+");
            Matcher m = r.matcher(role.getPrivilegesId());
            while (m.find()) {
                roleIdList.add(Long.valueOf(m.group()));
            }
            SysPrivilegesTable sysPrivilegesTable = new SysPrivilegesTable();
            sysPrivilegesTable.setIds(roleIdList);
            List<SysPrivilegesTable> sysPrivileges = privilegesService.getList(sysPrivilegesTable);

            role.setPrivileges(sysPrivileges);
        }
        user.setRole(role);
        return user;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(SysRoleTable role){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getRole()));
        if (role.getPrivileges() != null) {
            for (SysPrivilegesTable p :
                    role.getPrivileges()) {
                authorities.add(new SimpleGrantedAuthority(p.getName()));
            }
        }
        return authorities;
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