package com.rxh.service.merchant;

import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantAcountTable;

public interface NewMerchantAcountService {

    ResponseVO getOne(MerchantAcountTable merchantAcountTable);

    ResponseVO savaOrUpdate(MerchantAcountTable merchantAcountTable);
}
