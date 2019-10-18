package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TerminalMerchantsWallet implements Serializable {
    private String id;

    private String merId;

    private String terminalMerId;

    private BigDecimal totalAmount;

    private BigDecimal incomeAmount;

    private BigDecimal outAmount;

    private BigDecimal totalBalance;

    private BigDecimal totalUnavailableAmount;

    private BigDecimal totalAvailableAmount;

    private BigDecimal totalFee;

    private BigDecimal totalBond;

    private BigDecimal totalFreezeAmount;

    private Date updateTime;

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

    public String getTerminalMerId() {
        return terminalMerId;
    }

    public void setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId == null ? null : terminalMerId.trim();
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

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getTotalUnavailableAmount() {
        return totalUnavailableAmount;
    }

    public void setTotalUnavailableAmount(BigDecimal totalUnavailableAmount) {
        this.totalUnavailableAmount = totalUnavailableAmount;
    }

    public BigDecimal getTotalAvailableAmount() {
        return totalAvailableAmount;
    }

    public void setTotalAvailableAmount(BigDecimal totalAvailableAmount) {
        this.totalAvailableAmount = totalAvailableAmount;
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