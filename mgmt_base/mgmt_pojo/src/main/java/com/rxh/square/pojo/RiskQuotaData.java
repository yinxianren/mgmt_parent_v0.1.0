package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class RiskQuotaData implements Serializable {
    public final static Short LIMIT_TYPE_DAY = 1;
    public final static Short LIMIT_TYPE_MONTH = 2;
    public final static Short LIMIT_TYPE_YEAR = 3;
    // 单笔限额
    public final static Short LIMIT_TYPE_ORDER_AMOUNT = 4;

    // 通道风控类型
    public final static Short CHANNEL_REF_TYPE = 1;
    // 通道风控类型
    public final static Short CHANNEL_REF_TYPE_Short = 1;
    // 商户风控类型
    public final static Short MERCHANT_REF_TYPE = 2;



    private Integer id;

    private String refId;

    private List<String> refIdList;
    //1:通道 2:商户
    private Short refType;

    //1:日 2:月 3:年 4:单笔
    private Short type;

    private String tradeTime;

    private List<String> tradeTimeList;

    private BigDecimal amount;


    public List<String> getRefIdList() {
        return refIdList;
    }

    public void setRefIdList(List<String> refIdList) {
        this.refIdList = refIdList;
    }

    public List<String> getTradeTimeList() {
        return tradeTimeList;
    }

    public void setTradeTimeList(List<String> tradeTimeList) {
        this.tradeTimeList = tradeTimeList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId == null ? null : refId.trim();
    }

    public Short getRefType() {
        return refType;
    }

    public void setRefType(Short refType) {
        this.refType = refType;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime == null ? null : tradeTime.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}