package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ChannelInfo implements Serializable {
    private String channelId;

    private  List<String> channelIds;//查询使用

    private String organizationId;

    private List<String> organizationIds;//查询使用

    private String channelName;

    private Integer channelLevel;

    private Integer type;

    private String channelTransCode;

    private String payUrl;

    private String others;

    private BigDecimal channelSingleFee;

    private BigDecimal channelRateFee;

    private String settleCycle;

    private BigDecimal singleMinAmount;

    private BigDecimal singleMaxAmount;

    private BigDecimal dayQuotaAmount;

    private BigDecimal monthQuotaAmount;

    private String outChannelId;



    private Integer status;

    private Date createTime;

    public List<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<String> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public List<String> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(List<String> channelIds) {
        this.channelIds = channelIds;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId == null ? null : organizationId.trim();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName == null ? null : channelName.trim();
    }

    public Integer getChannelLevel() {
        return channelLevel;
    }

    public void setChannelLevel(Integer channelLevel) {
        this.channelLevel = channelLevel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getChannelTransCode() {
        return channelTransCode;
    }

    public void setChannelTransCode(String channelTransCode) {
        this.channelTransCode = channelTransCode == null ? null : channelTransCode.trim();
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl == null ? null : payUrl.trim();
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others == null ? null : others.trim();
    }

    public BigDecimal getChannelSingleFee() {
        return channelSingleFee;
    }

    public void setChannelSingleFee(BigDecimal channelSingleFee) {
        this.channelSingleFee = channelSingleFee;
    }

    public BigDecimal getChannelRateFee() {
        return channelRateFee;
    }

    public void setChannelRateFee(BigDecimal channelRateFee) {
        this.channelRateFee = channelRateFee;
    }

    public String getSettleCycle() {
        return settleCycle;
    }

    public void setSettleCycle(String settleCycle) {
        this.settleCycle = settleCycle == null ? null : settleCycle.trim();
    }

    public BigDecimal getSingleMinAmount() {
        return singleMinAmount;
    }

    public void setSingleMinAmount(BigDecimal singleMinAmount) {
        this.singleMinAmount = singleMinAmount;
    }

    public BigDecimal getSingleMaxAmount() {
        return singleMaxAmount;
    }

    public void setSingleMaxAmount(BigDecimal singleMaxAmount) {
        this.singleMaxAmount = singleMaxAmount;
    }

    public BigDecimal getDayQuotaAmount() {
        return dayQuotaAmount;
    }

    public void setDayQuotaAmount(BigDecimal dayQuotaAmount) {
        this.dayQuotaAmount = dayQuotaAmount;
    }

    public BigDecimal getMonthQuotaAmount() {
        return monthQuotaAmount;
    }

    public void setMonthQuotaAmount(BigDecimal monthQuotaAmount) {
        this.monthQuotaAmount = monthQuotaAmount;
    }

    public String getOutChannelId() {
        return outChannelId;
    }

    public void setOutChannelId(String outChannelId) {
        this.outChannelId = outChannelId == null ? null : outChannelId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "ChannelInfo{" +
                "channelId='" + channelId + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", channelName='" + channelName + '\'' +
                ", channelLevel=" + channelLevel +
                ", type=" + type +
                ", channelTransCode='" + channelTransCode + '\'' +
                ", payUrl='" + payUrl + '\'' +
                ", others='" + others + '\'' +
                ", channelSingleFee=" + channelSingleFee +
                ", channelRateFee=" + channelRateFee +
                ", settleCycle='" + settleCycle + '\'' +
                ", singleMinAmount=" + singleMinAmount +
                ", singleMaxAmount=" + singleMaxAmount +
                ", dayQuotaAmount=" + dayQuotaAmount +
                ", monthQuotaAmount=" + monthQuotaAmount +
                ", outChannelId='" + outChannelId + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}