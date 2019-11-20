package com.rxh.controller.anew.merchant;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.merchant.MerchantRoleTable;
import com.internal.playment.common.table.merchant.MerchantUserTable;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.MerchantUserService;
import com.rxh.service.merchant.AnewMerchantRoleService;
import com.rxh.service.merchant.AnewMerchantUserService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.GlobalConfiguration;
import com.rxh.vo.ResponseVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
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
public class AnewMerchantUserController {

    @Autowired
    private AnewMerchantUserService anewMerchantUserService;
    @Autowired
    private AnewMerchantRoleService anewMerchantRoleService;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @SystemLogInfo(description = "新增商户用户")
    @RequestMapping(value = "/addMerchantUser")
    @ResponseBody
    public ResponseVO addMerchantUser(@RequestBody MerchantUserTable merchantUser) {
        try {
            merchantUser.setPassword(passwordEncoder.encode(merchantUser.getPassword()));
            merchantUser.setCreator(UserInfoUtils.getName());
            merchantUser.setCreateTime(new Date());
            return anewMerchantUserService.saveOrUpdate(merchantUser);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }

    }

    @SystemLogInfo(description = "删除商户用户")
    @RequestMapping(value = "/deleteMerchantUser")
    @ResponseBody
    public ResponseVO deleteMerchantUser(@RequestBody List<Long> idList) {
        try {
            return anewMerchantUserService.delByIds(idList);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "更新商户用户")
    @RequestMapping(value = "/updateMerchantUser")
    @ResponseBody
    public ResponseVO updateMerchantUser(@RequestBody MerchantUserTable merchantUser) {
        try {
            if (merchantUser.getPassword() != null) {
                merchantUser.setPassword(passwordEncoder.encode(merchantUser.getPassword()));
            }
            merchantUser.setModifyTime(new Date());
            merchantUser.setModifier(UserInfoUtils.getName());
            return anewMerchantUserService.saveOrUpdate(merchantUser);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "获取商户用户")
    @RequestMapping(value = "/getMerchantUserByMerchantId")
    @ResponseBody
    public ResponseVO getMerchantUserByMerchantId(@RequestBody MerchantUserTable merchantInfo) {
        try {
            return anewMerchantUserService.getList(merchantInfo);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @RequestMapping(value = "/getMerchantRoleByMerchantId")
    @ResponseBody
    public ResponseVO getMerchantRoleByMerchantId(@RequestBody MerchantRoleTable merchantInfo) {
        try {
            return anewMerchantRoleService.getList(merchantInfo);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @RequestMapping(value = "/isMerchantUserExist")
    @ResponseBody
    public ResponseVO isMerchantUserExist(@RequestBody MerchantUserTable merchantUser) {
        try {
            List<MerchantUserTable> list = (List)anewMerchantUserService.getList(merchantUser).getData();
            if (CollectionUtils.isEmpty(list)) return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._0.getRemark());
            return new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }
}