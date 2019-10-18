package com.rxh.pojo.merchant;


import com.rxh.pojo.AbstratorParamModel;

public class MerchantBasicInformationRegistration extends AbstratorParamModel {
    //  接口编号 用于区分不同的业务接口（进件接口ID）
    private String  bizType;
    //参数字符集编码	 请求使用的编码格式，固定UTF-8
    private String  charset;
    //签名类型 固定为MD5
    private String  signType;
    //商户类型		00公司商户，01个体商户
    private String  merchantType;
    //子商户id		商户系统中商户的编码，要求唯一
    private String  terminalMerId;
    // 子商户名称	商户系统中商户的名称
    private String  terminalMerName;
    //商户简称
    private String  userShortName;
    //证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
    private Integer identityType;
    //证件号码
    private String  identityNum;
    // 手机号
    private String phone;
    // 省份		所在省份
    private String  province;
    // 城市		所在市
    private String  city;
    //详细地址	详细地址
    private String  address;
    //银行名称	如：中国农业银行： ABC，中国工商银行： ICBC
    private String  bankCode;
    //卡号类型	1借记卡  2信用卡
    private String  bankCardType;
    // 银行卡号
    private String  bankCardNum;
    //银行卡手机号
    private String  bankCardPhone;

    private String payFee;

    private String backFee;

    private String cardHolderName;

    private String bankName;

    private String category;//经营类目

    public String getPayFee() {
        return payFee;
    }

    public void setPayFee(String payFee) {
        this.payFee = payFee;
    }

    public String getBackFee() {
        return backFee;
    }

    public void setBackFee(String backFee) {
        this.backFee = backFee;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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


    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }


    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getBankCardPhone() {
        return bankCardPhone;
    }

    public void setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
    }



    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }


}
