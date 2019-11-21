package com.rxh.controller;

import com.internal.playment.common.page.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.ConstantService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.service.square.AgentWalletService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.AgentWallet;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agentWallet")
public class AgentWalletController {

    @Resource
   private AgentWalletService agentWalletService;
    @Resource
    private AgentMerchantInfoService agentMerchantInfoService;
    @Resource
    private ConstantService constantService;

    @SystemLogInfo(description = "代理商钱包查询")
    @RequestMapping("/search")
    public List<AgentWallet> search(@RequestBody  AgentWallet agentWallet){
        agentWallet.setAgentMerchantId(UserInfoUtils.getMerchantId().toString());
        List<AgentWallet> result = agentWalletService.search(agentWallet);
        return result;
    };
    @RequestMapping("/idsInit")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("agents", agentMerchantInfoService.getAllIdAndName());
        return init;
    }
    @RequestMapping("/init")
    public Map<String, Object> initTypes() {
        Map<String, Object> init = new HashMap<>();
        init.put("payTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("detailsTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.DETAILSTYPE));
        init.put("agents", agentMerchantInfoService.getAllIdAndName());
        return init;
    }

    @SystemLogInfo(description = "代理商提现")
    @RequestMapping("/financeChange")
    public boolean orderChange(@RequestBody AgentWallet agentWallet){
        return agentWalletService.insert(agentWallet);
    }

    @RequestMapping(value="/findAgentWallteDetailsPage")
    public PageResult search(@RequestBody Page page ){
        page.getSearchInfo().setMerId(UserInfoUtils.getMerchantId().toString());
        PageResult pageResult = agentWalletService.findAgentWallteDetails(page);
        return pageResult;
    };

}
