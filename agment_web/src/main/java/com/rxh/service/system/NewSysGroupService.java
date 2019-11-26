package com.rxh.service.system;

import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.system.SysGroupTable;

public interface NewSysGroupService {

    ResponseVO saveOrUpdate(SysGroupTable sysGroupTable);

    ResponseVO getList(SysGroupTable sysGroupTable);

    ResponseVO batchByIds(String ids);

    ResponseVO page(Page page);

    ResponseVO delByCodes(String codes);

}
