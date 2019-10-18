package com.rxh.cache.ehcache;

import com.rxh.service.square.RiskQuotaDataService;
import com.rxh.square.pojo.RiskQuotaData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: hul
 * Date: 2019/5/24
 */

//@CacheConfig(cacheNames = "risk_quota_data")
@Component
public class RiskQuotaDataCache implements BaseCache<List<RiskQuotaData>, RiskQuotaData> {


    @Resource
    RiskQuotaDataService riskQuotaDataService;



    @Override
//    @Cacheable(key = "'all'")
    public List<RiskQuotaData> getCache() {
        return riskQuotaDataService.getAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<RiskQuotaData> refreshCache() {
        return riskQuotaDataService.getAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<RiskQuotaData> updateCache(RiskQuotaData riskQuotaData) {
        return getCache();
    }

//    @CacheEvict(allEntries = true)
    @Override
    public void cleanCache() {

    }

    public List<RiskQuotaData> getQuotaData(String merId, String channelId) {
        return getCache()
                .stream()
                .filter(riskQuotaData -> riskQuotaData.getRefId() == channelId || riskQuotaData.getRefId().equals(merId))
                .collect(Collectors.toList());
    }
    public List<RiskQuotaData> getChannelQuotaData(String channelId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        String month = sdf1.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        String year = sdf2.format(new Date());

        return getCache()
                .stream()
                .filter(riskQuotaData -> riskQuotaData.getRefId() .equals( channelId )&&
                        riskQuotaData.getRefType().equals(Short.valueOf("2"))
                && (riskQuotaData.getTradeTime().equals(day)
                                || riskQuotaData.getTradeTime().equals(month)
                        )
                )
                .collect(Collectors.toList());
    }


    public List<RiskQuotaData> getQuotaData1(String merId,String channelId, String day, String month, String year) {
        List<RiskQuotaData> quotaDataList  = getCache();
        if (quotaDataList == null || quotaDataList.size()<=0){
            quotaDataList = new ArrayList<>();
            return  quotaDataList;
        }

        return quotaDataList
                .stream()
                .filter(riskQuotaData -> (riskQuotaData.getRefId().equals(channelId) || riskQuotaData.getRefId().equals(merId)) &&
                        (riskQuotaData.getTradeTime().equals(day)
                                || riskQuotaData.getTradeTime().equals(month)
                        )
//                                || riskQuotaData.getType().equals(RiskQuotaData.LIMIT_TYPE_ORDER_AMOUNT)
                        // || riskQuotaData.equals(year)
                )
                .collect(Collectors.toList());
    }
}