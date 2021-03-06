package com.rxh.service.merchant;

import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantInfoTable;

import java.util.List;

public interface AnewMerchantInfoService {

    ResponseVO getMerchants(MerchantInfoTable merchantInfoTable);

    ResponseVO delByIds(List<String> ids);

    ResponseVO saveOrUpdate(MerchantInfoTable merchantInfoTable);
}
