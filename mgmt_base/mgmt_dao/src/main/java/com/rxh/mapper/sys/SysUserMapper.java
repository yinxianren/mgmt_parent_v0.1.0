package com.rxh.mapper.sys;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.sys.SysUser;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser, Long> {

    int updateByUserNameSelective(SysUser user);

    SysUser selectByUserName(String username);

    List<SysUser> selectAll();

    int deleteByIdList(List<Long> idList);

    List<SysUser> selectByRoleId(Long roleId);
}