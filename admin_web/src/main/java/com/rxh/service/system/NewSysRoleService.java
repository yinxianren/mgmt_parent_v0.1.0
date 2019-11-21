package com.rxh.service.system;

import com.internal.playment.common.table.system.SysRoleTable;
import com.internal.playment.common.page.ResponseVO;

import java.util.List;

public interface NewSysRoleService {

    ResponseVO saveOrUpdate(SysRoleTable sysRoleTable);

    ResponseVO delByids(List<String> ids);

    ResponseVO getList(SysRoleTable sysRoleTable);
}
