package com.rxh.service;

import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewMerchantService {

    ResponseVO getMerchant(MerchantInfoTable merchantInfoTable);

    ResponseVO delByIds(List<String> ids);

    ResponseVO saveOrUpdate(MerchantInfoTable merchantInfoTable);
}
