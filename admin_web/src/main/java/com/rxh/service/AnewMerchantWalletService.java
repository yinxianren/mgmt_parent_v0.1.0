package com.rxh.service;

import com.internal.playment.common.table.merchant.MerchantWalletTable;
import com.rxh.pojo.base.Page;
import com.rxh.vo.ResponseVO;

public interface AnewMerchantWalletService {

    ResponseVO search(MerchantWalletTable merchantWalletTable);

    ResponseVO pageByDetails(Page page);

}
