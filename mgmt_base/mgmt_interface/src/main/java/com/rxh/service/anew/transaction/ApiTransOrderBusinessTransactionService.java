package com.rxh.service.anew.transaction;

import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.TransOrderInfoTable;

import java.util.List;

public interface ApiTransOrderBusinessTransactionService {

    void updateByPayOrderCorrelationInfo(TransOrderInfoTable transOrderInfoTable, List<PayOrderInfoTable> payOrderInfoTableList);

}
