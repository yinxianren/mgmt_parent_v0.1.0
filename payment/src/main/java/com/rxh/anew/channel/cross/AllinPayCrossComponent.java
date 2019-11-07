package com.rxh.anew.channel.cross;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;

public interface AllinPayCrossComponent {

    /**
     *
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO);



}
