package com.rxh.cache.ehcache;

import com.rxh.exception.PayException;
import com.rxh.service.square.MerchantSquareRateService;
import com.rxh.square.pojo.MerchantRate;
import com.rxh.utils.SystemConstant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: hul
 * Date: 2019/5/24
 */

//@CacheConfig(cacheNames = "merchant_square_rate")
@Component("merchantSquareRateCache")
public class MerchantSquareRateCache implements BaseCache<List<MerchantRate>, MerchantRate> {



    @Resource
    MerchantSquareRateService merchantSquareRateService;


    @Override
//    @Cacheable(key = "'all'")
    public List<MerchantRate> getCache() {
        return merchantSquareRateService.getAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<MerchantRate> refreshCache() {
        return merchantSquareRateService.getAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<MerchantRate> updateCache(MerchantRate merchantRate) {
        return getCache();
    }

//    @CacheEvict(allEntries = true)
    @Override
    public void cleanCache() {

    }

//    @Cacheable(key = "#merId")
    public List<MerchantRate> getByMerId(String  merId) throws PayException {
        List<MerchantRate> merchantRates =
                 getCache()
                .stream()
                .filter(merchantRate -> merchantRate.getMerId().equals(merId) && SystemConstant.SQUARE_ENABLE.equals(merchantRate.getStatus().toString()))
                .collect(Collectors.toList());
        checkMerchantRate(merchantRates);
        return  merchantRates;
    }

    /**
     *  校验商户费率
     * @param merchantRates
     */
    private void checkMerchantRate(List<MerchantRate> merchantRates) throws PayException{
        if(merchantRates == null || merchantRates.size()<=0){
            throw new PayException("商户费率未启用",2007);
        }
    }

    public MerchantRate getMerchantRateByMerIdAndPayType(String merId, String type) throws PayException {
        return
                getCache()
                .stream()//1115
                .filter(merchantRate -> merchantRate.getMerId().equals(merId) && merchantRate.getPayType().equals(type))
                .findFirst()
                .orElseThrow(() -> new PayException("商户费率不存在",2006));
    }
}