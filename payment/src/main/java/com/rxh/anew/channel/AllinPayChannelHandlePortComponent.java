package com.rxh.anew.channel;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;

public interface AllinPayChannelHandlePortComponent extends  CommonChannelHandlePortComponent{

    /**
     * 快捷免验证支付
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO exemptCodePay(RequestCrossMsgDTO requestCrossMsgDTO);

}
