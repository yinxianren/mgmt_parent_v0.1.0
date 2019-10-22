package com.rxh.square.pojo;

import java.io.Serializable;
import java.util.Date;

public class PayCardholderInfo implements Serializable {
    private String payId;

    private String cardholderName;

    private String cardholderPhone;

    private Integer identityType;

    private String identityNum;

    private String bankName;

    private String bankBranchName;

    private String bankBranchNum;

    private String bankcardNum;

    private Integer bankcardType;

    private String expiryYear;

    private String expiryMonth;

    private String cvv;

    private String agentMerchantName;

    private Date channelBankTime;

    private String channelBankResult;

    private String currency;

    private String payFee;

    private String orgOrderId;

    private String terminalMerId;

    private Integer orderStatus;

    private String productName;

    private String channelName;

    public String getAgentMerchantName() {
        return agentMerchantName;
    }

    public void setAgentMerchantName(String agentMerchantName) {
        this.agentMerchantName = agentMerchantName;
    }

    public Date getChannelBankTime() {
        return channelBankTime;
    }

    public void setChannelBankTime(Date channelBankTime) {
        this.channelBankTime = channelBankTime;
    }

    public String getChannelBankResult() {
        return channelBankResult;
    }

    public void setChannelBankResult(String channelBankResult) {
        this.channelBankResult = channelBankResult;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayFee() {
        return payFee;
    }

    public void setPayFee(String payFee) {
        this.payFee = payFee;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId == null ? null : payId.trim();
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName == null ? null : cardholderName.trim();
    }

    public String getCardholderPhone() {
        return cardholderPhone;
    }

    public void setCardholderPhone(String cardholderPhone) {
        this.cardholderPhone = cardholderPhone == null ? null : cardholderPhone.trim();
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
        this.identityNum = identityNum == null ? null : identityNum.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName == null ? null : bankBranchName.trim();
    }

    public String getBankBranchNum() {
        return bankBranchNum;
    }

    public void setBankBranchNum(String bankBranchNum) {
        this.bankBranchNum = bankBranchNum == null ? null : bankBranchNum.trim();
    }

    public String getBankcardNum() {
        return bankcardNum;
    }

    public void setBankcardNum(String bankcardNum) {
        this.bankcardNum = bankcardNum == null ? null : bankcardNum.trim();
    }

    public Integer getBankcardType() {
        return bankcardType;
    }

    public void setBankcardType(Integer bankcardType) {
        this.bankcardType = bankcardType;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear == null ? null : expiryYear.trim();
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth == null ? null : expiryMonth.trim();
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv == null ? null : cvv.trim();
    }

    public String getOrgOrderId() {
        return orgOrderId;
    }

    public void setOrgOrderId(String orgOrderId) {
        this.orgOrderId = orgOrderId;
    }

    public String getTerminalMerId() {
        return terminalMerId;
    }

    public void setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}