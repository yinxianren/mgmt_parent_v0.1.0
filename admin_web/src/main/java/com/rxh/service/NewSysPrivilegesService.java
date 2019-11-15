package com.rxh.service;

import com.internal.playment.common.table.system.SysPrivilegesTable;
import com.rxh.vo.ResponseVO;

public interface NewSysPrivilegesService {

    ResponseVO getList(SysPrivilegesTable sysPrivilegesTable);
}
