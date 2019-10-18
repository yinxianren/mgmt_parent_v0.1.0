package com.rxh.service.impl.sys;

import com.rxh.mapper.sys.MerchantSysLogMapper;
import com.rxh.mapper.sys.SysLogMapper;
import com.rxh.pojo.sys.SysLog;
import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.service.sys.MerchantSysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantSysLogServiceImpl extends AbstractBaseService<SysLog, Long> implements MerchantSysLogService {
    private final MerchantSysLogMapper merchantSysLogMapper;

    @Autowired
    public MerchantSysLogServiceImpl(MerchantSysLogMapper merchantSysLogMapper) {
        this.merchantSysLogMapper = merchantSysLogMapper;
        setMapper(merchantSysLogMapper);
    }
}