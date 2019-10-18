package com.rxh.service.merchant;


import com.rxh.pojo.merchant.MerchantRegisterInfo;

import java.util.List;

/**
 * com.rxh.service.merchant.MerchantAddCusService
 */
public interface MerchantAddCusService {
    List<MerchantRegisterInfo> selsectUsableByMerchId(String merchId, Integer status);
    List<MerchantRegisterInfo> getMerchantRegisterInfos( MerchantRegisterInfo merchantRegisterInfo);
    Integer insertSelective(MerchantRegisterInfo merchantRegisterInfo);
    Integer updateById(String id, Integer status, String param);




}
