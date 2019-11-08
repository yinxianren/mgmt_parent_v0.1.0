package com.rxh.anew.channel.cross.allinpay.impl;

import com.rxh.anew.channel.cross.TransOrderCrossComponent;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:31
 * Description:
 */
@Component
public class AllinPayTransOrderCrossComponentImpl implements TransOrderCrossComponent {
    @Override
    public CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }
}
