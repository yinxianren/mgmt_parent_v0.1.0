package com.rxh.service.anew.merchant;


import com.rxh.anew.table.merchant.MerchantInfoTable;

import java.util.List;

public interface ApiMerchantInfoService {

    MerchantInfoTable  getOne(MerchantInfoTable merchantInfoTable);

    List<MerchantInfoTable> getMerchant(MerchantInfoTable merchantInfoTable);

    Boolean saveOrUpdate(MerchantInfoTable merchantInfoTable);

    Boolean delByIds(List<String> ids);

}
