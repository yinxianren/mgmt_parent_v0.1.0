package com.rxh.mapper.merchant;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.merchant.MerchantPrivileges;

import java.util.List;

public interface AgmentPrivilegesMapper extends BaseMapper<MerchantPrivileges, Long> {
    List<MerchantPrivileges> selectAll();

    List<MerchantPrivileges> selectUserPrivileges(List<Long> ids);
}