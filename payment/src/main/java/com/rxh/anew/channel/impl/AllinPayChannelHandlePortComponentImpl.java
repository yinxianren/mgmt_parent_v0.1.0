package com.rxh.anew.channel.impl;

import com.rxh.anew.channel.AllinPayChannelHandlePortComponent;
import com.rxh.anew.channel.cross.allinpay.impl.*;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
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
public class AllinPayChannelHandlePortComponentImpl implements AllinPayChannelHandlePortComponent{

    private  final PayMessageSend payMessageSend;
    private  final AllinPayIntoPiecesOfInformationCrossComponentImpl allinPayIntoPiecesOfInformationCrossComponentImpl;
    private  final AllinPayBondCardCrossComponentImpl allinPayBondCardCrossComponentImpl;
    private  final AllinPayPayOrderCrossComponentImpl allinPayPayOrderCrossComponentImpl;
    private  final AllinPayTransOrderCrossComponentImpl allinPayTransOrderCrossComponentImpl;
    private  final AllinPayNoAuthenticationPayOrderCrossComponentImpl allinPayNoAuthenticationPayOrderCrossComponentImpl;
    @Override
    public CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayIntoPiecesOfInformationCrossComponentImpl.addCusInfo(requestCrossMsgDTO);
    }
    @Override
    public CrossResponseMsgDTO bankCardBind(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayIntoPiecesOfInformationCrossComponentImpl.bankCardBind(requestCrossMsgDTO);
    }
    @Override
    public CrossResponseMsgDTO serviceFulfillment(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayIntoPiecesOfInformationCrossComponentImpl.serviceFulfillment(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayBondCardCrossComponentImpl.bondCardApply(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO reGetBondCode(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayBondCardCrossComponentImpl.reGetBondCode(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO confirmBondCard(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayBondCardCrossComponentImpl.confirmBondCard(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO payApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayPayOrderCrossComponentImpl.payApply(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayPayOrderCrossComponentImpl.getPayCode(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayPayOrderCrossComponentImpl.confirmPay(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayTransOrderCrossComponentImpl.payment(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO exemptCodePay(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return allinPayNoAuthenticationPayOrderCrossComponentImpl.exemptCodePay(requestCrossMsgDTO);
    }

    public Tuple2 channelDifferBusinessHandleByPayOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO) {
        PayOrderInfoTable payOrderInfoTable = requestCrossMsgDTO.getPayOrderInfoTable();
        if(crossResponseMsgDTO.getCrossStatusCode() == StatusEnum._0.getStatus())
            payMessageSend.sendObjectMessageToPayOderMsgMQ(payOrderInfoTable);
        return null;
    }

    public Tuple2 channelDifferBusinessHandleByTransOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO) {
        TransOrderInfoTable transOrderInfoTable = requestCrossMsgDTO.getTransOrderInfoTable();
        if(crossResponseMsgDTO.getCrossStatusCode() == StatusEnum._0.getStatus())
            payMessageSend.sendObjectMessageToTransOderMsgMQ(transOrderInfoTable);
        return null;
    }



}
