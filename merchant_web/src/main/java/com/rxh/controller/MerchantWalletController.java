package com.rxh.controller;

import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.MerchantWalletService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.MerchantWallet;
import com.rxh.util.UserInfoUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/merchantWallet")
public class MerchantWalletController {

    @Resource
    private MerchantWalletService merchantWalletService;

    @Resource
    private MerchantInfoService merchantInfoService;

    @SystemLogInfo(description = "余额查询")
    @RequestMapping("/search")
    public List<MerchantWallet> search(@RequestBody MerchantWallet merchantWallet){
        merchantWallet.setMerId(UserInfoUtils.getMerchantId().toString());
        List<MerchantWallet> result = merchantWalletService.search(merchantWallet);
        return result;
    }

    @RequestMapping("/idsInit")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("agents", merchantInfoService.getIdsAndName());
        return init;
    }
    @SystemLogInfo(description = "余额提现")
    @RequestMapping("/financeChange")
    public boolean orderChange(@RequestBody MerchantWallet merchantWallet){
        return merchantWalletService.insert(merchantWallet);
    }

}
