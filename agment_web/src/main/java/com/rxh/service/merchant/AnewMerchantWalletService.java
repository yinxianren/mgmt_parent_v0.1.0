package com.rxh.service.merchant;

import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantWalletTable;

public interface AnewMerchantWalletService {

    ResponseVO search(MerchantWalletTable merchantWalletTable);

    ResponseVO pageByDetails(Page page);

}
