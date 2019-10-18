package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.service.merchant.MerchantRegisterCollectService;
import com.rxh.square.pojo.MerchantRegisterCollect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MerchantRegisterCollectCache  extends AbstractPayCache {

    private final String tableMapKeyName="merchant_register_collect";

    @Autowired
    protected MerchantRegisterCollectService merchantRegisterCollectService;


    public List<MerchantRegisterCollect> getAll(){
        List<MerchantRegisterCollect> merchantRegisterCollectList=redisTemplate.opsForHash()
                .values(tableMapKeyName)
                .stream()
                .map(obj->(MerchantRegisterCollect)obj)
                .distinct()
                .collect(Collectors.toList());
        if(!isHasElement(merchantRegisterCollectList)){
            merchantRegisterCollectList=merchantRegisterCollectService.selectByWhereCondition(new MerchantRegisterCollect().lsetStatus(0));
            if(isHasElement(merchantRegisterCollectList)){
                Map<Object,Object> map= new HashMap<>(merchantRegisterCollectList.size());
                for(MerchantRegisterCollect mgc : merchantRegisterCollectList){
                    map.put(mgc.getId(),mgc);
                }
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return merchantRegisterCollectList;
    }
}
