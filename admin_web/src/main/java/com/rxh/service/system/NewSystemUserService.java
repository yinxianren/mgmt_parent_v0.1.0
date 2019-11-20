package com.rxh.service.system;

import com.internal.playment.common.table.system.SysUserTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface NewSystemUserService {

    ResponseVO saveOrUpdate(SysUserTable sysUserTable);
    ResponseVO getList(SysUserTable sysUserTable);
    ResponseVO delByIds(List<Long> ids);
    SysUserTable getUserAndRoleAndPrivilege(String userName);
}
