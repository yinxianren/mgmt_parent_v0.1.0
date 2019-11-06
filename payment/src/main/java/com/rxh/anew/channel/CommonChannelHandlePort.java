package com.rxh.anew.channel;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.tuple.Tuple2;

public interface CommonChannelHandlePort {
    /**
     *  收单业务
     * @param requestCrossMsgDTO
     * @param crossResponseMsgDTO
     * @return
     */
    Tuple2  channelDifferBusinessHandleByPayOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO);

    /**
     *  代付业务
     * @param requestCrossMsgDTO
     * @param crossResponseMsgDTO
     * @return
     */
    Tuple2  channelDifferBusinessHandleByTransOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO);

}
