package com.rxh.anew.channel;

import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.tuple.Tuple2;

public interface CommonChannelHandlePortComponent {



    /**
     * 商户基础信息登记接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);

    /**
     *  银行卡登记接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO bankCardBind (RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);

    /**
     *  商户业务开通接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO serviceFulfillment (RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);

    /**
     * 绑卡申请接口
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);

    /**
     *  绑卡短信验证码获取
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO reGetBondCode (RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);

    /**
     *  绑卡确认
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO confirmBondCard (RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);

    /**
     * 支付下单申请
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO payApply(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);

    /**
     *  支付短信验证码获取
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO getPayCode (RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);

    /**
     * 支付交易确认
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO confirmPay (RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);

    /**
     * 付款
     * @param requestCrossMsgDTO
     * @return
     */
    CrossResponseMsgDTO payment(RequestCrossMsgDTO requestCrossMsgDTO, InnerPrintLogObject ipo);





    /**
     *  收单业务
     * @param requestCrossMsgDTO
     * @param crossResponseMsgDTO
     * @return
     */
    Tuple2  channelDifferBusinessHandleByPayOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO);

    /**
     *  代付业务
     * @param requestCrossMsgDTO
     * @param crossResponseMsgDTO
     * @return
     */
    Tuple2  channelDifferBusinessHandleByTransOrder(RequestCrossMsgDTO requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO);

}