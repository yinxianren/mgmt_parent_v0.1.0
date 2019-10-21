package com.rxh.service.impl.sys;

import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.mapper.sys.SysConstantMapper;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.sys.SysConstantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConstantServiceImpl extends AbstractBaseService<SysConstant,String> implements SysConstantService {
    private final SysConstantMapper sysConstantMapper;

    @Autowired
    public SysConstantServiceImpl(SysConstantMapper sysConstantMapper) {
        this.sysConstantMapper = sysConstantMapper;
        setMapper(sysConstantMapper);
    }


    @Override
    public SysConstant getOneByFirstValueAndCode(String firstValue, String groupCode) {
        return sysConstantMapper.getOneByFirstValueAndCode(firstValue,groupCode);
    }
}