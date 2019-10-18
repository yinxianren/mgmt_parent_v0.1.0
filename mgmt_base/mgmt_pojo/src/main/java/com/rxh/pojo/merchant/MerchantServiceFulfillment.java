package com.rxh.pojo.merchant;


import com.rxh.pojo.AbstratorParamModel;

public class MerchantServiceFulfillment extends AbstratorParamModel {
    //接口编号		用于区分不同的业务接口（进件接口ID）
    private String  bizType;
   //参数字符集编码		请求使用的编码格式，固定UTF-8
    private String  charset;
    // 签名类型		固定为MD5
    private String  signType;

    private String mimerCertPic1;

    private String mimerCertPic2;

    public String getMimerCertPic1() {
        return mimerCertPic1;
    }

    public void setMimerCertPic1(String mimerCertPic1) {
        this.mimerCertPic1 = mimerCertPic1;
    }

    public String getMimerCertPic2() {
        return mimerCertPic2;
    }

    public void setMimerCertPic2(String mimerCertPic2) {
        this.mimerCertPic2 = mimerCertPic2;
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

}
