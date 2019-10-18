package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AgentMerchantsDetails implements Serializable {

    private String id;

    private String agentMerId;

    private String merOrderId;

    private String orderId;

    private BigDecimal amount;

    private String type;

    private BigDecimal inAmount;

    private BigDecimal outAmount;

    private BigDecimal fee;

    private BigDecimal totalBenifit;

    private Date updateTime;

    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAgentMerId() {
        return agentMerId;
    }

    public void setAgentMerId(String agentMerId) {
        this.agentMerId = agentMerId == null ? null : agentMerId.trim();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public BigDecimal getInAmount() {
        return inAmount;
    }

    public void setInAmount(BigDecimal inAmount) {
        this.inAmount = inAmount;
    }

    public BigDecimal getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(BigDecimal outAmount) {
        this.outAmount = outAmount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getTotalBenifit() {
        return totalBenifit;
    }

    public void setTotalBenifit(BigDecimal totalBenifit) {
        this.totalBenifit = totalBenifit;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public String toString() {
        return "AgentMerchantsDetails{" +
                "id='" + id + '\'' +
                ", agentMerId='" + agentMerId + '\'' +
                ", merOrderId='" + merOrderId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", inAmount=" + inAmount +
                ", outAmount=" + outAmount +
                ", fee=" + fee +
                ", totalBenifit=" + totalBenifit +
                ", updateTime=" + updateTime +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}