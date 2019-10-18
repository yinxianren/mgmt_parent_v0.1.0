package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantQuotaRisk implements Serializable {
    private String merId;

    private BigDecimal singleQuotaAmount;

    private BigDecimal dayQuotaAmount;

    private BigDecimal monthQuotaAmount;

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId == null ? null : merId.trim();
    }

    public BigDecimal getSingleQuotaAmount() {
        return singleQuotaAmount;
    }

    public void setSingleQuotaAmount(BigDecimal singleQuotaAmount) {
        this.singleQuotaAmount = singleQuotaAmount;
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
}