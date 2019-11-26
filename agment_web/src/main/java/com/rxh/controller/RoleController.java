package com.rxh.controller;

import com.internal.playment.common.table.agent.AgentPrivielgesTable;
import com.internal.playment.common.table.agent.AgentRoleTable;
import com.internal.playment.common.table.merchant.MerchantRoleTable;
import com.rxh.pojo.merchant.MerchantPrivileges;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.service.AgmentUserService;
import com.rxh.service.agent.AnewAgentRoleService;
import com.rxh.util.UserInfoUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/7/26
 * Time: 10:21
 * Project: Management
 * Package: com.rxh.controller
 */
@Controller
@RequestMapping(value = "/role")
@ResponseBody
public class RoleController {
    @Resource
    private AnewAgentRoleService anewAgentRoleService;

    @RequestMapping(value = "/getRoleList")
    public List<AgentRoleTable> getRoleList() {
        AgentRoleTable agentRoleTable = new AgentRoleTable();
        agentRoleTable.setBelongto(UserInfoUtils.getMerchantId());
        return (List)anewAgentRoleService.getList(agentRoleTable).getData();
    }

    @RequestMapping(value = "/getPrivilegeList")
    public List<AgentPrivielgesTable> getPrivilegeList() {
        return agmentUserService.selectAllPrivileges();
    }

    @RequestMapping(value = "/isExist")
    public Boolean isExist(@RequestBody AgentRoleTable merchantRole) {
        merchantRole.setBelongto(UserInfoUtils.getMerchantId());
        return agmentUserService.isRoleExist(merchantRole);
    }

    @RequestMapping(value = "/add")
    public Boolean addRole(@RequestBody MerchantRoleTable merchantRole) {
        merchantRole.setBelongto(UserInfoUtils.getMerchantId());
        return agmentUserService.addRole(merchantRole, UserInfoUtils.getUserName(),null);
    }

    @RequestMapping(value = "/update")
    public Boolean updateRole(@RequestBody MerchantRoleTable merchantRole) {
        merchantRole.setBelongto(UserInfoUtils.getMerchantId());
        return agmentUserService.updateRoleByRoleNameAndBelongTo(merchantRole, UserInfoUtils.getUserName());
    }

    @RequestMapping(value = "/delete")
    public Boolean deleteRole(@RequestBody String roleName) {
        return agmentUserService.deleteRoleByRoleNameAndBelongTo(roleName, UserInfoUtils.getMerchantId());
    }
}