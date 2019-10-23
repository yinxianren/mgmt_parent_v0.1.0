package com.rxh.anew.dto.nest;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 上午9:22
 * Description:
 */
@Getter
public class ChannelMsgNest {

    private String channelParam;//通道配置参数
    private String extraChannelParam;//附属通道配置信息

    public ChannelMsgNest setChannelParam(String channelParam) {
        this.channelParam = channelParam;
        return this;
    }

    public ChannelMsgNest setExtraChannelParam(String extraChannelParam) {
        this.extraChannelParam = extraChannelParam;
        return this;
    }
}
