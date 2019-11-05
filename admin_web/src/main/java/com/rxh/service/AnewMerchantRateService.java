package com.rxh.service;

import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewMerchantRateService {

    ResponseVO batchUpdate(List<MerchantRateTable> param);

    ResponseVO getList(MerchantRateTable m);

    ResponseVO init();
}
