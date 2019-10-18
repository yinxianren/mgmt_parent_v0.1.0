package com.rxh.service.merchant;


import com.rxh.pojo.merchant.MerchantRegisterInfo;

import java.util.List;

public interface MerchantRegisterInfoService {

    int deleteByExample(MerchantRegisterInfo example);

    int deleteByMerIdAndTerminalMerId(MerchantRegisterInfo record);

    int insert(MerchantRegisterInfo record);

    int updateByMerId(MerchantRegisterInfo record);

    List<MerchantRegisterInfo> selectByExample(MerchantRegisterInfo example);

    MerchantRegisterInfo selectByMerIdAndTerminalMerId(MerchantRegisterInfo example);

    List<MerchantRegisterInfo> selecAll();
}
