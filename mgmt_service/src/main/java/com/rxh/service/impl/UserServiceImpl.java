package com.rxh.service.impl;

import com.rxh.mapper.sys.SysPrivilegesMapper;
import com.rxh.mapper.sys.SysRoleMapper;
import com.rxh.mapper.sys.SysUserMapper;
import com.rxh.pojo.sys.SysPrivileges;
import com.rxh.pojo.sys.SysRole;
import com.rxh.pojo.sys.SysUser;
import com.rxh.service.UserService;
import com.rxh.utils.*;
import com.rxh.utils.UUID;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysPrivilegesMapper privilegesMapper;


    @Resource
    private GlobalConfiguration globalConfiguration;

    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<SysUser> selectUserByRoleId(Long roleId) {
        return userMapper.selectByRoleId(roleId);
    }

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 用户信息
     */
    @Override
    public SysUser createUser(SysUser user, String userName) {
        if (userMapper.selectByUserName(user.getUserName()) == null) {
            user.setId(UUID.createShortKey("sys_user"));
            user.setCreator(userName);
            user.setCreateTime(new Date());
            user.setAvailable(true);
            userMapper.insertSelective(user);
            return user;
        }
        return null;
    }

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public SysUser selectUserByUsername(String username) {
        return userMapper.selectByUserName(username);
    }

    /**
     * 获取用户对应角色以及角色对应权限
     *
     * @param username 用户名
     * @return 用户对象包含角色、权限
     */
    @Override
    public SysUser getUserAndRoleAndPrivilege(String username) {
        SysUser user = userMapper.selectByUserName(username);
        if (user == null) {
            return null;
        }
        SysRole role = roleMapper.selectByPrimaryKey(user.getRoleId());
        if (StringUtils.isNotBlank(role.getPrivilegesId())) {
            List<Long> roleIdList = new ArrayList<>();
            Pattern r = Pattern.compile("\\d+");
            Matcher m = r.matcher(role.getPrivilegesId());
            while (m.find()) {
                roleIdList.add(Long.valueOf(m.group()));
            }
            List<SysPrivileges> sysPrivileges = privilegesMapper.selectUserPrivileges(roleIdList);

            role.setPrivileges(privilegesMapper.selectUserPrivileges(roleIdList));
        }
        user.setRole(role);
        return user;
    }

    /**
     * 获取所有用户基本信息
     *
     * @return 查询结果
     */
    @Override
    public List<SysUser> getAllUser() {
        return userMapper.selectAll();
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 成功/失败
     */
    @Override
    public Boolean updateUserByPrimaryKey(SysUser user, String userName) {
        user.setModifier(userName);
        user.setModifyTime(new Date());
        return userMapper.updateByPrimaryKeySelective(user) == 1;
    }

    @Override
    public Boolean updateUserByUserName(SysUser user) {
        return userMapper.updateByUserNameSelective(user) == 1;
    }

    @Override
    public Boolean createRole(SysRole role, String userName) {
        role.setId(UUID.createShortKey("sys_role"));
        role.setRole(SystemConstant.ROLE_INTERNAL_USER);
        role.setCreator(userName);
        role.setCreateTime(new Date());
        role.setAvailable(true);
        return roleMapper.insertSelective(role) == 1;
    }

    @Override
    public Boolean deleteUserByIdList(List<Long> idList) {
        return userMapper.deleteByIdList(idList) > 0;
    }

    /**
     * 更新角色信息
     *
     * @param role 角色信息
     * @return 成功/失败
     */
    @Override
    public Boolean upDateRole(SysRole role, String userName) {
        role.setModifier(userName);
        role.setModifyTime(new Date());
        return roleMapper.updateByPrimaryKeySelective(role) == 1;
    }

    @Override
    public List<Long> deleteRoleByIdLIst(List<Long> idList) {
        List<Long> undeletable = new ArrayList<>();
        idList.forEach(aLong -> {
            if (userMapper.selectByRoleId(aLong).size() > 0) {
                undeletable.add(aLong);
            } else {
                roleMapper.deleteByPrimaryKey(aLong);
            }
        });
        return undeletable;
    }

    /**
     * 获取所有角色基本信息
     *
     * @return 查询结果
     */
    @Override
    public List<SysRole> findAllRole() {
        List<SysRole> roleList = roleMapper.selectAll();
        // roleList.add(0, SystemConstant.);
        return roleList;
    }

    /**
     * 根据角色名称查找角色
     *
     * @param roleName 角色名称
     * @return 角色
     */
    @Override
    public SysRole findRoleByRoleName(String roleName) {
        return roleMapper.selectByName(roleName);
    }

    /**
     * 查找所有权限
     *
     * @return 权限信息
     */
    @Override
    public List<SysPrivileges> selectAllPrivileges() {
        return privilegesMapper.selectAll();
    }

    @Override
    public List<SysPrivileges> getMenu(String userName) {
        SysUser user = getUserAndRoleAndPrivilege(userName);
        List<SysPrivileges> privilegesList = user.getRole().getPrivileges();
        if (privilegesList != null) {
            List<SysPrivileges> menuList = privilegesList
                    .stream()
                    .filter(sysPrivileges -> sysPrivileges.getParentId() == null)
                    .collect(Collectors.toList());
            List<SysPrivileges> submenuList = privilegesList
                    .stream()
                    .filter(sysPrivileges -> sysPrivileges.getParentId() != null)
                    .collect(Collectors.toList());
            Map<Long, List<SysPrivileges>> submenuMap = submenuList
                    .stream()
                    .collect(Collectors.groupingBy(SysPrivileges::getParentId));
            menuList.forEach(sysPrivileges -> sysPrivileges.setSubmenu(submenuMap.get(sysPrivileges.getId())));
            menuList = menuList
                    .stream()
                    .filter(sysPrivileges -> sysPrivileges.getSubmenu() != null)
                    .collect(Collectors.toList());
            return menuList;
        }
        return null;
    }

    @Override
    public Map<String, Object> deleteRoleByIds(Map<String, Object> paramMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        String ids = paramMap.get("ids") + "";
        String[] idArray = null;
        int result = 0;

        if (StringUtils.isNotBlank(ids)) {

            idArray = ids.split(",");


        }

        for (int i = 0; i < idArray.length; i++) {

            String id = idArray[i];
            result = roleMapper.deleteByPrimaryKey(Long.parseLong(id));

        }


        if (result >= 1) {

            map.put("result", "1");

        } else {

            map.put("result", "0");
        }

        return map;
    }


    @Override
    public String refreshCache(String name) {

//        String msg = HttpClientUtils.doPost(HttpClientUtils.getHttpClient(), globalConfiguration.getPaymentUrl(), null);
        String msg = HttpClientUtils.doPost(HttpClientUtils.getHttpClient(), globalConfiguration.getPaymentUrl()+"/cache/refresh", null);
        return msg;
    }


    @Override
    public String getPassWord(String userName) {
    return sysUserMapper.selectByUserName(userName).getPassword();

    }


}