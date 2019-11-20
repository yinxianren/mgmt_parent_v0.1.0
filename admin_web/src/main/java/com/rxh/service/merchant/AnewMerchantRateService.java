package com.rxh.service.merchant;

import com.internal.playment.common.table.merchant.MerchantRateTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewMerchantRateService {

    ResponseVO batchUpdate(List<MerchantRateTable> param);

    ResponseVO getList(MerchantRateTable m);

    ResponseVO init();
}
