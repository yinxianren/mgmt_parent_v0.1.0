package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MerchantRegisterCollect implements Serializable {
    private String id;

    private String merId;

    private String merOrderId;

    private String terminalMerId;

    private String terminalMerName;

    private String cardType;

    private String bankCode;

    private String cardNum;

    private String cvv;

    private String settleCardType;

    private String settleCardNum;

    private String settleBankCode;

    private String settleBankBranchNo;

    private String settleBankBranchName;

    private String expireDate;

    private String extraChannelId;

    private Integer payType;

    private String backData;

    private String result;

    private Integer status;

    private BigDecimal tradeFee;

    private BigDecimal backFee;

    private Date time;

    private String bankCardPhone;

    private String year;

    private String month;

    private String cardProp;

    private String settleCardProp;

    public String getCardProp() {
        return cardProp;
    }

    public void setCardProp(String cardProp) {
        this.cardProp = cardProp;
    }

    public String getSettleCardProp() {
        return settleCardProp;
    }

    public void setSettleCardProp(String settleCardProp) {
        this.settleCardProp = settleCardProp;
    }

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
        this.merId = merId;
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

    public String getTerminalMerName() {
        return terminalMerName;
    }

    public void setTerminalMerName(String terminalMerName) {
        this.terminalMerName = terminalMerName == null ? null : terminalMerName.trim();
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum == null ? null : cardNum.trim();
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv == null ? null : cvv.trim();
    }

    public String getSettleCardType() {
        return settleCardType;
    }

    public void setSettleCardType(String settleCardType) {
        this.settleCardType = settleCardType == null ? null : settleCardType.trim();
    }

    public String getSettleCardNum() {
        return settleCardNum;
    }

    public void setSettleCardNum(String settleCardNum) {
        this.settleCardNum = settleCardNum == null ? null : settleCardNum.trim();
    }

    public String getSettleBankCode() {
        return settleBankCode;
    }

    public void setSettleBankCode(String settleBankCode) {
        this.settleBankCode = settleBankCode == null ? null : settleBankCode.trim();
    }

    public String getSettleBankBranchNo() {
        return settleBankBranchNo;
    }

    public void setSettleBankBranchNo(String settleBankBranchNo) {
        this.settleBankBranchNo = settleBankBranchNo == null ? null : settleBankBranchNo.trim();
    }

    public String getSettleBankBranchName() {
        return settleBankBranchName;
    }

    public void setSettleBankBranchName(String settleBankBranchName) {
        this.settleBankBranchName = settleBankBranchName == null ? null : settleBankBranchName.trim();
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

    public MerchantRegisterCollect lsetStatus(Integer status) {
        this.setStatus( status);
        return this;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getBankCardPhone() {
        return bankCardPhone;
    }

    public void setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}