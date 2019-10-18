package com.rxh.service.impl.sys;

import com.rxh.mapper.sys.SysLogMapper;
import com.rxh.pojo.sys.SysLog;
import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.service.sys.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl extends AbstractBaseService<SysLog, Long> implements SysLogService {
    private final SysLogMapper sysLogMapper;

    @Autowired
    public SysLogServiceImpl(SysLogMapper sysLogMapper) {
        this.sysLogMapper = sysLogMapper;
        setMapper(sysLogMapper);
    }
}