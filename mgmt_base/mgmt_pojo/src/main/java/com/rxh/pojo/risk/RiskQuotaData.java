package com.rxh.pojo.risk;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RiskQuotaData implements Serializable {
     // 
    private Long id;

     // 商户域名表主键/商户表主键/收单机构表主键
    private Integer refId;

     // 0:通用 1:收单机构 2:商户 3:域名
    private Short refType;

     // 1:日 2:月 3:年 
    private Short type;

    // 支付类型
    private String payType;

     // 
    private String tradeTime;

     // 将对应时间内商户的统计数据转换成CNY后累加
    private BigDecimal amount;

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 商户域名表主键/商户表主键/收单机构表主键
     * @return ref_id 商户域名表主键/商户表主键/收单机构表主键
     */
    public Integer getRefId() {
        return refId;
    }

    /**
     * 商户域名表主键/商户表主键/收单机构表主键
     * @param refId 商户域名表主键/商户表主键/收单机构表主键
     */
    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    /**
     * 0:通用 1:收单机构 2:商户 3:域名
     * @return ref_type 0:通用 1:收单机构 2:商户 3:域名
     */
    public Short getRefType() {
        return refType;
    }

    /**
     * 0:通用 1:收单机构 2:商户 3:域名
     * @param refType 0:通用 1:收单机构 2:商户 3:域名
     */
    public void setRefType(Short refType) {
        this.refType = refType;
    }

    /**
     * 1:日 2:月 3:年 
     * @return type 1:日 2:月 3:年 
     */
    public Short getType() {
        return type;
    }

    /**
     * 1:日 2:月 3:年 
     * @param type 1:日 2:月 3:年 
     */
    public void setType(Short type) {
        this.type = type;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 
     * @return trade_time 
     */
    public String getTradeTime() {
        return tradeTime;
    }

    /**
     * 
     * @param tradeTime 
     */
    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    /**
     * 将对应时间内商户的统计数据转换成CNY后累加
     * @return amount 将对应时间内商户的统计数据转换成CNY后累加
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 将对应时间内商户的统计数据转换成CNY后累加
     * @param amount 将对应时间内商户的统计数据转换成CNY后累加
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}