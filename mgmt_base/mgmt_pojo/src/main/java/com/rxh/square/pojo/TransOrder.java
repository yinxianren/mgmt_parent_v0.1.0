package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransOrder implements Serializable {

    private String transId;

    private String merId;

    private String merOrderId;

    private String originalMerOrderId;

    private String organizationId;

    private String channelId;

    private String payType;

    private String channelTransCode;

    private String currency;

    private BigDecimal amount;

    private BigDecimal realAmount;

    private BigDecimal terminalFee;

    private BigDecimal fee;

    private BigDecimal channelFee;

    private BigDecimal agentFee;

    private BigDecimal income;

    //交易状态（0成功、1失败、2未支付、3处理中）
    private Integer orderStatus;

    private List<Integer> orderStatusList;

    private Integer settleStatus;

    private Date tradeTime;

    private String terminalMerId;

    private String terminalMerName;

    private Date bankTime;

    private String orgOrderId;

    private String tradeResult;

    private String agmentId;
    //队列使用次数，冗余字段
    private Integer MQTimes = 0;
    // 冗余字段  查询使用
    private String beginTime;
    // 冗余字段  查询使用
    private String endTime;

    public List<Integer> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<Integer> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId == null ? null : transId.trim();
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

    public String getOriginalMerOrderId() {
        return originalMerOrderId;
    }

    public void setOriginalMerOrderId(String originalMerOrderId) {
        this.originalMerOrderId = originalMerOrderId == null ? null : originalMerOrderId.trim();
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId == null ? null : organizationId.trim();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public String getChannelTransCode() {
        return channelTransCode;
    }

    public void setChannelTransCode(String channelTransCode) {
        this.channelTransCode = channelTransCode == null ? null : channelTransCode.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public BigDecimal getTerminalFee() {
        return terminalFee;
    }

    public void setTerminalFee(BigDecimal terminalFee) {
        this.terminalFee = terminalFee;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getChannelFee() {
        return channelFee;
    }

    public void setChannelFee(BigDecimal channelFee) {
        this.channelFee = channelFee;
    }

    public BigDecimal getAgentFee() {
        return agentFee;
    }

    public void setAgentFee(BigDecimal agentFee) {
        this.agentFee = agentFee;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTerminalMerId() {
        return terminalMerId;
    }

    public void setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId == null ? null : terminalMerId.trim();
    }

    public String getTerminalMerName() {
        return terminalMerName;
    }

    public void setTerminalMerName(String terminalMerName) {
        this.terminalMerName = terminalMerName == null ? null : terminalMerName.trim();
    }

    public Date getBankTime() {
        return bankTime;
    }

    public void setBankTime(Date bankTime) {
        this.bankTime = bankTime;
    }

    public String getOrgOrderId() {
        return orgOrderId;
    }

    public void setOrgOrderId(String orgOrderId) {
        this.orgOrderId = orgOrderId == null ? null : orgOrderId.trim();
    }

    public String getTradeResult() {
        return tradeResult;
    }

    public void setTradeResult(String tradeResult) {
        this.tradeResult = (tradeResult == null ? null : tradeResult.trim());
    }

    public String getAgmentId() {
        return agmentId;
    }

    public void setAgmentId(String agmentId) {
        this.agmentId = agmentId == null ? null : agmentId.trim();
    }


    @Override
    public String toString() {
        return "TransOrder{" +
                "transId='" + transId + '\'' +
                ", merId='" + merId + '\'' +
                ", merOrderId='" + merOrderId + '\'' +
                ", originalMerOrderId='" + originalMerOrderId + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", payType='" + payType + '\'' +
                ", channelTransCode='" + channelTransCode + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", realAmount=" + realAmount +
                ", terminalFee=" + terminalFee +
                ", fee=" + fee +
                ", channelFee=" + channelFee +
                ", agentFee=" + agentFee +
                ", income=" + income +
                ", orderStatus=" + orderStatus +
                ", settleStatus=" + settleStatus +
                ", tradeTime=" + tradeTime +
                ", terminalMerId='" + terminalMerId + '\'' +
                ", terminalMerName='" + terminalMerName + '\'' +
                ", bankTime=" + bankTime +
                ", orgOrderId='" + orgOrderId + '\'' +
                ", tradeResult='" + tradeResult + '\'' +
                ", agmentId='" + agmentId + '\'' +
                '}';
    }

    public Integer getMQTimes() {
        return MQTimes;
    }

    public void setMQTimes(Integer MQTimes) {
        this.MQTimes = MQTimes;
    }
}
