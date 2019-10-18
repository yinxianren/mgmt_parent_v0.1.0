//package com.rxh.square.pojo;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//
//
//public class MerchantRegisterInfo implements Serializable {
//
//
//    private String id;
//
//    private String merId;
//
//    private String merOrderId;
//
//    private String terminalMerId;
//
//    private String terminalMerName;
//
//    private BigDecimal tradeFee;
//
//    private BigDecimal backFee;
//
//    private String userName;
//
//    private String userShortName;
//
//    private Integer identityType;
//
//    private String identityNum;
//
//    private String phone;
//
//    private String cardType;
//
//    private String cardNum;
//
//    private String bankCode;
//
//    private String cvv;
//
//    private String expireDate;
//
//    private String extraChannelId;
//
//    private Integer payType;
//
//    private String backData;
//
//    private String result;
//
//    private Integer status;
//
//    private Date time;
//
//    private String merchantType;
//
//    private String province;
//
//    private String city;
//
//    private String merchantAddress;
//
//
//
//    public String getMerchantType() {
//        return merchantType;
//    }
//
//    public void setMerchantType(String merchantType) {
//        this.merchantType = merchantType;
//    }
//
//    public String getProvince() {
//        return province;
//    }
//
//    public void setProvince(String province) {
//        this.province = province;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getMerchantAddress() {
//        return merchantAddress;
//    }
//
//    public void setMerchantAddress(String merchantAddress) {
//        this.merchantAddress = merchantAddress;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id == null ? null : id.trim();
//    }
//
//    public String getMerId() {
//        return merId;
//    }
//
//    public void setMerId(String merId) {
//        this.merId = merId == null ? null : merId.trim();
//    }
//
//    public String getMerOrderId() {
//        return merOrderId;
//    }
//
//    public void setMerOrderId(String merOrderId) {
//        this.merOrderId = merOrderId == null ? null : merOrderId.trim();
//    }
//
//    public String getTerminalMerId() {
//        return terminalMerId;
//    }
//
//    public void setTerminalMerId(String terminalMerId) {
//        this.terminalMerId = terminalMerId == null ? null : terminalMerId.trim();
//    }
//
//    public String getTerminalMerName() {
//        return terminalMerName;
//    }
//
//    public void setTerminalMerName(String terminalMerName) {
//        this.terminalMerName = terminalMerName == null ? null : terminalMerName.trim();
//    }
//
//    public BigDecimal getTradeFee() {
//        return tradeFee;
//    }
//
//    public void setTradeFee(BigDecimal tradeFee) {
//        this.tradeFee = tradeFee;
//    }
//
//    public BigDecimal getBackFee() {
//        return backFee;
//    }
//
//    public void setBackFee(BigDecimal backFee) {
//        this.backFee = backFee;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName == null ? null : userName.trim();
//    }
//
//    public String getUserShortName() {
//        return userShortName;
//    }
//
//    public void setUserShortName(String userShortName) {
//        this.userShortName = userShortName == null ? null : userShortName.trim();
//    }
//
//
//    public Integer getIdentityType() {
//        return identityType;
//    }
//
//    public void setIdentityType(Integer identityType) {
//        this.identityType = identityType;
//    }
//
//    public String getIdentityNum() {
//        return identityNum;
//    }
//
//    public void setIdentityNum(String identityNum) {
//        this.identityNum = identityNum == null ? null : identityNum.trim();
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone == null ? null : phone.trim();
//    }
//
//    public String getCardType() {
//        return cardType;
//    }
//
//    public void setCardType(String cardType) {
//        this.cardType = cardType == null ? null : cardType.trim();
//    }
//
//    public String getCardNum() {
//        return cardNum;
//    }
//
//    public void setCardNum(String cardNum) {
//        this.cardNum = cardNum == null ? null : cardNum.trim();
//    }
//
//    public String getBankCode() {
//        return bankCode;
//    }
//
//    public void setBankCode(String bankCode) {
//        this.bankCode = bankCode == null ? null : bankCode.trim();
//    }
//
//    public String getCvv() {
//        return cvv;
//    }
//
//    public void setCvv(String cvv) {
//        this.cvv = cvv == null ? null : cvv.trim();
//    }
//
//    public String getExpireDate() {
//        return expireDate;
//    }
//
//    public void setExpireDate(String expireDate) {
//        this.expireDate = expireDate == null ? null : expireDate.trim();
//    }
//
//    public String getExtraChannelId() {
//        return extraChannelId;
//    }
//
//    public void setExtraChannelId(String extraChannelId) {
//        this.extraChannelId = extraChannelId == null ? null : extraChannelId.trim();
//    }
//
//    public Integer getPayType() {
//        return payType;
//    }
//
//    public void setPayType(Integer payType) {
//        this.payType = payType;
//    }
//
//    public String getBackData() {
//        return backData;
//    }
//
//    public void setBackData(String backData) {
//        this.backData = backData == null ? null : backData.trim();
//    }
//
//    public String getResult() {
//        return result;
//    }
//
//    public void setResult(String result) {
//        this.result = result == null ? null : result.trim();
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public Date getTime() {
//        return time;
//    }
//
//    public void setTime(Date time) {
//        this.time = time;
//    }
//}