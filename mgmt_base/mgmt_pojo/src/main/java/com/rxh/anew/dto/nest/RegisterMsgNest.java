package com.rxh.anew.dto.nest;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 上午9:13
 * Description:
 */
@Getter
public class RegisterMsgNest {

    private String  platformOrderId;//平台流水号
    private String  merOrderId;//商户订单号
    private String  merchantType;  //商户类型		00公司商户，01个体商户
    private String  terminalMerId; //子商户id		商户系统中商户的编码，要求唯一
    private String  terminalMerName;// 子商户名称	商户系统中商户的名称
    private String  userShortName; //商户简称
    private Integer identityType; //证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
    private String  identityNum; //证件号码
    private String  phone; // 手机号
    private String  province;   // 省份		所在省份
    private String  city;   // 城市		所在市
    private String  address;  //详细地址	详细地址
    private String  bankCode; //银行名称	如：中国农业银行： ABC，中国工商银行： ICBC
    private String  bankCardType;  //卡号类型	1借记卡  2信用卡
    private String  bankCardNum; // 银行卡号
    private String  bankCardPhone;//银行卡手机号
    private String  payFee;
    private String  backFee;
    private String  cardHolderName;//用户名
    private String  bankName;//银行名称
    private String  category;//经营类目
    private String  creatTime;//创建时间

    public RegisterMsgNest setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
        return this;
    }

    public RegisterMsgNest setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
        return this;
    }

    public RegisterMsgNest setMerchantType(String merchantType) {
        this.merchantType = merchantType;
        return this;
    }

    public RegisterMsgNest setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
        return this;
    }

    public RegisterMsgNest setTerminalMerName(String terminalMerName) {
        this.terminalMerName = terminalMerName;
        return this;
    }

    public RegisterMsgNest setUserShortName(String userShortName) {
        this.userShortName = userShortName;
        return this;
    }

    public RegisterMsgNest setIdentityType(Integer identityType) {
        this.identityType = identityType;
        return this;
    }

    public RegisterMsgNest setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
        return this;
    }

    public RegisterMsgNest setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public RegisterMsgNest setProvince(String province) {
        this.province = province;
        return this;
    }

    public RegisterMsgNest setCity(String city) {
        this.city = city;
        return this;
    }

    public RegisterMsgNest setAddress(String address) {
        this.address = address;
        return this;
    }

    public RegisterMsgNest setBankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public RegisterMsgNest setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
        return this;
    }

    public RegisterMsgNest setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
        return this;
    }

    public RegisterMsgNest setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
        return this;
    }

    public RegisterMsgNest setPayFee(String payFee) {
        this.payFee = payFee;
        return this;
    }

    public RegisterMsgNest setBackFee(String backFee) {
        this.backFee = backFee;
        return this;
    }

    public RegisterMsgNest setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
        return this;
    }

    public RegisterMsgNest setBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public RegisterMsgNest setCategory(String category) {
        this.category = category;
        return this;
    }

    public RegisterMsgNest setCreatTime(String creatTime) {
        this.creatTime = creatTime;
        return this;
    }
}
