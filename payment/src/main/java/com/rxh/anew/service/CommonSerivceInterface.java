package com.rxh.anew.service;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.exception.NewPayException;

public interface CommonSerivceInterface {

    MerchantInfoTable  getOne(InnerPrintLogObject ipo) throws NewPayException;

}
