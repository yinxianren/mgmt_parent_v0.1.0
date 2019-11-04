package com.rxh.anew.channel;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.tuple.Tuple2;

public interface CommonChannelHandlePort {

    Tuple2  channelDifferBusinessHandle(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO);
}
