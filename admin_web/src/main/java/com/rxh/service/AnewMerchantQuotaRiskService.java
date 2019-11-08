package com.rxh.service;

import com.internal.playment.common.table.merchant.MerchantQuotaRiskTable;
import com.rxh.vo.ResponseVO;

public interface AnewMerchantQuotaRiskService {

    ResponseVO search(MerchantQuotaRiskTable merchantId);

    ResponseVO saveOrUpdate(MerchantQuotaRiskTable merchantQuotaRiskTable);
}
