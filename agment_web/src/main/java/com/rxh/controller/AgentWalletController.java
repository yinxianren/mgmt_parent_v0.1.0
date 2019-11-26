package com.rxh.controller;

import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.agent.AgentMerchantWalletTable;
import com.rxh.service.agent.AnewAgentMerchantService;
import com.rxh.service.agent.AnewAgentWalletService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import com.internal.playment.common.enums.SystemConstant;
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
   private AnewAgentWalletService agentWalletService;
    @Resource
    private AnewAgentMerchantService agentMerchantInfoService;
    @Resource
    private NewSystemConstantService constantService;

    @SystemLogInfo(description = "代理商钱包查询")
    @RequestMapping("/search")
    public List<AgentMerchantWalletTable> search(@RequestBody AgentMerchantWalletTable agentWallet){
        agentWallet.setAgentMerchantId(UserInfoUtils.getMerchantId().toString());
        List<AgentMerchantWalletTable> result = (List)agentWalletService.search(agentWallet).getData();
        return result;
    };
    @RequestMapping("/idsInit")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("agents", agentMerchantInfoService.list(null));
        return init;
    }
    @RequestMapping("/init")
    public Map<String, Object> initTypes() {
        Map<String, Object> init = new HashMap<>();
        init.put("payTypes", constantService.getConstantByGroupName(SystemConstant.PAYTYPE).getData());
        init.put("detailsTypes", constantService.getConstantByGroupName(SystemConstant.DETAILSTYPE).getData());
        init.put("agents", agentMerchantInfoService.list(null).getData());
        return init;
    }

    /*@SystemLogInfo(description = "代理商提现")
    @RequestMapping("/financeChange")
    public boolean orderChange(@RequestBody AgentMerchantWalletTable agentWallet){
        return agentWalletService.insert(agentWallet);
    }*/

    @RequestMapping(value="/findAgentWallteDetailsPage")
    public ResponseVO search(@RequestBody Page page ){
        page.getSearchInfo().setMerId(UserInfoUtils.getMerchantId());
        ResponseVO pageResult = agentWalletService.pageByDetails(page);
        return pageResult;
    };

}
