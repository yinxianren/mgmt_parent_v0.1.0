package com.rxh.pojo.payment;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/15
 * Time: 10:30
 * Project: Management
 * Package: com.rxh.pojo.payment
 */
public class MerchantNotify implements Serializable {
    // 商户订单号
    private String billNo;
    // 订单账单地址
    private String billAddress;
    // 平台订单号
    private Long tradeNo;
    // 币种
    private String currency;
    // 订单金额
    private BigDecimal amount;
    // 订单状态
    private Short succeed;
    // 结果描述
    private String result;
    // md5
    private String md5Info;
    // 备注
    private String remark;
    // 通知地址
    private String notifyUrl;
    // 通知周期
    private Long notifyCycle;
    // 通知次数
    private Integer notifyTimes;

    private String resultJson;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress;
    }

    public Long getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(Long tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Short getSucceed() {
        return succeed;
    }

    public void setSucceed(Short succeed) {
        this.succeed = succeed;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMd5Info() {
        return md5Info;
    }

    public void setMd5Info(String md5Info) {
        this.md5Info = md5Info;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Long getNotifyCycle() {
        return notifyCycle;
    }

    public void setNotifyCycle(Long notifyCycle) {
        this.notifyCycle = notifyCycle;
    }

    public Integer getNotifyTimes() {
        return notifyTimes;
    }

    public void setNotifyTimes(Integer notifyTimes) {
        this.notifyTimes = notifyTimes;
    }

    public String getResultJson() {
        return resultJson;
    }

    public void setResultJson(String resultJson) {
        this.resultJson = resultJson;
    }
}
