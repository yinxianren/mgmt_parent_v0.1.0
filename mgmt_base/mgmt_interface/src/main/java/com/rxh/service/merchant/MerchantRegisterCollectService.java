package com.rxh.service.merchant;


import com.rxh.square.pojo.MerchantRegisterCollect;

import java.util.List;

public interface MerchantRegisterCollectService {

    int insert(MerchantRegisterCollect record);

    List<MerchantRegisterCollect> selectByExample(MerchantRegisterCollect merchantRegisterCollect);

    List<MerchantRegisterCollect> selectByMeridAndterminalMerIdAndStatus(MerchantRegisterCollect merchantRegisterCollect);

    List<MerchantRegisterCollect> selectByMerOrderIdAndMerId(MerchantRegisterCollect merchantRegisterCollect);

    int updateByMerOrderIdAndMerId(MerchantRegisterCollect record);

    List<MerchantRegisterCollect> selectByWhereCondition(MerchantRegisterCollect record);

}
