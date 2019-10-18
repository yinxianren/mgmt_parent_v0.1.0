package com.rxh.pojo.payment;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 信用卡无卡支付
 */
public class NoCardInfo implements Serializable {

    private String bizType; //接口编号，用于区分不同的业务接口
    private String charset; //参数字符集编码，固定UTF-8
    private String signType; //签名类型，固定为MD5
    private String merId; //商户号，我司分配给接入方的唯一编码
    private String merOrderId; //商户订单号(唯一)
    private String bankCardNum; //卡号
    private String expireYear; //有效期年
    private String expireMonth; //有效期月
    private String cvv; //安全码
    private  String currency; // 交易的币种，固定传CNY
    private BigDecimal amount; //交易金额
    private  Integer identityType; //证件类型（01身份证、02护照、03港澳回乡证、04台胞证、05军官证）
    private  String identityNum; //证件号码
    private  String ext1; //扩展参数
    private  String noticeUrl; //回调地址
    private  String signMsg; //签名字符串


    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getMerOrderId() {
        return merOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(String expireYear) {
        this.expireYear = expireYear;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(String expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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

    public Integer getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Integer identityType) {
        this.identityType = identityType;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getSignMsg() {
        return signMsg;
    }

    public void setSignMsg(String signMsg) {
        this.signMsg = signMsg;
    }
}
