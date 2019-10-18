package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MerchantCard implements Serializable {
    private String id;

    private String merId;

    private String merOrderId;

    private String terminalMerId;

    private String terminalMerName;

    private BigDecimal tradeFee;

    private BigDecimal backFee;

    private String name;

    private String identityNum;

    private String phone;

    private String cardType;

    private String cardNum;

    private String bankCode;

    private String cvv;

    private String expireDate;

    private String extraChannelId;

    private Integer payType;

    private String backAcountNum;

    private String backBankCode;

    private String backAcountPhone;

    private String backData;

    private String result;

    private Integer status;

    private Date time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId == null ? null : merId.trim();
    }

    public MerchantCard lsetMerId(String merId) {
        this.setMerId(merId);
        return  this;
    }

    public String getMerOrderId() {
        return merOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId == null ? null : merOrderId.trim();
    }

    public String getTerminalMerId() {
        return terminalMerId;
    }

    public void setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId == null ? null : terminalMerId.trim();
    }
    public MerchantCard lsetTerminalMerId(String terminalMerId) {
        this.setTerminalMerId(terminalMerId);
        return this;
    }
    public String getTerminalMerName() {
        return terminalMerName;
    }

    public void setTerminalMerName(String terminalMerName) {
        this.terminalMerName = terminalMerName == null ? null : terminalMerName.trim();
    }

    public BigDecimal getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(BigDecimal tradeFee) {
        this.tradeFee = tradeFee;
    }

    public BigDecimal getBackFee() {
        return backFee;
    }

    public void setBackFee(BigDecimal backFee) {
        this.backFee = backFee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum == null ? null : identityNum.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum == null ? null : cardNum.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv == null ? null : cvv.trim();
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate == null ? null : expireDate.trim();
    }

    public String getExtraChannelId() {
        return extraChannelId;
    }

    public void setExtraChannelId(String extraChannelId) {
        this.extraChannelId = extraChannelId == null ? null : extraChannelId.trim();
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getBackAcountNum() {
        return backAcountNum;
    }

    public void setBackAcountNum(String backAcountNum) {
        this.backAcountNum = backAcountNum == null ? null : backAcountNum.trim();
    }

    public String getBackBankCode() {
        return backBankCode;
    }

    public void setBackBankCode(String backBankCode) {
        this.backBankCode = backBankCode == null ? null : backBankCode.trim();
    }

    public String getBackAcountPhone() {
        return backAcountPhone;
    }

    public void setBackAcountPhone(String backAcountPhone) {
        this.backAcountPhone = backAcountPhone == null ? null : backAcountPhone.trim();
    }

    public String getBackData() {
        return backData;
    }

    public void setBackData(String backData) {
        this.backData = backData == null ? null : backData.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MerchantCard lsetStatus(Integer status) {
        this.setStatus( status);
        return this;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}