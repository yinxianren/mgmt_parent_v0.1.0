package com.rxh.service.merchant;

import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantBankRateTable;

import java.util.List;

public interface AnewMerchantBankRateService {

    ResponseVO search(MerchantBankRateTable merchantBankRateTable);

    ResponseVO saveOrUpdate(List<MerchantBankRateTable> list);

}
