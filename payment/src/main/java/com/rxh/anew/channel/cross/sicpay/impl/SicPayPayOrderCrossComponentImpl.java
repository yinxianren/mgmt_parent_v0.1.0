package com.rxh.anew.channel.cross.sicpay.impl;

import com.rxh.anew.channel.cross.PayOrderCrossComponent;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:28
 * Description:
 */
@Component
public class SicPayPayOrderCrossComponentImpl implements PayOrderCrossComponent {
    @Override
    public CrossResponseMsgDTO payApply(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }
}
