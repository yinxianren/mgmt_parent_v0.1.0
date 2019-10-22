package com.rxh.service.anew.merchant;


import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.merchant.MerchantInfoTable;

public interface ApiMerchantInfoService {

    MerchantInfoTable  getOne(InnerPrintLogObject ipo);

}
