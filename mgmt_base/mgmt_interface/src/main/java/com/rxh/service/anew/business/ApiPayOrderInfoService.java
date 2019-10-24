package com.rxh.service.anew.business;

import com.rxh.anew.table.business.PayOrderInfoTable;

import java.util.List;

public interface ApiPayOrderInfoService {

    PayOrderInfoTable getOne(PayOrderInfoTable pit);

    List<PayOrderInfoTable> getList(PayOrderInfoTable pit);

    boolean save(PayOrderInfoTable pit);

    boolean updateByPrimaryKey(PayOrderInfoTable pit);
}
