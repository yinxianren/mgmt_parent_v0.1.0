package com.rxh.service;

import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.vo.ResponseVO;

public interface AnewMerchantQuotaRiskService {

    ResponseVO search(MerchantQuotaRiskTable merchantId);

    ResponseVO saveOrUpdate(MerchantQuotaRiskTable merchantQuotaRiskTable);
}
