package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.square.pojo.MerchantRate;
import com.rxh.square.pojo.MerchantRateExample;

import java.util.List;

public interface MerchantSquareRateService {
    Result insert(MerchantRate record);
    Result update(List<MerchantRate> param);
    Result delete(String id);
    List<MerchantRate> search(String merId);
    List<MerchantRate> merSearch(String merId);
    List<MerchantRate> getAll();
    MerchantRate getMerchantRateByIdAndPayType(String merId,String payType);
}
