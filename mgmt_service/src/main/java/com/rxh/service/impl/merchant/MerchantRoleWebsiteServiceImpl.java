package com.rxh.service.impl.merchant;

import com.rxh.mapper.merchant.MerchantRoleWebsiteMapper;
import com.rxh.pojo.merchant.MerchantRoleWebsite;
import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.service.merchant.MerchantRoleWebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantRoleWebsiteServiceImpl extends AbstractBaseService<MerchantRoleWebsite, Long> implements MerchantRoleWebsiteService {
    private final MerchantRoleWebsiteMapper merchantRoleWebsiteMapper;

    @Autowired
    public MerchantRoleWebsiteServiceImpl(MerchantRoleWebsiteMapper merchantRoleWebsiteMapper) {
        this.merchantRoleWebsiteMapper = merchantRoleWebsiteMapper;
        setMapper(merchantRoleWebsiteMapper);
    }
}