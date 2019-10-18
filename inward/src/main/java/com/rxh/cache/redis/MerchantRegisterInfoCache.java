package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.service.merchant.MerchantRegisterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MerchantRegisterInfoCache extends AbstractPayCache {

    private final String tableMapKeyName="merchant_register_info";

    @Autowired
    protected MerchantRegisterInfoService merchantRegisterInfoService;

    public List<MerchantRegisterInfo>  getAll(){
        List<MerchantRegisterInfo>  merchantRegisterInfoList=redisTemplate.opsForHash()
                .values(tableMapKeyName)
                .stream()
                .map(obj->(MerchantRegisterInfo)obj)
                .distinct()
                .collect(Collectors.toList());
        if(!isHasElement(merchantRegisterInfoList)){
            merchantRegisterInfoList=merchantRegisterInfoService.selecAll();
            if(isHasElement(merchantRegisterInfoList)){
                Map<Object,Object> map= new HashMap<>(merchantRegisterInfoList.size());
                for(MerchantRegisterInfo mri :merchantRegisterInfoList){
                    map.put(mri.getId(),mri);
                }
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return merchantRegisterInfoList;
    }

}
