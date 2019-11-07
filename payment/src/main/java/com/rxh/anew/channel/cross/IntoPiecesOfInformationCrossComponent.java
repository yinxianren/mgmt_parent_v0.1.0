package com.rxh.anew.channel.cross;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;

public interface IntoPiecesOfInformationCrossComponent {

    /**
     * 商户基础信息登记接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO);

    /**
     *  银行卡登记接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO bankCardBind (RequestCrossMsgDTO requestCrossMsgDTO);

    /**
     *  商户业务开通接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO serviceFulfillment (RequestCrossMsgDTO requestCrossMsgDTO);
}
