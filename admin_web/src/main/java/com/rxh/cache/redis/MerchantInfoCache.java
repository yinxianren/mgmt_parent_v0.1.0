package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.pojo.Result;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.square.pojo.MerchantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  描述：商户信息缓存对象， 需要商户级别的锁
 * @author panda
 * @date 20190802
 *
 */
@Component
public class MerchantInfoCache extends AbstractPayCache {

    private final String tableMapKeyName="merchant_info";
    @Autowired
    private MerchantInfoService  merchantInfoService;


    /**
     *
     * @param merId
     * @return
     */
    public MerchantInfo getOne(String merId){
           MerchantInfo merchantInfo= (MerchantInfo) redisTemplate.opsForHash().get(tableMapKeyName,merId);
            if(isNull(merchantInfo)){
                merchantInfo=merchantInfoService.getMerchantById(merId);
                final MerchantInfo mi=merchantInfo;
                if(!isNull(merchantInfo)){
                    pool.execute(()->put(tableMapKeyName,mi,merId));
                }
            }
            return merchantInfo;
    }

    public List<MerchantInfo> getAll(){
        List<MerchantInfo> merchantInfoList=redisTemplate.opsForHash().values(tableMapKeyName)
                .stream()
                .map(obj->(MerchantInfo)obj)
                .distinct()
                .collect(Collectors.toList());
        if(!isHasElement(merchantInfoList)){
            Result<List> result = merchantInfoService.getAll();
            merchantInfoList = result.getData();
            if(isHasElement(merchantInfoList)){
                Map<Object,Object> map=new HashMap<>();
                for(MerchantInfo t:merchantInfoList){
                    map.put(t.getMerId(),t);
                }
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return merchantInfoList;
    }


}
