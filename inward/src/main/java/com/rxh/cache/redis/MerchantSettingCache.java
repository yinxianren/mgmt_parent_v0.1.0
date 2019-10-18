package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.service.square.MerchantSquareSettingService;
import com.rxh.square.pojo.MerchantSetting;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MerchantSettingCache extends AbstractPayCache {

    private final String tableMapKeyName="merchant_setting";

    @Resource
    private MerchantSquareSettingService merchantSquareSettingService;

    /**
     *
     * @param merchantId
     * @return
     */
    public MerchantSetting getOne(String merchantId){
        MerchantSetting  merchantSetting= (MerchantSetting) redisTemplate.opsForHash().get(tableMapKeyName,merchantId);
        if(isNull(merchantSetting)){
            List<MerchantSetting> merchantSettingList= merchantSquareSettingService.getAll();
            if(isHasElement(merchantSettingList)){
                Map<Object,Object> map= new HashMap<>(merchantSettingList.size());
               for(MerchantSetting ms:merchantSettingList){
                   if(ms.getMerId().equals(merchantId)){
                       merchantSetting=ms;
                   }
                  map.put(ms.getMerId(),ms);
               }
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return merchantSetting;
    }

    private void put(MerchantSetting merchantSetting, String objectKey) {
        redisTemplate.opsForHash().put(tableMapKeyName,objectKey,merchantSetting);
    }
}
