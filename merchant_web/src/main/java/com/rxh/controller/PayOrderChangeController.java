package com.rxh.controller;

import com.rxh.pojo.Result;
import com.internal.playment.common.page.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.ConstantService;
import com.rxh.service.square.PayOrderChangeService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.PayOrder;
import com.rxh.square.pojo.PayOrderChange;
import com.rxh.util.UserInfoUtils;
import com.internal.playment.common.enums.SystemConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/payOrderChange")
public class PayOrderChangeController {

    @Resource
    private PayOrderChangeService payOrderChangeService;

    @Resource
    private ConstantService constantService;

    @SystemLogInfo(description = "异常交易查询")
    @RequestMapping(value="/findPayOrderPage")
    public PageResult findPayOrderPage(@RequestBody Page page ) {
        page.getSearchInfo().setMerId(String.valueOf(UserInfoUtils.getMerchantId()));
        PageResult pageResult = payOrderChangeService.findPayOrder(page);
        return pageResult;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("customerId", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.customerId));
        init.put("exceptionId", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.exceptionId));
        init.put("payId", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.payId));
        init.put("type", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.type));

        return init;
    }

    @RequestMapping("/orderChange")
    public boolean orderChange(@RequestBody PayOrder payOrder){
        return payOrderChangeService.insert(payOrder);
    }
}
