package com.rxh.anew.service.shortcut;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonSerivceInterface;

import java.util.Map;

public interface NewPayPaymentBondCardService extends CommonSerivceInterface {
    /**
     *  绑卡申请接口
     * @return
     */
    Map<String, ParamRule> getParamMapByBC();

    /**
     *  判断订单是否重复
     * @param merOrderId
     * @param ipo
     */
    void multipleOrder(String merOrderId, InnerPrintLogObject ipo);
}
