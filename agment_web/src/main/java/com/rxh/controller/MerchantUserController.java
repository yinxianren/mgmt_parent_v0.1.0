package com.rxh.controller;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantRoleTable;
import com.internal.playment.common.table.merchant.MerchantUserTable;
import com.rxh.service.merchant.AnewMerchantRoleService;
import com.rxh.service.merchant.AnewMerchantUserService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
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
@RequestMapping(value = "/merchant")
public class MerchantUserController {

    @Resource
    private AnewMerchantUserService merchantUserService;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AnewMerchantRoleService anewMerchantRoleService;




    @SystemLogInfo(description = "新增商户用户")
    @RequestMapping(value = "/addMerchantUser")
    @ResponseBody
    public Boolean addMerchantUser(@RequestBody MerchantUserTable merchantUser) {
        merchantUser.setPassword(passwordEncoder.encode(merchantUser.getPassword()));
        merchantUser.setCreator( UserInfoUtils.getName());
        merchantUser.setCreateTime(new Date());
        merchantUser.setModifyTime(new Date());
        ResponseVO responseVO = merchantUserService.saveOrUpdate(merchantUser);
        if (responseVO.getCode() == StatusEnum._0.getStatus()){
            return true;
        }
        return false;
    }

    @SystemLogInfo(description = "删除商户用户")
    @RequestMapping(value = "/deleteMerchantUser")
    @ResponseBody
    public Boolean deleteMerchantUser(@RequestBody List<Long> idList) {
        ResponseVO responseVO = merchantUserService.delByIds(idList);
        if (responseVO.getCode() == StatusEnum._0.getStatus()) return true;
        return false;
    }

    @SystemLogInfo(description = "更新商户用户")
    @RequestMapping(value = "/updateMerchantUser")
    @ResponseBody
    public Boolean updateMerchantUser(@RequestBody MerchantUserTable merchantUser) {
        if (merchantUser.getPassword() != null) {
            merchantUser.setPassword(passwordEncoder.encode(merchantUser.getPassword()));
        }
        merchantUser.setModifyTime(new Date());
        merchantUser.setModifier(UserInfoUtils.getName());
        ResponseVO responseVO = merchantUserService.saveOrUpdate(merchantUser);
        if (responseVO.getCode() == StatusEnum._0.getStatus()) return true;
        return false;
    }

    @SystemLogInfo(description = "获取商户用户")
    @RequestMapping(value = "/getMerchantUserByMerchantId")
    @ResponseBody
    public List<MerchantUserTable> getMerchantUserByMerchantId(@RequestBody MerchantUserTable merchantInfo) {
        List<MerchantUserTable> merchantUsers =(List) merchantUserService.getList(merchantInfo).getData();
        return merchantUsers;
    }

    @RequestMapping(value = "/getMerchantRoleByMerchantId")
    @ResponseBody
    public List<MerchantRoleTable> getMerchantRoleByMerchantId(@RequestBody MerchantRoleTable merchantInfo) {
        return (List)anewMerchantRoleService.getList(merchantInfo).getData();
    }

    @RequestMapping(value = "/isMerchantUserExist")
    @ResponseBody
    public Boolean isMerchantUserExist(@RequestBody MerchantUserTable merchantUser) {
        List list = (List)merchantUserService.getList(merchantUser).getData();
        if (CollectionUtils.isEmpty(list)) return false;
        return true;
    }
}