package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.service.square.MerchantSquareRateService;
import com.rxh.square.pojo.MerchantRate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MerchantRateCache extends AbstractPayCache{

    private final String tableMapKeyName="merchant_rate";

    @Resource
    private MerchantSquareRateService merchantSquareRateService;

    /**
     *
     * @param merchanrtId
     * @param payType
     * @return
     */
    public MerchantRate  getOne(String merchanrtId,String payType){
        String key=merchanrtId+"-"+payType;
        MerchantRate  merchantRate= (MerchantRate) redisTemplate.opsForHash().get(tableMapKeyName,key);
        if(isNull(merchantRate)){
            List<MerchantRate> merchantRateList=merchantSquareRateService.getAll();
            if(isHasElement(merchantRateList)){
                Map<Object,Object> map=new HashMap<>(merchantRateList.size());
                merchantRateList.forEach(t->{
                    map.put(t.getMerId()+"-"+t.getPayType(),t);
                });
                merchantRate= (MerchantRate) map.get(key);
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return merchantRate;
    }

    /**
     *
     * @return
     */
    public List<MerchantRate> getAll(){
        List<MerchantRate> merchantRateList=redisTemplate.opsForHash().values(tableMapKeyName)
                .stream()
                .map(t->(MerchantRate)t)
                .distinct()
                .collect(Collectors.toList());
        if(!isHasElement(merchantRateList)){
            merchantRateList=merchantSquareRateService.getAll();
            if(isHasElement(merchantRateList)){
                Map<Object,Object> map=new HashMap<>();
                merchantRateList.forEach(t->{
                    map.put(t.getMerId()+"-"+t.getPayType(),t);
                });
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return merchantRateList;
    }

}
