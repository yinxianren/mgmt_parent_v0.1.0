package com.rxh.service.impl.sys;

import com.rxh.mapper.sys.SysRoleMapper;
import com.rxh.pojo.sys.SysRole;
import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends AbstractBaseService<SysRole, Long> implements SysRoleService {
    private final SysRoleMapper sysRoleMapper;

    @Autowired
    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
        setMapper(sysRoleMapper);
    }
}