package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.square.pojo.MerchantAcount;

public interface MerchantAcountService {

    Result insert(MerchantAcount record);
    Result update(MerchantAcount record);
    Result delete(String merId);
    Result search(String merId);
}
