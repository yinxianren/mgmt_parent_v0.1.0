package com.rxh.controller;


import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.ConstantService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.TerminalMerchantsDetailsService;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/terminalMerchantsDetails")
public class TerminalMerchantsDetailsController {

    @Resource
    private TerminalMerchantsDetailsService terminalMerchantsDetailsServcie;
    @Resource
    private MerchantInfoService merchantInfoService;
    @Resource
    private ConstantService constantService;

    @RequestMapping(value="/findTerminalMerchantsDetails")
    public PageResult findTerminalMerchantsDetails(@RequestBody Page page ) {
        PageResult pageResult = terminalMerchantsDetailsServcie.terminalMerchantsDetails(page);
        return pageResult;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchants", merchantInfoService.getIdsAndName());
        init.put("terminalMerIds", terminalMerchantsDetailsServcie.getIds());
        init.put("payTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("detailsTypes",constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.DETAILSTYPE));
        return init;
    }
}
