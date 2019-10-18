package com.rxh.service.impl.merchant;

import com.rxh.mapper.merchant.MerchantUserMapper;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.service.merchant.MerchantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantUserServiceImpl extends AbstractBaseService<MerchantUser, Long> implements MerchantUserService {
    private final MerchantUserMapper merchantUserMapper;

    @Autowired
    public MerchantUserServiceImpl(MerchantUserMapper merchantUserMapper) {
        this.merchantUserMapper = merchantUserMapper;
        setMapper(merchantUserMapper);
    }
}