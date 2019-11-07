package com.rxh.pojo.cross;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rxh.pojo.payment.SquareTrade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BankResult implements Serializable {
    // 银行状态
    private Integer status;
    // 本订单ID
    private String orderId;
    // 商户订单号
    private String merchantOrderId;
    // 银行订单号（流水号）
    private String bankOrderId;
    // 订单变更ID
    private String orderChangeId;
    // 银行生成的订单变更ID（现用于保存AllinPay生成新退款ID代替本平台生成的退款ID）
    private String bankRefundId;
    // 通道时间
    private Date bankTime;
    private String bankResult;
    private String bankCode;
    private String param;
    private String systemRef;
    private String bankData;
    private String result;

    private BigDecimal realAmount;

    private SquareTrade trade;

    private BigDecimal orderAmount;

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public SquareTrade getTrade() {
        return trade;
    }

    public void setTrade(SquareTrade trade) {
        this.trade = trade;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBankData() {
        return bankData;
    }

    public void setBankData(String bankData) {
        this.bankData = bankData;
    }

    public BankResult() {
    }

    public BankResult(Integer status, String bankCode) {
        this.status = status;
        this.bankCode = bankCode;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getBankOrderId() {
        return bankOrderId;
    }

    public void setBankOrderId(String bankOrderId) {
        this.bankOrderId = bankOrderId;
    }

    public String getOrderChangeId() {
        return orderChangeId;
    }

    public void setOrderChangeId(String orderChangeId) {
        this.orderChangeId = orderChangeId;
    }

    public String getBankRefundId() {
        return bankRefundId;
    }

    public void setBankRefundId(String bankRefundId) {
        this.bankRefundId = bankRefundId;

    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getBankTime() {
        return bankTime;
    }

    public void setBankTime(Date bankTime) {
        this.bankTime = bankTime;
    }


    public String getBankResult() {
        return bankResult;
    }

    public void setBankResult(String bankResult) {
        this.bankResult = bankResult;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSystemRef() {
        return systemRef;
    }

    public void setSystemRef(String systemRef) {
        this.systemRef = systemRef;
    }
}
