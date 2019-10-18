package com.rxh.mapper.sys;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.sys.SysPrivileges;

import java.util.List;

public interface SysPrivilegesMapper extends BaseMapper<SysPrivileges, Long> {

    List<SysPrivileges> selectAll();

    List<SysPrivileges> selectUserPrivileges(List<Long> ids);
}