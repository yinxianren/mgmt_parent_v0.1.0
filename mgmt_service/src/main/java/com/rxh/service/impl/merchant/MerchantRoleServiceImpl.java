package com.rxh.service.impl.merchant;

import com.rxh.mapper.merchant.MerchantRoleMapper;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.service.merchant.MerchantRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantRoleServiceImpl extends AbstractBaseService<MerchantRole, Long> implements MerchantRoleService {
    private final MerchantRoleMapper merchantRoleMapper;

    @Autowired
    public MerchantRoleServiceImpl(MerchantRoleMapper merchantRoleMapper) {
        this.merchantRoleMapper = merchantRoleMapper;
        setMapper(merchantRoleMapper);
    }
}