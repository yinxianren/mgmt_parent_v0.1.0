package com.rxh.pojo.risk;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RiskOrderTrack implements Serializable {
    // 支付网关	0
    public final static short PAY_STATUS_PAY_GATEWAY = 0;
    // 支付页面	1
    public final static short PAY_STATUS_PAY_PAGE = 1;
    // 平台风控	2
    public final static short PAY_STATUS_RISK = 2;
    // 外置风控	3
    public final static short PAY_STATUS_RISK_OUTER = 3;
    // 发送银行	4
    public final static short PAY_STATUS_TO_BANK = 4;
    // 结果处理	5
    public final static short PAY_STATUS_RESULT_PROCESSING = 5;
    // 返回商户	6
    public final static short PAY_STATUS_RETURN_MERCHANT = 6;

    //
    private Long id;

    // 商户表主键
    private Integer merId;

    // 商户域名表主键
    private Integer siteId;

    //商户订单号
    private String merOrderId;

    // 商户币种(常量组Currency提供)
    private String currency;

    // 原始金额
    private BigDecimal amount;

    //
    private Date tradeTime;

    // 0支付网关 1:支付页面 2:平台风控 3:外置风控 4:发送银行 5:接收银行 6:返回商户
    private Short status;

    //
    private String refer;

    // http 或者https
    private String scheme;

    //
    private String ip;

    //
    private String lang;

    // 0跳转 1内嵌
    private Short interType;

    //
    private String result;
     //
    private String host;

    private String orderInfo;

    //交易原始密文
    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 商户表主键
     *
     * @return mer_id 商户表主键
     */
    public Integer getMerId() {
        return merId;
    }

    /**
     * 商户表主键
     *
     * @param merId 商户表主键
     */
    public void setMerId(Integer merId) {
        this.merId = merId;
    }

    /**
     * 商户域名表主键
     *
     * @return site_id 商户域名表主键
     */
    public Integer getSiteId() {
        return siteId;
    }

    /**
     * 商户域名表主键
     *
     * @param siteId 商户域名表主键
     */
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * @return mer_order_id
     */
    public String getMerOrderId() {
        return merOrderId;
    }

    /**
     * @param merOrderId
     */
    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId == null ? null : merOrderId.trim();
    }

    /**
     * 商户币种(常量组Currency提供)
     *
     * @return currency 商户币种(常量组Currency提供)
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 商户币种(常量组Currency提供)
     *
     * @param currency 商户币种(常量组Currency提供)
     */
    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    /**
     * 原始金额
     *
     * @return amount 原始金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 原始金额
     *
     * @param amount 原始金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return trade_time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getTradeTime() {
        return tradeTime;
    }

    /**
     * @param tradeTime
     */
    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    /**
     * 0支付网关 1:支付页面 2:平台风控 3:外置风控 4:发送银行 5:接收银行
     * 6:返回商户
     *
     * @return status 0支付网关 1:支付页面 2:平台风控 3:外置风控 4:发送银行 5:接收银行
     * 6:返回商户
     */
    public Short getStatus() {
        return status;
    }

    /**
     * 0支付网关 1:支付页面 2:平台风控 3:外置风控 4:发送银行 5:接收银行
     * 6:返回商户
     *
     * @param status 0支付网关 1:支付页面 2:平台风控 3:外置风控 4:发送银行 5:接收银行
     *               6:返回商户
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * @return refer
     */
    public String getRefer() {
        return refer;
    }

    /**
     * @param refer
     */
    public void setRefer(String refer) {
        this.refer = refer == null ? null : refer.trim();
    }

    /**
     * http 或者https
     *
     * @return scheme http 或者https
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * http 或者https
     *
     * @param scheme http 或者https
     */
    public void setScheme(String scheme) {
        this.scheme = scheme == null ? null : scheme.trim();
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * @return lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @param lang
     */
    public void setLang(String lang) {
        this.lang = lang == null ? null : lang.trim();
    }

    /**
     * 0跳转 1内嵌
     *
     * @return inter_type 0跳转 1内嵌
     */
    public Short getInterType() {
        return interType;
    }

    /**
     * 0跳转 1内嵌
     *
     * @param interType 0跳转 1内嵌
     */
    public void setInterType(Short interType) {
        this.interType = interType;
    }

    /**
     * @return result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result
     */
    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}