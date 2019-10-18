package com.rxh.service;

import com.rxh.pojo.sys.SysPrivileges;
import com.rxh.pojo.sys.SysRole;
import com.rxh.pojo.sys.SysUser;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * SysUser: 陈俊雄
 * Date: 2018/3/6
 * Time: 13:50
 * Project: Management
 * Package: com.rxh.service
 */
public interface UserService {
    SysUser selectUserById(Long userId);

    List<SysUser> selectUserByRoleId(Long roleId);

    SysUser createUser(SysUser user, String userName);

    SysUser selectUserByUsername(String username);

    SysUser getUserAndRoleAndPrivilege(String username);

    SysRole findRoleByRoleName(String roleName);

    List<SysUser> getAllUser();

    Boolean updateUserByPrimaryKey(SysUser user, String userName);

    Boolean updateUserByUserName(SysUser user);

    Boolean createRole(SysRole role, String userName);

    Boolean deleteUserByIdList(List<Long> idList);

    Boolean upDateRole(SysRole role, String userName);

    List<Long> deleteRoleByIdLIst(List<Long> idList);

    List<SysRole> findAllRole();

    List<SysPrivileges> selectAllPrivileges();

    List<SysPrivileges> getMenu(String userName);

    Map<String, Object> deleteRoleByIds(Map<String, Object> paramMap);

    String refreshCache(String name);

    String getPassWord(String userName);
}
