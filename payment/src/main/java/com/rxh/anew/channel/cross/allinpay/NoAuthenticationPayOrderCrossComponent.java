package com.rxh.anew.channel.cross.allinpay;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;

public interface NoAuthenticationPayOrderCrossComponent {
    /**
     * 快捷免验证支付
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO);
}