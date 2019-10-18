package com.rxh.square.pojo;

import java.io.Serializable;

public class MerchantAcount implements Serializable {
    private String merId;

    //收款主体名称
    private String benefitName;
    //银行名称
    private String bankName;
    //银行卡号
    private String bankcardNum;
    //银行卡类型
    private Integer bankcardType;
    //收款银行网点名称
    private String bankBranchName;
    //收款行开户行号
    private String bankBranchNum;
    //证件类型
    private Integer identityType;
    //证件号码
    private String identityNum;
    //证件图片地址1
    private String identityUrl1;
    //证件图片地址2
    private String identityUrl2;

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId == null ? null : merId.trim();
    }

    public String getBenefitName() {
        return benefitName;
    }

    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName == null ? null : benefitName.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
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

    public String getIdentityUrl1() {
        return identityUrl1;
    }

    public void setIdentityUrl1(String identityUrl1) {
        this.identityUrl1 = identityUrl1 == null ? null : identityUrl1.trim();
    }

    public String getIdentityUrl2() {
        return identityUrl2;
    }

    public void setIdentityUrl2(String identityUrl2) {
        this.identityUrl2 = identityUrl2 == null ? null : identityUrl2.trim();
    }
}