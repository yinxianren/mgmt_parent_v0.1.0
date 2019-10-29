package com.rxh.controller.anew.merchant;

import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.pojo.Result;
import com.rxh.service.square.MerchantQuotaRiskService;
import com.rxh.square.pojo.MerchantQuotaRisk;
import com.rxh.square.pojo.MerchantSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchantQuotaRisk")
public class AnewMerchantQuotaRiskController {

    @Autowired
    private MerchantQuotaRiskService merchantQuotaRiskService;
    @Autowired
    private RedisCacheCommonCompoment redisCacheCommonCompoment;

    @RequestMapping("/search")
    public Result search(@RequestBody MerchantSetting merchantSetting){
        //modify by gjm 添加redis  at 20190809 start
        Result<MerchantQuotaRisk> result = new Result<>();
        MerchantQuotaRisk risk = redisCacheCommonCompoment.merchantQuotaRiskCache.getOne(merchantSetting.getMerId());
        if(null == risk){
            result = merchantQuotaRiskService.search(merchantSetting.getMerId());
        }else{
            result.setCode(Result.SUCCESS);
            result.setMsg("获取商户风控信息成功");
            result.setData(risk);
        }
        //modify by gjm 添加redis  at 20190809 end
        return result;
    }
    @RequestMapping("/update")
    public Result update(@RequestBody  MerchantQuotaRisk merchantQuotaRisk){

        return merchantQuotaRiskService.update(merchantQuotaRisk);
    }

}
