package com.rxh.anew.channel.cross.yacolpay.impl;

import com.rxh.anew.channel.cross.IntoPiecesOfInformationCrossComponent;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:21
 * Description:
 *
 *
 */
@Component
public class IntoPiecesOfInformationCrossComponentImpl implements IntoPiecesOfInformationCrossComponent {
    @Override
    public CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO bankCardBind(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO serviceFulfillment(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }
}
