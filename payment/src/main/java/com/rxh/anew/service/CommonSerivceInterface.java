package com.rxh.anew.service;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.exception.NewPayException;

public interface CommonSerivceInterface {
    /**
     *  获取商户信息
     * @param ipo
     * @return
     * @throws NewPayException
     */
    MerchantInfoTable  getOneMerInfo(InnerPrintLogObject ipo) throws NewPayException;

    /**
     *  判断多重订单
     * @return
     */
    boolean multipleOrder(String merOrderId,InnerPrintLogObject ipo) throws NewPayException;
}
