package com.rxh.service;

import com.internal.playment.common.table.system.SysGroupTable;
import com.rxh.pojo.base.Page;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface NewSysGroupService {

    ResponseVO saveOrUpdate(SysGroupTable sysGroupTable);

    ResponseVO getList(SysGroupTable sysGroupTable);

    ResponseVO batchByIds(List<Long> ids);

    ResponseVO page(Page page);

    ResponseVO delByCodes(String codes);

}
