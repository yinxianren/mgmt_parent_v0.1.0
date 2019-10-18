package com.rxh.service.impl.merchant;

import com.rxh.mapper.merchant.MerchantPrivilegesMapper;
import com.rxh.pojo.merchant.MerchantPrivileges;
import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.service.merchant.MerchantPrivilegesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantPrivilegesServiceImpl extends AbstractBaseService<MerchantPrivileges, Long> implements MerchantPrivilegesService {
    private final MerchantPrivilegesMapper merchantPrivilegesMapper;

    @Autowired
    public MerchantPrivilegesServiceImpl(MerchantPrivilegesMapper merchantPrivilegesMapper) {
        this.merchantPrivilegesMapper = merchantPrivilegesMapper;
        setMapper(merchantPrivilegesMapper);
    }
}