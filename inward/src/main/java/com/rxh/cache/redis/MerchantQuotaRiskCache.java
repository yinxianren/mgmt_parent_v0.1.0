package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.square.pojo.MerchantQuotaRisk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MerchantQuotaRiskCache extends AbstractPayCache {

    private final String tableMapKeyName="merchant_quota_risk";
    @Autowired
    private PaymentRecordSquareService paymentRecordSquareService;

    /**
     *
     * @param merchantId
     * @return
     */
    public MerchantQuotaRisk getOne(String merchantId){
        MerchantQuotaRisk merchantQuotaRisk= (MerchantQuotaRisk) redisTemplate.opsForHash().get(tableMapKeyName,merchantId);
        if(isNull(merchantQuotaRisk)){
            merchantQuotaRisk=paymentRecordSquareService.getMerchantQuotaRiskByMerId(merchantId);
            if(isNotNull(merchantQuotaRisk)){
               final MerchantQuotaRisk mqr=merchantQuotaRisk;
               pool.execute(()->put(tableMapKeyName,mqr,merchantId));
            }
        }
        return merchantQuotaRisk;
    }

}
