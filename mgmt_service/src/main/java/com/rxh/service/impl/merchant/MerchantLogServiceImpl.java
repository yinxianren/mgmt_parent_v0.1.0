package com.rxh.service.impl.merchant;

import com.rxh.mapper.merchant.MerchantLogMapper;
import com.rxh.pojo.Result;
import com.rxh.pojo.merchant.MerchantLog;
import com.rxh.pojo.merchant.MerchantLogWithBLOBs;
import com.rxh.service.impl.base.AbstractBaseWithBLOBsService;
import com.rxh.service.merchant.MerchantLogService;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantLogServiceImpl extends AbstractBaseWithBLOBsService<MerchantLog, MerchantLogWithBLOBs, Long> implements MerchantLogService {
    private final MerchantLogMapper merchantLogMapper;

    @Autowired
    public MerchantLogServiceImpl(MerchantLogMapper merchantLogMapper) {
        this.merchantLogMapper = merchantLogMapper;
        setMapper(merchantLogMapper);
    }

    @Override
    public Result<MerchantLogWithBLOBs> insert(MerchantLogWithBLOBs record) {
        record.setId(UUID.createKey("merchant_log"));
        return super.insert(record);
    }

    @Override
    public Result<MerchantLogWithBLOBs> insertSelective(MerchantLogWithBLOBs record) {
        record.setId(UUID.createKey("merchant_log"));
        return super.insertSelective(record);
    }
}