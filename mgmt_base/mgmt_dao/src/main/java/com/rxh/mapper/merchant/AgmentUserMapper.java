package com.rxh.mapper.merchant;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.merchant.MerchantUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgmentUserMapper extends BaseMapper<MerchantUser, Long> {
    int deleteByIdList(List<Long> idList);

    List<MerchantUser> selectMerchantUserByMerchantId(String merchantId);

    List<MerchantUser> selectByMerchantUser(MerchantUser merchantUser);

    int deleteByBelongTo(String merchantId);

    int deleteByUserNameAndBelongTo(@Param("userName") String userName, @Param("belongTo") String belongTo);

    MerchantUser selectByUserNameAndBelongTo(@Param("userName") String userName, @Param("belongTo") String belongTo);

    int updateMerchantUserByUserNameAndBelongTo(MerchantUser merchantUser);
}