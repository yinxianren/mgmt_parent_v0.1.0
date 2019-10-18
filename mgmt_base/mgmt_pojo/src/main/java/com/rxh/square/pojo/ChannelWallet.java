package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ChannelWallet implements Serializable {
    private String id;

    private String channelId;

    private String channelTransCode;

    private String payType;

    private BigDecimal totalAmount;

    private BigDecimal incomeAmount;

    private BigDecimal outAmount;

    private BigDecimal totalFee;

    private BigDecimal feeProfit;

    private BigDecimal totalBalance;

    private BigDecimal totalAvailableAmount;

    private BigDecimal totalUnavailableAmount;

    private BigDecimal totalBond;

    private BigDecimal totalFreezeAmount;

    private Date updateTime;

    public BigDecimal getFeeProfit() {
        return feeProfit;
    }

    public void setFeeProfit(BigDecimal feeProfit) {
        this.feeProfit = feeProfit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getChannelTransCode() {
        return channelTransCode;
    }

    public void setChannelTransCode(String channelTransCode) {
        this.channelTransCode = channelTransCode == null ? null : channelTransCode.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(BigDecimal outAmount) {
        this.outAmount = outAmount;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getTotalAvailableAmount() {
        return totalAvailableAmount;
    }

    public void setTotalAvailableAmount(BigDecimal totalAvailableAmount) {
        this.totalAvailableAmount = totalAvailableAmount;
    }

    public BigDecimal getTotalUnavailableAmount() {
        return totalUnavailableAmount;
    }

    public void setTotalUnavailableAmount(BigDecimal totalUnavailableAmount) {
        this.totalUnavailableAmount = totalUnavailableAmount;
    }

    public BigDecimal getTotalBond() {
        return totalBond;
    }

    public void setTotalBond(BigDecimal totalBond) {
        this.totalBond = totalBond;
    }

    public BigDecimal getTotalFreezeAmount() {
        return totalFreezeAmount;
    }

    public void setTotalFreezeAmount(BigDecimal totalFreezeAmount) {
        this.totalFreezeAmount = totalFreezeAmount;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
