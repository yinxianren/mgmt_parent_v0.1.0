package com.rxh.controller;

import com.rxh.pojo.merchant.MerchantPrivileges;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.service.MerchantUserService;
import com.rxh.util.UserInfoUtils;
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
    private MerchantUserService merchantUserService;

    @RequestMapping(value = "/getRoleList")
    public List<MerchantRole> getRoleList() {
        return merchantUserService.getRoleByMerchantId(UserInfoUtils.getMerchantId());
    }

    @RequestMapping(value = "/getPrivilegeList")
    public List<MerchantPrivileges> getPrivilegeList() {
        return merchantUserService.selectAllPrivileges();
    }

    @RequestMapping(value = "/isExist")
    public Boolean isExist(@RequestBody MerchantRole merchantRole) {
        merchantRole.setBelongto(UserInfoUtils.getMerchantId());
        return merchantUserService.isRoleExist(merchantRole);
    }

    @RequestMapping(value = "/add")
    public Boolean addRole(@RequestBody MerchantRole merchantRole) {
        merchantRole.setBelongto(UserInfoUtils.getMerchantId());
        return merchantUserService.addRole(merchantRole, UserInfoUtils.getUserName(),null);
    }

    @RequestMapping(value = "/update")
    public Boolean updateRole(@RequestBody MerchantRole merchantRole) {
        merchantRole.setBelongto(UserInfoUtils.getMerchantId());
        return merchantUserService.updateRoleByRoleNameAndBelongTo(merchantRole, UserInfoUtils.getUserName());
    }

    @RequestMapping(value = "/delete")
    public Boolean deleteRole(@RequestBody String roleName) {
        return merchantUserService.deleteRoleByRoleNameAndBelongTo(roleName, UserInfoUtils.getMerchantId());
    }
}