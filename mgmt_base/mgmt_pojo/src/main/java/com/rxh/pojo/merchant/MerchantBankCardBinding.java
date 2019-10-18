package com.rxh.pojo.merchant;

import com.rxh.pojo.AbstratorParamModel;

/**
 *  银行绑卡登记信息
 * @author  zhanguanghuo
 */

public class MerchantBankCardBinding   extends AbstratorParamModel {
    // 接口编号			用于区分不同的业务接口（进件接口ID）
    private String  bizType;
    //  参数字符集编码		请求使用的编码格式，固定UTF-8
    private String  charset;
    // 签名类型			固定为MD5
    private String  signType;

    // 账户属性		账户属性0：私人，1：公司
    private String  bankaccProp;
    //  证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
    private String  identityType;
    //  证件号码
    private String  identityNum;
    // 卡号类型		1借记卡  2信用卡
    private String  bankcardType;
    // 持卡人姓名
    private String  cardHolderName;
    //  银行卡号
    private String  bankCardNum;
    // 手机号
    private String bankCardPhone;
    // 省份
    private String  province;
    //   城市
    private String  city;


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

    public String getBankaccProp() {
        return bankaccProp;
    }

    public void setBankaccProp(String bankaccProp) {
        this.bankaccProp = bankaccProp;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public String getBankcardType() {
        return bankcardType;
    }

    public void setBankcardType(String bankcardType) {
        this.bankcardType = bankcardType;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
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


    public String getBankCardPhone() {
        return bankCardPhone;
    }

    public void setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
    }


}
