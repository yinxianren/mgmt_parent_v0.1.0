package com.rxh.service.system;

import com.internal.playment.common.table.system.SysPrivilegesTable;
import com.internal.playment.common.page.ResponseVO;

public interface NewSysPrivilegesService {

    ResponseVO getList(SysPrivilegesTable sysPrivilegesTable);
}
