package com.rxh.anew.dto;

import com.rxh.anew.dto.nest.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 上午9:11
 * Description:
 */
@Getter
public class RequestCrossMsgDTO {

    private RegisterMsgNest registerMsgNest;
    private BondCardMsgNest bondCardMsgNest;
    private PayOrderMsgNest payOrderMsgNest;
    private TransOrderMsgNest transOrderMsgNest;
    private ChannelMsgNest channelMsgNest;

    public RequestCrossMsgDTO setChannelMsgNest(ChannelMsgNest channelMsgNest) {
        this.channelMsgNest = channelMsgNest;
        return this;
    }

    public RequestCrossMsgDTO setRegisterMsgNest(RegisterMsgNest registerMsgNest) {
        this.registerMsgNest = registerMsgNest;
        return this;
    }

    public RequestCrossMsgDTO setBondCardMsgNest(BondCardMsgNest bondCardMsgNest) {
        this.bondCardMsgNest = bondCardMsgNest;
        return this;
    }

    public RequestCrossMsgDTO setPayOrderMsgNest(PayOrderMsgNest payOrderMsgNest) {
        this.payOrderMsgNest = payOrderMsgNest;
        return this;
    }

    public RequestCrossMsgDTO setTransOrderMsgNest(TransOrderMsgNest transOrderMsgNest) {
        this.transOrderMsgNest = transOrderMsgNest;
        return this;
    }
}
