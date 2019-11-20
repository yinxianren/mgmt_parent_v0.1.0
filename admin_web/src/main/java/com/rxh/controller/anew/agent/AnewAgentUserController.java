package com.rxh.controller.anew.agent;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.agent.AgentRoleTable;
import com.internal.playment.common.table.agent.AgentUserTable;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.AgmentUserService;
import com.rxh.service.agent.AnewAgentRoleService;
import com.rxh.service.agent.AnewAgentUserService;
import com.rxh.spring.annotation.SystemLogInfo;
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
@RequestMapping(value = "/agent")
public class AnewAgentUserController {

    @Autowired
    private AnewAgentUserService anewAgentUserService;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AnewAgentRoleService anewAgentRoleService;

    @SystemLogInfo(description = "新增商户用户")
    @RequestMapping(value = "/addMerchantUser")
    @ResponseBody
    public ResponseVO addMerchantUser(@RequestBody AgentUserTable agentUserTable) {
        try {
            agentUserTable.setPassword(passwordEncoder.encode(agentUserTable.getPassword()));
            agentUserTable.setCreator(UserInfoUtils.getName());
            agentUserTable.setCreateTime(new Date());
            return anewAgentUserService.saveOrUpdate(agentUserTable);
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
            return anewAgentUserService.delByIds(idList);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "更新商户用户")
    @RequestMapping(value = "/updateMerchantUser")
    @ResponseBody
    public ResponseVO updateMerchantUser(@RequestBody AgentUserTable agentUserTable) {
        if (agentUserTable.getPassword() != null) {
            agentUserTable.setPassword(passwordEncoder.encode(agentUserTable.getPassword()));
        }
        try {
            agentUserTable.setModifier(UserInfoUtils.getName());
            agentUserTable.setModifyTime(new Date());
            return anewAgentUserService.saveOrUpdate(agentUserTable);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @SystemLogInfo(description = "获取商户用户")
    @RequestMapping(value = "/getMerchantUserByMerchantId")
    @ResponseBody
    public ResponseVO getMerchantUserByMerchantId(@RequestBody AgentUserTable agentUserTable) {
        try {
            return anewAgentUserService.getList(agentUserTable);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @RequestMapping(value = "/getMerchantRoleByMerchantId")
    @ResponseBody
    public ResponseVO getMerchantRoleByMerchantId(@RequestBody AgentRoleTable agentRoleTable) {
        try {
            return anewAgentRoleService.getList(agentRoleTable);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }

    @RequestMapping(value = "/isMerchantUserExist")
    @ResponseBody
    public ResponseVO isMerchantUserExist(@RequestBody AgentUserTable agentUserTable) {
        try {
            List list = (List)anewAgentUserService.getList(agentUserTable).getData();
            if (CollectionUtils.isEmpty(list)) return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._0.getRemark());
            return new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
        }
    }
}