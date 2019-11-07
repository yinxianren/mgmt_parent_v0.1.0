package com.rxh.anew.channel.cross;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;

public interface PayOrderCrossComponent {
    /**
     * 支付下单申请
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO payApply(RequestCrossMsgDTO requestCrossMsgDTO);

    /**
     *  支付短信验证码获取
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO getPayCode (RequestCrossMsgDTO requestCrossMsgDTO);

    /**
     * 支付交易确认
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO confirmPay (RequestCrossMsgDTO requestCrossMsgDTO);
}