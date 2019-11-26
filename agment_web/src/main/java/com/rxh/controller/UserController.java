package com.rxh.controller;

import com.internal.playment.common.table.agent.AgentUserTable;
import com.rxh.pojo.merchant.MerchantPrivileges;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.AgmentUserService;
import com.rxh.service.agent.AnewAgentUserService;
import com.rxh.util.UserInfoUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/8
 * Time: 9:54
 * Project: MySSM
 * Package: com.chan.controller
 */
@Controller
@RequestMapping(value = "/user")
@ResponseBody
public class UserController {
    @Resource
    private AnewAgentUserService agmentUserService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 获取用户菜单
     *
     * @return 用户菜单
     */
    @RequestMapping(value = "/getMenuList")
    public List<MerchantPrivileges> getMenuList() {
        return agmentUserService.getMenu(UserInfoUtils.getUserName(), UserInfoUtils.getMerchantId());
    }

    @RequestMapping(value = "/getUserList")
    public List<AgentUserTable> getUserList() {
        return agmentUserService.getUserByMerchantId(UserInfoUtils.getMerchantId());
    }

    @RequestMapping(value = "/getRoleList")
    public List<MerchantRole> getRoleList() {
        return agmentUserService.getRoleByMerchantId(UserInfoUtils.getMerchantId());
    }

    @RequestMapping(value = "/isExist")
    public Boolean isExist(@RequestBody MerchantUser merchantUser) {
        merchantUser.setBelongto(UserInfoUtils.getMerchantId());
        return agmentUserService.isUserExist(merchantUser);
    }

    @RequestMapping(value = "/add")
    public Boolean addUser(@RequestBody MerchantUser merchantUser) {
        merchantUser.setBelongto(UserInfoUtils.getMerchantId());
        merchantUser.setPassword(passwordEncoder.encode(merchantUser.getPassword()));
        return agmentUserService.addUser(merchantUser, UserInfoUtils.getUserName());
    }

    @RequestMapping(value = "/update")
    public Boolean updateUser(@RequestBody MerchantUser merchantUser) {
        if (merchantUser.getPassword() != null) {
            merchantUser.setPassword(passwordEncoder.encode(merchantUser.getPassword()));
        }
        merchantUser.setBelongto(UserInfoUtils.getMerchantId());
        return agmentUserService.updateUserByUserNameAndBelongTo(merchantUser, UserInfoUtils.getUserName());
    }

    @RequestMapping(value = "/delete")
    public Boolean deleteUser(@RequestBody String userName) {
        return agmentUserService.deleteUserByUserNameAndBelongTo(userName, UserInfoUtils.getMerchantId());
    }

    @RequestMapping(value = "/getPrivilegeList")
    public List<MerchantPrivileges> getPrivilegeList() {
        return agmentUserService.selectAllPrivileges();
    }
}