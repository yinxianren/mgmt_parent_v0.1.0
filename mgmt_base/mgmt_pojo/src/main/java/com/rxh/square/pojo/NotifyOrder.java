package com.rxh.square.pojo;

import java.io.Serializable;
import java.util.Date;

public class NotifyOrder implements Serializable {

    private String notifyId;//id
    private String merId;//商户号
    private String merOrderId;//商户订单号
    private String originalOrderId;//平台订单号
    private String notifyResult;//通知参数
    private Integer notifyNum;//通知次数
    private String notifyStatus;//通知状态
    private Date notifyTime;//通知时间
    private String remark;//备注
    private String notifyUrl;//通知地址
    private String ip;

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getMerOrderId() {
        return merOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
    }

    public String getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(String originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    public String getNotifyResult() {
        return notifyResult;
    }

    public void setNotifyResult(String notifyResult) {
        this.notifyResult = notifyResult;
    }

    public Integer getNotifyNum() {
        return notifyNum;
    }

    public void setNotifyNum(Integer notifyNum) {
        this.notifyNum = notifyNum;
    }

    public String getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "NotifyOrder{" +
                "notifyId='" + notifyId + '\'' +
                ", merId='" + merId + '\'' +
                ", merOrderId='" + merOrderId + '\'' +
                ", originalOrderId='" + originalOrderId + '\'' +
                ", notifyResult='" + notifyResult + '\'' +
                ", notifyNum=" + notifyNum +
                ", notifyStatus='" + notifyStatus + '\'' +
                ", notifyTime=" + notifyTime +
                ", remark='" + remark + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
