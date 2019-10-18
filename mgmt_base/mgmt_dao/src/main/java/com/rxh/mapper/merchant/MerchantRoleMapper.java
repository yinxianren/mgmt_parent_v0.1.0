package com.rxh.mapper.merchant;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.merchant.MerchantRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantRoleMapper extends BaseMapper<MerchantRole, Long> {
    List<MerchantRole> selectByMerchantId(String merchantId);

    List<MerchantRole> selectByMerchantRole(MerchantRole merchantRole);

    int updateRoleByRoleNameAndBelongTo(MerchantRole merchantRole);

    int deleteRoleByRoleNameAndBelongTo(@Param("roleName") String roleName, @Param("belongTo") String belongTo);
}