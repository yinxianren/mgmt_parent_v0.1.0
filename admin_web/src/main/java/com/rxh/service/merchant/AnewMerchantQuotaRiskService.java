package com.rxh.service.merchant;

import com.internal.playment.common.table.merchant.MerchantQuotaRiskTable;
import com.internal.playment.common.page.ResponseVO;

public interface AnewMerchantQuotaRiskService {

    ResponseVO search(MerchantQuotaRiskTable merchantId);

    ResponseVO saveOrUpdate(MerchantQuotaRiskTable merchantQuotaRiskTable);
}
