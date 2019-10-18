package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.Date;

public class SysTradeFailAdvice implements Serializable {
     // 主键
    private Long id;

     // 商户号
    private Integer merId;

     // 交易失败笔数
    private Integer tradeFailNum;

     // 交易失败的原因
    private String tradeFailReason;

     // 交易失败提供的建议
    private String tradeFailAdvice;

     // 创建时间
    private Date createTime;

    /**
     * 主键
     * @return id 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 商户号
     * @return mer_id 商户号
     */
    public Integer getMerId() {
        return merId;
    }

    /**
     * 商户号
     * @param merId 商户号
     */
    public void setMerId(Integer merId) {
        this.merId = merId;
    }

    /**
     * 交易失败笔数
     * @return trade_fail_num 交易失败笔数
     */
    public Integer getTradeFailNum() {
        return tradeFailNum;
    }

    /**
     * 交易失败笔数
     * @param tradeFailNum 交易失败笔数
     */
    public void setTradeFailNum(Integer tradeFailNum) {
        this.tradeFailNum = tradeFailNum;
    }

    /**
     * 交易失败的原因
     * @return trade_fail_reason 交易失败的原因
     */
    public String getTradeFailReason() {
        return tradeFailReason;
    }

    /**
     * 交易失败的原因
     * @param tradeFailReason 交易失败的原因
     */
    public void setTradeFailReason(String tradeFailReason) {
        this.tradeFailReason = tradeFailReason == null ? null : tradeFailReason.trim();
    }

    /**
     * 交易失败提供的建议
     * @return trade_fail_advice 交易失败提供的建议
     */
    public String getTradeFailAdvice() {
        return tradeFailAdvice;
    }

    /**
     * 交易失败提供的建议
     * @param tradeFailAdvice 交易失败提供的建议
     */
    public void setTradeFailAdvice(String tradeFailAdvice) {
        this.tradeFailAdvice = tradeFailAdvice == null ? null : tradeFailAdvice.trim();
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}