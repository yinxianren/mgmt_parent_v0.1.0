package com.rxh.pojo.cross;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class SquareBankResult implements Serializable {
    // 银行状态
    private Short status;
    // 本订单ID
    private Long orderId;
    // 商户订单号
    private String merchantOrderId;
    // 通道时间
    private Date bankTime;
    // 通道返回的结果（所有信息）
    private String bankResult;
    // 通道返回状态码，用于多语言处理以及获取通道文档对应状态码信息
    private String bankCode;
    // 通道返回参数
    private String param;

    public SquareBankResult() {
    }

    public SquareBankResult(Short status, String bankCode) {
        this.status = status;
        this.bankCode = bankCode;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
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
}
