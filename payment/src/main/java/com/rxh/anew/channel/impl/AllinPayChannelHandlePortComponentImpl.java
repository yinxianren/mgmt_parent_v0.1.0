package com.rxh.anew.channel.impl;

import com.rxh.anew.channel.AllinPayChannelHandlePortComponent;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.mq.PayMessageSend;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.TransOrderInfoTable;
import com.rxh.enums.StatusEnum;
import com.rxh.tuple.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/1
 * Time: 下午5:09
 * Description:
 */

@AllArgsConstructor
@Component
public class AllinPayChannelHandlePortComponentImpl implements AllinPayChannelHandlePortComponent {

    private  final PayMessageSend payMessageSend;


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

    @Override
    public CrossResponseMsgDTO payApply(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO exemptCodePay(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }


    @Override
    public Tuple2 channelDifferBusinessHandleByPayOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO) {

        PayOrderInfoTable payOrderInfoTable = requestCrossMsgDTO.getPayOrderInfoTable();
        if(crossResponseMsgDTO.getCrossStatusCode() == StatusEnum._0.getStatus())
            payMessageSend.sendObjectMessageToPayOderMsgMQ(payOrderInfoTable);


        return null;
    }

    @Override
    public Tuple2 channelDifferBusinessHandleByTransOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO) {
        TransOrderInfoTable transOrderInfoTable = requestCrossMsgDTO.getTransOrderInfoTable();
        if(crossResponseMsgDTO.getCrossStatusCode() == StatusEnum._0.getStatus())
            payMessageSend.sendObjectMessageToTransOderMsgMQ(transOrderInfoTable);
        return null;
    }


}
