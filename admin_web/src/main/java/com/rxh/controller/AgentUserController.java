package com.rxh.controller;

import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.AgmentUserService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.GlobalConfiguration;
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
 * Date: 2018/3/21
 * Time: 14:51
 * Project: Management
 * Package: com.rxh.controller
 */
@Controller
@RequestMapping(value = "/agent")
public class AgentUserController {

    @Resource
    private AgmentUserService  agmentUserService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private GlobalConfiguration globalConfiguration;





    @SystemLogInfo(description = "新增商户用户")
    @RequestMapping(value = "/addMerchantUser")
    @ResponseBody
    public Boolean addMerchantUser(@RequestBody MerchantUser merchantUser) {
        merchantUser.setPassword(passwordEncoder.encode(merchantUser.getPassword()));
        return agmentUserService.addUser(merchantUser, UserInfoUtils.getName());
    }

    @SystemLogInfo(description = "删除商户用户")
    @RequestMapping(value = "/deleteMerchantUser")
    @ResponseBody
    public Boolean deleteMerchantUser(@RequestBody List<Long> idList) {
        return agmentUserService.deleteUserByIdList(idList);
    }

    @SystemLogInfo(description = "更新商户用户")
    @RequestMapping(value = "/updateMerchantUser")
    @ResponseBody
    public Boolean updateMerchantUser(@RequestBody MerchantUser merchantUser) {
        if (merchantUser.getPassword() != null) {
            merchantUser.setPassword(passwordEncoder.encode(merchantUser.getPassword()));
        }
        return agmentUserService.updateUserById(merchantUser, UserInfoUtils.getName());
    }

    @SystemLogInfo(description = "获取商户用户")
    @RequestMapping(value = "/getMerchantUserByMerchantId")
    @ResponseBody
    public List<MerchantUser> getMerchantUserByMerchantId(@RequestBody MerchantUser merchantUser) {
        return agmentUserService.getUserByMerchantId(merchantUser.getBelongto());
    }

    @RequestMapping(value = "/getMerchantRoleByMerchantId")
    @ResponseBody
    public List<MerchantRole> getMerchantRoleByMerchantId(@RequestBody MerchantUser merchantUser) {
        return agmentUserService.getRoleByMerchantId(merchantUser.getBelongto());
    }

    @RequestMapping(value = "/isMerchantUserExist")
    @ResponseBody
    public Boolean isMerchantUserExist(@RequestBody MerchantUser merchantUser) {
        return agmentUserService.isUserExist(merchantUser);
    }
}