package com.rxh.anew.channel.cross;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;

public interface BondCardCrossComponent {
    /**
     * 绑卡申请接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO requestCrossMsgDTO);

    /**
     *  绑卡短信验证码获取
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO reGetBondCode (RequestCrossMsgDTO requestCrossMsgDTO);

    /**
     *  绑卡确认
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO confirmBondCard (RequestCrossMsgDTO requestCrossMsgDTO);
}