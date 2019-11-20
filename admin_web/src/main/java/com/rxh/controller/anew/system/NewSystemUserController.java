package com.rxh.controller.anew.system;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SysRoleTable;
import com.internal.playment.common.table.system.SysUserTable;
import com.rxh.service.system.NewSysPrivilegesService;
import com.rxh.service.system.NewSysRoleService;
import com.rxh.service.system.NewSystemUserService;
import com.rxh.service.UserService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
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
public class NewSystemUserController {
    @Resource
    private UserService userService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Resource
    private RedisOperationsSessionRepository redisOperationsSessionRepository;
    @Autowired
    private NewSystemUserService newSystemUserService;
    @Autowired
    private NewSysRoleService newSysRoleService;
    @Autowired
    private NewSysPrivilegesService newSysPrivilegesService;

    /**
     * 用户注册
     *
     * @param user 注册信息
     * @return 注册信息
     */
    @SystemLogInfo(description = "用户注册")
    @RequestMapping(value = "/user/register")
    @ResponseBody
    public ResponseVO userRegister(@RequestBody SysUserTable user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return newSystemUserService.saveOrUpdate(user);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
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
    public ResponseVO updateUser(@RequestBody SysUserTable user) {
        try {
            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            // 若用户信息被修改则需要重新登录
            ResponseVO responseVO =  newSystemUserService.saveOrUpdate(user);
            if (responseVO.getCode() == 0) {
                forcedLogout(user.getId());
            }
            return responseVO;
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }


    }

    @SystemLogInfo(description = "删除用户")
    @RequestMapping(value = "/user/delete")
    @ResponseBody
    public ResponseVO deleteUser(@RequestBody List<Long> idList) {
        try {
            // 注销删除用户
            idList.forEach(this::forcedLogout);
            return newSystemUserService.delByIds(idList);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @SystemLogInfo(description = "获取用户列表")
    @RequestMapping(value = "/user/getAll")
    @ResponseBody
    public ResponseVO getAllUser() {
        try {
            return newSystemUserService.getList(null);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
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
        try {
            SysUserTable sysUserTable = new SysUserTable();
            sysUserTable.setUserName(userName);
            List list = (List) newSystemUserService.getList(sysUserTable).getData();
            if (CollectionUtils.isEmpty(list)) return false;
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @SystemLogInfo(description = "新增角色")
    @RequestMapping(value = "/role/add")
    @ResponseBody
    public ResponseVO addRole(@RequestBody SysRoleTable role) {
        try {
            return newSysRoleService.saveOrUpdate(role);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
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
    public ResponseVO updateRole(@RequestBody SysRoleTable role) {
        try {
            ResponseVO responseVO =  newSysRoleService.saveOrUpdate(role);
            // 注销所有拥有此角色用户
            if (responseVO.getCode() == StatusEnum._0.getStatus()) {
                SysUserTable sysUserTable = new SysUserTable();
                sysUserTable.setRoleId(role.getId());
                List<SysUserTable> userList =(List<SysUserTable>) newSystemUserService.getList(sysUserTable).getData();
                userList.forEach(user -> forcedLogout(user.getId()));
            }
            return newSysRoleService.saveOrUpdate(role);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "删除角色")
    @RequestMapping(value = "/role/delete")
    @ResponseBody
    public ResponseVO deleteRole(@RequestBody Map<String, Object> paramMap) {
        try {
            String ids = paramMap.get("ids").toString();
            List<String> longList = Arrays.asList(ids.split(","));
            return newSysRoleService.delByids(longList);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @RequestMapping(value = "/role/isExist")
    @ResponseBody
    public boolean isRoleExist(@RequestBody String role) {
        try {
            SysRoleTable sysRoleTable = new SysRoleTable();
            sysRoleTable.setRoleName(role);
            List list = (List)newSysRoleService.getList(sysRoleTable).getData();
            if (CollectionUtils.isEmpty(list)) return false;
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询角色列表
     *
     * @return 角色列表
     */
    @RequestMapping(value = "/role/getAll")
    @ResponseBody
    public ResponseVO getAllRole() {
        try {
            return newSysRoleService.getList(null);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    /**
     * 获取所有权限信息
     *
     * @return 所有权限信息
     */
    @RequestMapping(value = "/role/getAllPrivileges")
    @ResponseBody
    public ResponseVO getAllPrivileges() {
        try {
            return newSysPrivilegesService.getList(null);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    /**
     * 根据用户Id强制注销用户
     *
     * @param userId 用户Id
     */
    private void forcedLogout(Long userId) {
        SysUserTable sysUserTable = new SysUserTable();
        sysUserTable.setId(userId);
        List<SysUserTable> users = (List)newSystemUserService.getList(sysUserTable).getData();
        SysUserTable user = users.get(0);
        Map<String, ? extends Session> userSessions = sessionRepository
                .findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, user.getUserName());
        userSessions.forEach((s, o) -> redisOperationsSessionRepository.delete(s));
    }
}
