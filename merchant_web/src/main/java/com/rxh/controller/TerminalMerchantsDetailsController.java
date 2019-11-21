package com.rxh.controller;


import com.internal.playment.common.page.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.TerminalMerchantsDetailsService;
import com.rxh.util.UserInfoUtils;
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

    @RequestMapping(value="/findTerminalMerchantsDetails")
    public PageResult findTerminalMerchantsDetails(@RequestBody Page page ) {
        page.getSearchInfo().setMerId(UserInfoUtils.getMerchantId().toString());
        PageResult pageResult = terminalMerchantsDetailsServcie.terminalMerchantsDetails(page);
        return pageResult;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchants", merchantInfoService.getIdsAndName());
        init.put("terminalMerIds", terminalMerchantsDetailsServcie.getIds());
        return init;
    }
}
