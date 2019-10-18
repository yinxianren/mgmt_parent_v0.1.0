package com.rxh.square.pojo;

import java.io.Serializable;
import java.util.List;

public class BatchRepayInfo implements Serializable {
    //批次号
    private String batchNo;
    //交易列表
    private List<RepayInfo> detailList;
    //扩展信息
    private String extend_param;
    //通道信息
    private  ChannelInfo channelInfo ;

    public ChannelInfo getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(ChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public List<RepayInfo> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<RepayInfo> detailList) {
        this.detailList = detailList;
    }

    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }

}
