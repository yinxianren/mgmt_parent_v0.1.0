package com.rxh.anew.service;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.exception.NewPayException;
import com.rxh.square.pojo.MerchantSetting;

import java.util.List;

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

    /**
     *  获取商户所有通道配置信息
     * @param ipo
     * @return
     */
    List<MerchantSettingTable> getMerchantSetting(InnerPrintLogObject ipo) throws NewPayException;

}
