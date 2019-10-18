package com.rxh.utils;

/**
 * Created with IDEA
 * author: JJww
 * Date:2018/9/12
 * Time:17:39
 */
public class PaymentInfo {

    private String md5Key;

    private String paymentUrl;

    private String bankNotifyUrl;

    private String refundNotifyUrl;

    private String fpxKeyPath;

    private String bankCardNotifyUrl;

    public String getBankCardNotifyUrl() {
        return bankCardNotifyUrl;
    }

    public void setBankCardNotifyUrl(String bankCardNotifyUrl) {
        this.bankCardNotifyUrl = bankCardNotifyUrl;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getBankNotifyUrl() {
        return bankNotifyUrl;
    }

    public void setBankNotifyUrl(String bankNotifyUrl) {
        this.bankNotifyUrl = bankNotifyUrl;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public void setRefundNotifyUrl(String refundNotifyUrl) {
        this.refundNotifyUrl = refundNotifyUrl;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "md5Key='" + md5Key + '\'' +
                ", paymentUrl='" + paymentUrl + '\'' +
                ", bankNotifyUrl='" + bankNotifyUrl + '\'' +
                ", refundNotifyUrl='" + refundNotifyUrl + '\'' +
                ", fpxKeyPath='" + fpxKeyPath + '\'' +
                '}';
    }

    public String getFpxKeyPath() {
        return fpxKeyPath;
    }

    public void setFpxKeyPath(String fpxKeyPath) {
        this.fpxKeyPath = fpxKeyPath;
    }

}
