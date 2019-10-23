package com.rxh.anew.dto;

import com.rxh.anew.table.business.*;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
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

    private RegisterCollectTable registerCollectTable;
    private RegisterInfoTable registerInfoTable;
    private MerchantCardTable merchantCardTable;
    private PayOrderInfoTable payOrderInfoTable;
    private TransOrderInfoTable transOrderInfoTable;
    private ChannelInfoTable channelInfoTable;
    private ChannelExtraInfoTable channelExtraInfoTable;

    public RequestCrossMsgDTO setRegisterCollectTable(RegisterCollectTable registerCollectTable) {
        this.registerCollectTable = registerCollectTable;
        return this;
    }

    public RequestCrossMsgDTO setRegisterInfoTable(RegisterInfoTable registerInfoTable) {
        this.registerInfoTable = registerInfoTable;
        return this;
    }

    public RequestCrossMsgDTO setMerchantCardTable(MerchantCardTable merchantCardTable) {
        this.merchantCardTable = merchantCardTable;
        return this;
    }

    public RequestCrossMsgDTO setPayOrderInfoTable(PayOrderInfoTable payOrderInfoTable) {
        this.payOrderInfoTable = payOrderInfoTable;
        return this;
    }

    public RequestCrossMsgDTO setTransOrderInfoTable(TransOrderInfoTable transOrderInfoTable) {
        this.transOrderInfoTable = transOrderInfoTable;
        return this;
    }

    public RequestCrossMsgDTO setChannelInfoTable(ChannelInfoTable channelInfoTable) {
        this.channelInfoTable = channelInfoTable;
        return this;
    }

    public RequestCrossMsgDTO setChannelExtraInfoTable(ChannelExtraInfoTable channelExtraInfoTable) {
        this.channelExtraInfoTable = channelExtraInfoTable;
        return this;
    }
}
