package com.rxh.service;

import com.internal.playment.common.table.system.BankRateTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewBankRateService {

    ResponseVO search(BankRateTable bankRateTable);

    ResponseVO saveOrUpdate(BankRateTable bankRateTable);

    ResponseVO removeByIds(List<String> idList);
}
