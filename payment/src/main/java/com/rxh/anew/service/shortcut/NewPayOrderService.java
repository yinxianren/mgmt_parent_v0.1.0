package com.rxh.anew.service.shortcut;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonSerivceInterface;
import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.exception.NewPayException;

import java.util.Map;

public interface NewPayOrderService extends CommonSerivceInterface {
    /**
     * 支付下单申请
     * @return
     */
    Map<String, ParamRule> getParamMapByB7();

    /**
     * 查看是否重复订单
     * @param merOrderId
     * @param ipo
     */
    void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  获取商户风控信息
     * @param merchantId
     * @param ipo
     * @return
     */
    MerchantQuotaRiskTable getMerchantQuotaRiskByMerId(String merchantId, InnerPrintLogObject ipo);
}
