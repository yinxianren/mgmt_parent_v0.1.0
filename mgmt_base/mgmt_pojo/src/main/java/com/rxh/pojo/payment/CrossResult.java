package com.rxh.pojo.payment;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/11
 * Time: 13:50
 * Project: Management
 * Package: com.rxh.pojo.payment
 */
public class CrossResult implements Serializable {
    // 平台订单号
    private Long orderId;
    // 银行状态码，用于页面国际化
    private String bankCode;
    // 银行状态
    private Short bankStatus;
    // MD5
    private String md5Info;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Short getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(Short bankStatus) {
        this.bankStatus = bankStatus;
    }

    public String getMd5Info() {
        return md5Info;
    }

    public void setMd5Info(String md5Info) {
        this.md5Info = md5Info;
    }
}
