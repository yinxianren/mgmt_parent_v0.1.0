package com.rxh.controller.anew.merchant;

import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.pojo.Result;
import com.rxh.service.AnewMerchantQuotaRiskService;
import com.rxh.service.square.MerchantQuotaRiskService;
import com.rxh.square.pojo.MerchantQuotaRisk;
import com.rxh.square.pojo.MerchantSetting;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchantQuotaRisk")
public class AnewMerchantQuotaRiskController {

    @Autowired
    private AnewMerchantQuotaRiskService anewMerchantQuotaRiskService;

    @RequestMapping("/search")
    public ResponseVO search(@RequestBody MerchantQuotaRiskTable merchantQuotaRiskTable){
        return anewMerchantQuotaRiskService.search(merchantQuotaRiskTable);
    }
    @RequestMapping("/update")
    public ResponseVO update(@RequestBody  MerchantQuotaRiskTable merchantQuotaRiskTable){
        return anewMerchantQuotaRiskService.saveOrUpdate(merchantQuotaRiskTable);
    }

}
