package com.rxh.anew.channel.cross.yacolpay.impl;

import com.rxh.anew.channel.cross.BondCardCrossComponent;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:24
 * Description:
 */

@Component
public class YaColPayBondCardCrossComponentImpl implements BondCardCrossComponent {
    @Override
    public CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO reGetBondCode(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO confirmBondCard(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }
}
