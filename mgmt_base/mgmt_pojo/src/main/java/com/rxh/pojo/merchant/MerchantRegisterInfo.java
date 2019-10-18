package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;


public class MerchantRegisterInfo implements Serializable {

    private String id;

    private String merId;

    private String merOrderId;

    private String terminalMerId;

    private String terminalMerName;

    private String userName;

    private String userShortName;

    private Integer identityType;

    private String identityNum;

    private String bankCardName;

    private String phone;

    private String merchantType;

    private String province;

    private String city;

    private String merchantAddress;

    private Date time;
    //以下字段为冗余字段
    private String extraChannelId;
    //以下字段为冗余字段
    private String backData;
    //以下字段为冗余字段
    private String cardType;
    //以下字段为冗余字段
    private String cardNum;


    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getBackData() {
        return backData;
    }

    public void setBackData(String backData) {
        this.backData = backData;
    }

    public String getExtraChannelId() {
        return extraChannelId;
    }

    public void setExtraChannelId(String extraChannelId) {
        this.extraChannelId = extraChannelId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTerminalMerId() {
        return terminalMerId;
    }

    public void setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
    }

    public String getTerminalMerName() {
        return terminalMerName;
    }

    public void setTerminalMerName(String terminalMerName) {
        this.terminalMerName = terminalMerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserShortName() {
        return userShortName;
    }

    public void setUserShortName(String userShortName) {
        this.userShortName = userShortName;
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

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MerchantRegisterInfo{" +
                "id='" + id + '\'' +
                ", merId='" + merId + '\'' +
                ", terminalMerId='" + terminalMerId + '\'' +
                ", terminalMerName='" + terminalMerName + '\'' +
                ", userName='" + userName + '\'' +
                ", userShortName='" + userShortName + '\'' +
                ", identityType=" + identityType +
                ", identityNum='" + identityNum + '\'' +
                ", bankCardName='" + bankCardName + '\'' +
                ", phone='" + phone + '\'' +
                ", merchantType='" + merchantType + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", merchantAddress='" + merchantAddress + '\'' +
                ", time=" + time +
                '}';
    }
}