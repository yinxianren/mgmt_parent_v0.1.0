package com.rxh.service.merchant;

import com.internal.playment.common.table.merchant.MerchantWalletTable;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;

public interface AnewMerchantWalletService {

    ResponseVO search(MerchantWalletTable merchantWalletTable);

    ResponseVO pageByDetails(Page page);

}
