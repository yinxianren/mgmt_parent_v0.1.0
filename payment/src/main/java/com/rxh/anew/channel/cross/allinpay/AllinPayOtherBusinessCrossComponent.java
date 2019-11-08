package com.rxh.anew.channel.cross.allinpay;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;

import java.text.ParseException;

public interface AllinPayOtherBusinessCrossComponent {

    CrossResponseMsgDTO queryByPayOrder(RequestCrossMsgDTO requestCrossMsgDTO) throws ParseException;

    CrossResponseMsgDTO queryByTransOrder(RequestCrossMsgDTO requestCrossMsgDTO);

    CrossResponseMsgDTO unBondCard(RequestCrossMsgDTO requestCrossMsgDTO);

}
