package com.rxh.controller;

import com.rxh.pojo.Result;
import com.rxh.service.square.AgentMerchantSettingService;
import com.rxh.square.pojo.AgentMerchantSetting;
import com.rxh.cache.RedisCacheCommonCompoment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/agentMerchantSetting")
public class AgentMerchantSettingController {

    @Resource
    private AgentMerchantSettingService agentMerchantSettingService;

    @Autowired
    private RedisCacheCommonCompoment redisCacheCommonCompoment;

    @RequestMapping("/search")
    public List<AgentMerchantSetting> search(@RequestBody AgentMerchantSetting agentMerchantSetting){
        return agentMerchantSettingService.search(agentMerchantSetting.getAgentMerchantId());
    }

    @RequestMapping("/update")
    public Result update(@RequestBody  List<AgentMerchantSetting> param ){
        Result result = agentMerchantSettingService.update(param);
        return result;

    }
}
