package com.rxh.service.anew.business;

import com.rxh.anew.table.business.TransOrderInfoTable;

import java.util.List;

public interface ApiTransOrderInfoService {

    TransOrderInfoTable getOne(TransOrderInfoTable tit);

    List<TransOrderInfoTable> getList(TransOrderInfoTable tit);

    boolean save(TransOrderInfoTable tit);

    boolean updateById(TransOrderInfoTable tit);

}
