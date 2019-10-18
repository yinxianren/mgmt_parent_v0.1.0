package com.rxh.mapper.merchant;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.merchant.MerchantInfo;

import java.util.List;

public interface MerchantInfoMapper extends BaseMapper<MerchantInfo, Integer> {

    List<MerchantInfo> selectAll();

    List<MerchantInfo> selectAllIdAndName();

}