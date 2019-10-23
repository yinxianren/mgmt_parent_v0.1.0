package com.rxh.anew.dto;


import lombok.Data;

@Data
public class MerchantBasicInformationRegistrationDTO extends AbstractParamModelDTO {
    private String  merOrderId;//商户订单号
    private String  productType; // 产品类型
    private String  charset;    //参数字符集编码	 请求使用的编码格式，固定UTF-8
    private String  signType; //签名类型 固定为MD5
    private String  merchantType;  //商户类型		00公司商户，01个体商户
    private String  terminalMerId; //子商户id		商户系统中商户的编码，要求唯一
    private String  terminalMerName;// 子商户名称	商户系统中商户的名称
    private String  userShortName; //商户简称
    private String identityType; //证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
    private String  identityNum; //证件号码
    private String phone; // 手机号
    private String  province;   // 省份		所在省份
    private String  city;   // 城市		所在市
    private String  address;  //详细地址	详细地址
    private String  bankCode; //银行名称	如：中国农业银行： ABC，中国工商银行： ICBC
    private String  bankCardType;  //卡号类型	1借记卡  2信用卡
    private String  bankCardNum; // 银行卡号
    private String  bankCardPhone;//银行卡手机号
    private String payFee;
    private String backFee;
    private String cardHolderName;
    private String bankName;
    private String category;//经营类目
    private byte[]  miMerCertPic1;//资质影印件',
    private byte[]  miMerCertPic2;//资质影印件',

}
