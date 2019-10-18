package com.rxh.controller;


import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.ConstantService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.SystemOrderTrackService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.utils.SystemConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/systemOrderTrack")
public class SystemOrderTrackController {

    @Resource
    private SystemOrderTrackService systemOrderTrackService;
    @Resource
    private ConstantService constantService;
    @Resource
    private MerchantInfoService merchantInfoService;
    @SystemLogInfo(description = "订单信息追踪查询")
    @RequestMapping(value="/findSystemOrder")
    public PageResult findSystemOrder(@RequestBody Page page ) {
        PageResult pageResult = systemOrderTrackService.findSystemOrder(page);
        return pageResult;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("id", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.sysId));
        init.put("returnUrl", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.returnUrl));
        init.put("noticeUrl", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.noticeUrl));
        init.put("orderTrackStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.orderTrackStatus));
        init.put("tradeInfo", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.tradeInfo));
        init.put("refer", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.refer));
        init.put("merchants", merchantInfoService.getIdsAndName());
        return init;
    }

}
