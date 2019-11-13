package com.rxh.service;

import com.internal.playment.common.table.merchant.MerchantBankRateTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewMerchantBankRateService {

    ResponseVO search(MerchantBankRateTable merchantBankRateTable);

    ResponseVO saveOrUpdate(List<MerchantBankRateTable> list);

}
