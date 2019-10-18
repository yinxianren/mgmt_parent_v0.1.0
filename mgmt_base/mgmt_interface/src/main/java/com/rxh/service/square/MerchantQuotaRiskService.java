package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.square.pojo.MerchantQuotaRisk;

import java.util.List;

public interface MerchantQuotaRiskService {

    Result insert(MerchantQuotaRisk record);
    Result delete(MerchantQuotaRisk record);
    Result update(MerchantQuotaRisk record);
    Result search(String merId);
    List<MerchantQuotaRisk> getAll();


}
