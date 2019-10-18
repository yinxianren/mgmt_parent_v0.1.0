package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantRate implements Serializable {
    private String id;

    private String merId;

    private String payType;

    private BigDecimal singleFee;

    private BigDecimal rateFee;

    private BigDecimal bondRate;

    private String settlecycle;

//    bond_cycle
    private Integer bondCycle;

    private Integer status;

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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public BigDecimal getSingleFee() {
        return singleFee;
    }

    public void setSingleFee(BigDecimal singleFee) {
        this.singleFee = singleFee;
    }

    public BigDecimal getRateFee() {
        return rateFee;
    }

    public void setRateFee(BigDecimal rateFee) {
        this.rateFee = rateFee;
    }

    public BigDecimal getBondRate() {
        return bondRate;
    }

    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }

    public String getSettlecycle() {
        return settlecycle;
    }

    public void setSettlecycle(String settlecycle) {
        this.settlecycle = settlecycle == null ? null : settlecycle.trim();
    }

    public Integer getBondCycle() {
        return bondCycle;
    }

    public void setBondCycle(Integer bondCycle) {
        this.bondCycle = bondCycle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}