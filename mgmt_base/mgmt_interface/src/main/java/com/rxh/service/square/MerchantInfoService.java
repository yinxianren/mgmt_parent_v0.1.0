package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.square.pojo.MerchantInfo;

import java.util.List;

public interface MerchantInfoService {
    Result insert(MerchantInfo record,String username);
    Result deleteByPrimaryKey(String[] array);
    Result update(MerchantInfo record, String name);
    Result  search(MerchantInfo merchantInfo);
    Result  getAll();
    List<MerchantInfo> getIdsAndName();
    List<MerchantInfo> getAllmerId();
    MerchantInfo getMerchantById(String merchantId);
    Result getAllByAgentMerchantId(String agentMerchantId);
    Result getAllByMerchantId(String merchantId);
    MerchantInfo selectByMerId(String merId);
    Result getMerchants(String merchantId);
}
