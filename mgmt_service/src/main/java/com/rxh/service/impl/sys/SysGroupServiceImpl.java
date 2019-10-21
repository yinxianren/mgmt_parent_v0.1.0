package com.rxh.service.impl.sys;

import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.mapper.sys.SysGroupMapper;
import com.rxh.pojo.sys.SysGroup;
import com.rxh.service.sys.SysGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysGroupServiceImpl extends AbstractBaseService<SysGroup,String> implements SysGroupService {
    private final SysGroupMapper sysGroupMapper;

    @Autowired
    public SysGroupServiceImpl(SysGroupMapper sysGroupMapper) {
        this.sysGroupMapper = sysGroupMapper;
        setMapper(sysGroupMapper);
    }
}