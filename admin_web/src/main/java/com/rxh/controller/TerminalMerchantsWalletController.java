package com.rxh.controller;

import com.rxh.pojo.Result;
import com.rxh.service.ConstantService;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.service.square.TerminalMerchantsWalletService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.ChannelWallet;
import com.rxh.square.pojo.TerminalMerchantsWallet;
import com.rxh.utils.SystemConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/terminalMerchantsWallet")
public class TerminalMerchantsWalletController {

    @Resource
    private TerminalMerchantsWalletService terminalMerchantsWalletService;

    @Resource
    private ConstantService constantService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private MerchantInfoService merchantInfoService;
    @SystemLogInfo(description = "终端商户钱包查询")
    @RequestMapping("/search")
    public List<TerminalMerchantsWallet> search(@RequestBody TerminalMerchantsWallet terminalMerchantsWallet){

        List<TerminalMerchantsWallet> result = terminalMerchantsWalletService.getWalletByParam(terminalMerchantsWallet);
        return result;
    };


    @RequestMapping("/idsInit")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("paytype", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        // init.put("channels", terminalMerchantsWalletService.getIdsAndName());
        init.put("organizations", organizationService.getIdsAndName());
        init.put("merchants", merchantInfoService.getIdsAndName());
        return init;
    }
}
