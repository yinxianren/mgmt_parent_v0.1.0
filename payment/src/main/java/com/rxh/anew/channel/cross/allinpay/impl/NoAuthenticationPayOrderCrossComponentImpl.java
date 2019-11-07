package com.rxh.anew.channel.cross.allinpay.impl;

import com.rxh.anew.channel.cross.allinpay.NoAuthenticationPayOrderCrossComponent;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:40
 * Description:
 */
@Component
public class NoAuthenticationPayOrderCrossComponentImpl implements NoAuthenticationPayOrderCrossComponent {
    @Override
    public CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }
}
