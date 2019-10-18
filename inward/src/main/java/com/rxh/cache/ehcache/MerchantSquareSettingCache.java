package com.rxh.cache.ehcache;

import com.rxh.exception.PayException;
import com.rxh.service.square.MerchantSquareSettingService;
import com.rxh.square.pojo.MerchantSetting;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hul
 * Date: 2019/5/24
 */

//@CacheConfig(cacheNames = "merchant_square_setting")
@Component("merchantSquareSettingCache")
public class MerchantSquareSettingCache implements BaseCache<List<MerchantSetting>, MerchantSetting> {



    @Resource
    MerchantSquareSettingService merchantSquareSettingService;


    @Override
//    @Cacheable(key = "'all'")
    public List<MerchantSetting> getCache() {
        return merchantSquareSettingService.getAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<MerchantSetting> refreshCache() {
        return merchantSquareSettingService.getAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<MerchantSetting> updateCache(MerchantSetting merchantSetting) {
        return getCache();
    }

//    @CacheEvict(allEntries = true)
    @Override
    public void cleanCache() {

    }

//    @Cacheable(key = "#merId")
    public MerchantSetting getMerchantSettingByMerId(String  merId) throws PayException {
        return getCache()
                .stream()
                .filter(merchantSetting -> merchantSetting.getMerId().equals(merId))
                .findFirst()
                .orElseThrow(() -> new PayException("商户配置不存在",2004));
    }
}