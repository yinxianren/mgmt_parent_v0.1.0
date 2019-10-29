package com.rxh.controller.anew.agent;

import com.rxh.anew.table.agent.AgentMerchantSettingTable;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.pojo.Result;
import com.rxh.service.AnewAgentMerchantSettingService;
import com.rxh.service.ConstantService;
import com.rxh.service.ProductTypeSettingService;
import com.rxh.service.square.AgentMerchantSettingService;
import com.rxh.square.pojo.AgentMerchantSetting;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agentMerchantSetting")
public class AnewAgentMerchantSettingController {

    @Resource
    private AnewAgentMerchantSettingService anewAgentMerchantSettingService;
    @Autowired
    private ProductTypeSettingService productTypeSettingService;
    @Autowired
    private ConstantService constantService;

    @RequestMapping("/search")
    public ResponseVO search(@RequestBody AgentMerchantSettingTable agentMerchantSetting){
        return anewAgentMerchantSettingService.getList(agentMerchantSetting);
    }

    @RequestMapping("/update")
    public ResponseVO update(@RequestBody  List<AgentMerchantSettingTable> param ){
        return anewAgentMerchantSettingService.betchUpdate(param);
    }

    @RequestMapping("/init")
    public Map<String, Object> init(){
        Map<String, Object> init = new HashMap<>();
        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
        init.put("agentRate",constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PRODUCTTYPE));
        return init;
    }
}
