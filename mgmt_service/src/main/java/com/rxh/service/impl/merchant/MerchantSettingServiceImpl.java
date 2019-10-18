package com.rxh.service.impl.merchant;

import com.rxh.mapper.square.MerchantSquareSettingMapper;
import com.rxh.service.merchant.MerchantSettingService;
import com.rxh.square.pojo.MerchantSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantSettingServiceImpl implements MerchantSettingService {

    @Autowired
    private MerchantSquareSettingMapper  merchantSquareSettingMapper;

    @Override
    public MerchantSetting getMerchantSettingByMerId(String merId) {
        return merchantSquareSettingMapper.selectByMerId(merId);
    }
}
