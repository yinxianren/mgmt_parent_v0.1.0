package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.exception.PayException;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.square.pojo.MerchantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public MerchantInfo getOne(String merId) throws PayException {
        try{
            MerchantInfo merchantInfo= (MerchantInfo) redisTemplate.opsForHash().get(tableMapKeyName,merId);
            if(isNull(merchantInfo)){
                merchantInfo=merchantInfoService.getMerchantById(merId);
                final MerchantInfo mi=merchantInfo;
                if(!isNull(merchantInfo)){
                    pool.execute(()->put(tableMapKeyName,mi,merId));
                }
            }
            return merchantInfo;
        }catch (Exception e){
            throw new PayException(e.getMessage());
        }
    }


}
