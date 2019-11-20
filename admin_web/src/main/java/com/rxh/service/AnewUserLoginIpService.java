package com.rxh.service;

import com.internal.playment.common.table.system.UserLoginIpTable;
import com.rxh.pojo.base.Page;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewUserLoginIpService {

    ResponseVO saveOrUpdate(UserLoginIpTable userLoginIpTable);

    ResponseVO delByIds(List<Long> ids);

    ResponseVO page(Page page);
}
