package com.rxh.service.merchant;

import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.service.base.BaseService;

public interface MerchantRoleService extends BaseService<MerchantRole, Long> {

    int save(MerchantRole role);

}