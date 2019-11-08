package com.rxh.anew.channel.cross.sicpay;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;

public interface SicPayOtherBusinessCrossComponent {

    CrossResponseMsgDTO queryBondCard(RequestCrossMsgDTO requestCrossMsgDTO) throws Exception;

}
