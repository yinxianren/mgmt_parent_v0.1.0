package com.rxh.service.merchant;

import com.internal.playment.common.table.merchant.MerchantAcountTable;
import com.rxh.vo.ResponseVO;

public interface NewMerchantAcountService {

    ResponseVO getOne(MerchantAcountTable merchantAcountTable);

    ResponseVO savaOrUpdate(MerchantAcountTable merchantAcountTable);
}
