package com.rxh.service.anew.merchant;

import com.rxh.anew.table.merchant.MerchantWalletTable;

public interface ApiMerchantWalletService {

    MerchantWalletTable getOne(MerchantWalletTable mwt);

    boolean updateOrSave(MerchantWalletTable mwt);
}
