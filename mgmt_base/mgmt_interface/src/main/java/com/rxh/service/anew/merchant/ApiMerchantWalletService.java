package com.rxh.service.anew.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;

import java.util.List;

public interface ApiMerchantWalletService {

    MerchantWalletTable getOne(MerchantWalletTable mwt);

    boolean updateOrSave(MerchantWalletTable mwt);

    List<MerchantWalletTable> getList(MerchantWalletTable merchantWalletTable);

    IPage page(MerchantsDetailsTable merchantsDetailsTable);
}
