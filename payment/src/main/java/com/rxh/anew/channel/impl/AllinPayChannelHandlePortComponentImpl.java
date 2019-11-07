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
    private  final IntoPiecesOfInformationCrossComponentImpl intoPiecesOfInformationCrossComponentImpl;
    private  final BondCardCrossComponentImpl bondCardCrossComponentImpl;
    private  final PayOrderCrossComponentImpl payOrderCrossComponentImpl;
    private  final TransOrderCrossComponentImpl transOrderCrossComponentImpl;
    private  final NoAuthenticationPayOrderCrossComponentImpl noAuthenticationPayOrderCrossComponentImpl;
    @Override
    public CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return intoPiecesOfInformationCrossComponentImpl.addCusInfo(requestCrossMsgDTO);
    }
    @Override
    public CrossResponseMsgDTO bankCardBind(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return intoPiecesOfInformationCrossComponentImpl.bankCardBind(requestCrossMsgDTO);
    }
    @Override
    public CrossResponseMsgDTO serviceFulfillment(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return intoPiecesOfInformationCrossComponentImpl.serviceFulfillment(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return bondCardCrossComponentImpl.bondCardApply(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO reGetBondCode(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return bondCardCrossComponentImpl.reGetBondCode(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO confirmBondCard(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return bondCardCrossComponentImpl.confirmBondCard(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO payApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return payOrderCrossComponentImpl.payApply(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return payOrderCrossComponentImpl.getPayCode(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return payOrderCrossComponentImpl.confirmPay(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return transOrderCrossComponentImpl.payment(requestCrossMsgDTO);
    }

    @Override
    public CrossResponseMsgDTO exemptCodePay(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo) {
        return noAuthenticationPayOrderCrossComponentImpl.exemptCodePay(requestCrossMsgDTO);
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
