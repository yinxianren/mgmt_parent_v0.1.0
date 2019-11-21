package com.rxh.service.system;

import com.internal.playment.common.table.system.SysLogTable;
import com.internal.playment.common.page.Page;
import com.rxh.vo.ResponseVO;

public interface NewSystemLogService {

    ResponseVO saveOrUpdate(SysLogTable sysLogTable);

    ResponseVO page(Page page);
}
