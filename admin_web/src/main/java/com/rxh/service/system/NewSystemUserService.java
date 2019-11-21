package com.rxh.service.system;

import com.internal.playment.common.table.system.SysPrivilegesTable;
import com.internal.playment.common.table.system.SysUserTable;
import com.internal.playment.common.page.ResponseVO;

import java.util.List;

public interface NewSystemUserService {

    ResponseVO saveOrUpdate(SysUserTable sysUserTable);
    ResponseVO getList(SysUserTable sysUserTable);
    ResponseVO delByIds(List<Long> ids);
    SysUserTable getUserAndRoleAndPrivilege(String userName);
    List<SysPrivilegesTable> getMenu(String userName);

}
