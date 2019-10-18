package com.rxh.controller;

import com.rxh.pojo.sys.SysPrivileges;
import com.rxh.pojo.sys.SysRole;
import com.rxh.pojo.sys.SysUser;
import com.rxh.service.UserService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/18
 * Time: 11:43
 * Project: Management
 */
@Configuration
@RequestMapping(value = "/admin")
public class AdminController {
    @Resource
    private UserService userService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Resource
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    /**
     * 用户注册
     *
     * @param user 注册信息
     * @return 注册信息
     */
    @SystemLogInfo(description = "用户注册")
    @RequestMapping(value = "/user/register")
    @ResponseBody
    public SysUser userRegister(@RequestBody SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.createUser(user, UserInfoUtils.getName());
    }

    /**
     * 用户信息变更
     *
     * @param user 变更对象
     * @return 变更结果
     */
    @SystemLogInfo(description = "更新用户信息")
    @RequestMapping(value = "/user/update")
    @ResponseBody
    public Boolean updateUser(@RequestBody SysUser user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // 若用户信息被修改则需要重新登录
        Boolean status = userService.updateUserByPrimaryKey(user, UserInfoUtils.getName());
        if (status) {
            forcedLogout(user.getId());
        }
        return status;
    }

    @SystemLogInfo(description = "删除用户")
    @RequestMapping(value = "/user/delete")
    @ResponseBody
    public Boolean deleteUser(@RequestBody List<Long> idList) {
        // 注销删除用户
        idList.forEach(this::forcedLogout);
        Boolean status = userService.deleteUserByIdList(idList);
        return status;
    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @SystemLogInfo(description = "获取用户列表")
    @RequestMapping(value = "/user/getAll")
    @ResponseBody
    public List<SysUser> getAllUser() {
        // userService.getMenu(UserInfoUtils.getName());
        return userService.getAllUser();
    }

    /**
     * 查看帐户是否存在
     *
     * @param userName 帐户名
     * @return 是否存在
     */
    @RequestMapping(value = "/user/isExist")
    @ResponseBody
    public Boolean isUserExist(@RequestBody String userName) {
        return userService.selectUserByUsername(userName) != null;
    }

    @SystemLogInfo(description = "新增角色")
    @RequestMapping(value = "/role/add")
    @ResponseBody
    public Boolean addRole(@RequestBody SysRole role) {
        return userService.createRole(role, UserInfoUtils.getName());
    }

    /**
     * 更新角色信息
     *
     * @param role 角色对象
     * @return 控制结果
     */
    @SystemLogInfo(description = "更新角色信息")
    @RequestMapping(value = "/role/update")
    @ResponseBody
    public Boolean updateRole(@RequestBody SysRole role) {
        Boolean status = userService.upDateRole(role, UserInfoUtils.getName());
        // 注销所有拥有此角色用户
        if (status) {
            List<SysUser> userList = userService.selectUserByRoleId(role.getId());
            userList.forEach(user -> forcedLogout(user.getId()));
        }
        return status;
    }

    @SystemLogInfo(description = "删除角色")
    @RequestMapping(value = "/role/delete")
    @ResponseBody
    public Map<String, Object> deleteRole(@RequestBody Map<String, Object> paramMap) {
        return userService.deleteRoleByIds(paramMap);
    }

    @RequestMapping(value = "/role/isExist")
    @ResponseBody
    public Boolean isRoleExist(@RequestBody String role) {
        return userService.findRoleByRoleName(role) != null;
    }

    /**
     * 查询角色列表
     *
     * @return 角色列表
     */
    @RequestMapping(value = "/role/getAll")
    @ResponseBody
    public List<SysRole> getAllRole() {
        return userService.findAllRole();
    }

    /**
     * 获取所有权限信息
     *
     * @return 所有权限信息
     */
    @RequestMapping(value = "/role/getAllPrivileges")
    @ResponseBody
    public List<SysPrivileges> getAllPrivileges() {
        return userService.selectAllPrivileges();
    }

    /**
     * 根据用户Id强制注销用户
     *
     * @param userId 用户Id
     */
    private void forcedLogout(Long userId) {
        SysUser user = userService.selectUserById(userId);
        Map<String, ? extends Session> userSessions = sessionRepository
                .findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, user.getUserName());
        userSessions.forEach((s, o) -> redisOperationsSessionRepository.delete(s));
    }
}
