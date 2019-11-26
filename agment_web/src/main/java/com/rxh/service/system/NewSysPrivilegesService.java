package com.rxh.service.system;

import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.system.SysPrivilegesTable;

public interface NewSysPrivilegesService {

    ResponseVO getList(SysPrivilegesTable sysPrivilegesTable);
}
