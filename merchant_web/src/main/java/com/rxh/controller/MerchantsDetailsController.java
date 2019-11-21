package com.rxh.controller;


import com.internal.playment.common.page.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.ConstantService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.MerchantsDetailsService;
import com.rxh.util.UserInfoUtils;
import com.internal.playment.common.enums.SystemConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/merchantsDetails")
public class MerchantsDetailsController {

    @Resource
    private MerchantsDetailsService merchantsDetailsService;
    @Resource
    private MerchantInfoService merchantInfoService;
    @Resource
    private ConstantService constantService;

    @RequestMapping(value="/findMerchantsDetails")
    public PageResult findMerchantsDetails(@RequestBody Page page ) {
        page.getSearchInfo().setMerId(UserInfoUtils.getMerchantId().toString());
        PageResult pageResult = merchantsDetailsService.merchantsDetails(page);
        return pageResult;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchants", merchantInfoService.getIdsAndName());
        init.put("payTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("detailsTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.DETAILSTYPE));
        return init;
    }
}
