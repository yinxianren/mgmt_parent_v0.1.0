package com.rxh.pojo.risk;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SystemOrderTrack implements Serializable {

    private String id;
    //商户号
    private String merId;
    //商户订单号
    private String merOrderId;
    //平台订单号
    private String orderId;
    //订单金额
    private BigDecimal amount;
    //页面返回地址
    private String returnUrl;
    //异步通知地址
    private String noticeUrl;
    //追踪状态
    private Integer orderTrackStatus;
    //请求报文
    private String tradeInfo;
    // 请求网址
    private String refer;
    // 交易时间
    private Date tradeTime;
    // 返回结果
    private String result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId == null ? null : merId.trim();
    }

    public String getMerOrderId() {
        return merOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId == null ? null : merOrderId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl == null ? null : returnUrl.trim();
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl == null ? null : noticeUrl.trim();
    }

    public Integer getOrderTrackStatus() {
        return orderTrackStatus;
    }

    public void setOrderTrackStatus(Integer orderTrackStatus) {
        this.orderTrackStatus = orderTrackStatus;
    }

    public String getTradeInfo() {
        return tradeInfo;
    }

    public void setTradeInfo(String tradeInfo) {
        this.tradeInfo = tradeInfo == null ? null : tradeInfo.trim();
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}