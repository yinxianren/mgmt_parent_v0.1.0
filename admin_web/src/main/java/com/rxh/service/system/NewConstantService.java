package com.rxh.service.system;

import com.internal.playment.common.table.system.SysConstantTable;
import com.rxh.pojo.base.Page;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface NewConstantService {

    ResponseVO saveOrUpdate(SysConstantTable sysConstantTable);

    ResponseVO delByIds(String ids);

    ResponseVO page(Page page);

    ResponseVO getList(SysConstantTable sysConstantTable);
}
