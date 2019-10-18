package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.square.pojo.MerchantSetting;

import java.util.List;

public interface MerchantSquareSettingService {

    Result insert(MerchantSetting record);
    Result delete (String id);
    Result update(MerchantSetting record);
    Result search(String merId);
    Result merSearch(String merId);
    List<MerchantSetting> getAll();
}
