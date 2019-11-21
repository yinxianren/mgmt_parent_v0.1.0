package com.rxh.controller;


import com.rxh.service.ConstantService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.service.square.FinanceDrawingService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.FinanceDrawing;
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
@RequestMapping("/financeDrawing")
public class FinanceDrawingController {

    @Resource
    private FinanceDrawingService financeDrawingService;
    @Resource
    private AgentMerchantInfoService agentMerchantInfoService;

    @Resource
    private ConstantService constantService;
    @RequestMapping("/search")
    public List<FinanceDrawing> search(@RequestBody FinanceDrawing financeDrawing){
        financeDrawing.setCustomerId(UserInfoUtils.getMerchantId().toString());
        List<FinanceDrawing> result = financeDrawingService.search(financeDrawing);
        return result;
    }

    @RequestMapping("/idsInit")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("customerId", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.customerId));
        init.put("id", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.id));
        init.put("agents", agentMerchantInfoService.getAllIdAndName());
        return init;
    }
}
