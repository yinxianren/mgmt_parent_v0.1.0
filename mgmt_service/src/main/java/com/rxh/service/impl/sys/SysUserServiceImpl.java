package com.rxh.service.impl.sys;

import com.rxh.mapper.sys.SysUserMapper;
import com.rxh.pojo.sys.SysUser;
import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends AbstractBaseService<SysUser, Long> implements SysUserService {
    private final SysUserMapper sysUserMapper;

    @Autowired
    public SysUserServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
        setMapper(sysUserMapper);
    }
}