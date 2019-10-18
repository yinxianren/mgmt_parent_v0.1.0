package com.rxh.service.merchant;

import com.rxh.square.pojo.MerchantSetting;

public interface MerchantSettingService {

    MerchantSetting getMerchantSettingByMerId(String merId);

}