package com.rxh.mapper.sys;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.sys.SysRole;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole, Long> {

    List<SysRole> selectAll();

    SysRole selectByName(String name);


}