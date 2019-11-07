package com.rxh.anew.dto;

import lombok.Getter;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午2:19
 * Description:
 */
@ToString
@Getter
public class CrossResponseMsgDTO {
    private Integer crossStatusCode ;//cross返回的状态码
    private String  crossResponseMsg;//cross 方法信息
    private String  channelOrderId;//通道流水号
    private String  channelStatusCode;//通道返回的状态码
    private Date    channelResponseTime;//通道响应返回的时间
    private String  channelResponseMsg;//通道返回信息
    private String  errorCode;
    private String  errorMsg;

    public CrossResponseMsgDTO setCrossStatusCode(Integer crossStatusCode) {
        this.crossStatusCode = crossStatusCode;
        return this;
    }

    public CrossResponseMsgDTO setCrossResponseMsg(String crossResponseMsg) {
        this.crossResponseMsg = crossResponseMsg;
        return this;
    }

    public CrossResponseMsgDTO setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
        return this;
    }

    public CrossResponseMsgDTO setChannelStatusCode(String channelStatusCode) {
        this.channelStatusCode = channelStatusCode;
        return this;
    }

    public CrossResponseMsgDTO setChannelResponseTime(Date channelResponseTime) {
        this.channelResponseTime = channelResponseTime;
        return this;
    }

    public CrossResponseMsgDTO setChannelResponseMsg(String channelResponseMsg) {
        this.channelResponseMsg = channelResponseMsg;
        return this;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
