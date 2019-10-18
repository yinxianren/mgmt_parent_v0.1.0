package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MerchantWallet implements Serializable {
    private String merId;

    private BigDecimal totalAmount;

    private BigDecimal incomeAmount;

    private BigDecimal outAmount;

    private BigDecimal totalFee;

    private BigDecimal totalBond;

    private BigDecimal totalBalance;

    private BigDecimal totalAvailableAmount;

    private BigDecimal totalUnavailableAmount;

    private BigDecimal totalFreezeAmount;

    private Date updateTime;

    private BigDecimal feeProfit;

    private BigDecimal withdrawalAmount;

    public BigDecimal getFeeProfit() {
        return feeProfit;
    }

    public void setFeeProfit(BigDecimal feeProfit) {
        this.feeProfit = feeProfit;
    }

    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId == null ? null : merId.trim();
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

    public BigDecimal getTotalBond() {
        return totalBond;
    }

    public void setTotalBond(BigDecimal totalBond) {
        this.totalBond = totalBond;
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