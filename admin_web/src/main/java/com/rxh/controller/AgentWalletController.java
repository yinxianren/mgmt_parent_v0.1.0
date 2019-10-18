package com.rxh.controller;

import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.ConstantService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.service.square.AgentWalletService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.AgentMerchantInfo;
import com.rxh.square.pojo.AgentWallet;
import com.rxh.utils.SystemConstant;
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
    private ConstantService constantService;
    @Resource
    private AgentMerchantInfoService agentMerchantInfoService;


    @SystemLogInfo(description = "代理商钱包查询")
    @RequestMapping("/search")
    public List<AgentWallet> search(@RequestBody  AgentWallet agentWallet){

        List<AgentWallet> result = agentWalletService.search(agentWallet);
        return result;
    };
    @RequestMapping("/batchDel")
    public Result delete(@RequestBody List<String> ids){
        return agentWalletService.deleteByPrimaryKey(ids);
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("payTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("detailsTypes",constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.DETAILSTYPE));
        init.put("agents", agentMerchantInfoService.getAllIdAndName());
        return init;
    }
    @RequestMapping(value="/findAgentWallteDetailsPage")
    public PageResult search(@RequestBody Page page ){

        PageResult pageResult = agentWalletService.findAgentWallteDetails(page);
        return pageResult;
    };

}
