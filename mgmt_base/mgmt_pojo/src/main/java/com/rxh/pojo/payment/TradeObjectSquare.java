package com.rxh.pojo.payment;

import com.rxh.pojo.AbstratorParamModel;

import java.io.Serializable;
import java.math.BigDecimal;


public class TradeObjectSquare extends AbstratorParamModel implements Serializable {
    //接口编号
    private String bizType;
    private String charset;
    private String signType; //签名类型，固定为MD5
    private String userName; //
    private String smscode;
    //海懿代付参数
    private String  inAcctNo;//收款卡号
    private String  inAcctName;//收款户名
    private String  remark;//用途/备注
    private BigDecimal fee;
    // 开户人
    private String benefitName;
    // 银行卡号
    private String bankCardNum;
    // 开户行
    private String bankName;
    // 开户行网点名称
    private String bankBranchName;
    // 开户行号
    private String bankBranchNum;
    private String cvv;
    // 卡类型
    private  Integer bankCardType;
    // 证件类型
    private  Integer identityType;
    // 证件号码
    private String identityNum;
    // 扫码字符串
    private String signMsg;
    // 信用卡无卡支付
    private  String ext1; //扩展参数
    private String expireYear; //有效期年
    private String expireMonth; //有效期月
    private String currency;
    private String authCode;
    private String productName;
    private String cardholderName;
    private String cardholderPhone;
    private String bankCardPhone;
    private String bankCode;
    private String validDate;
    private String securityCode;
    private BigDecimal payFee;
    private BigDecimal backFee;
    private String backCardNum;
    private String backBankCode;
    private String backCardPhone;
    private String terminalMerName;
    private String expiryYear;
    private String expiryMonth;
    private String bankcardType;
    private String smsCode;
    private String originalMerOrderId;
    private String innerType;
    private String productCategory;//商品类型
    private String mimerCertPic1;//身份证正面
    private String mimerCertPic2;//身份证反面
    private String deviceId;//交易设备号
    private String deviceType;//付款用户设备类型
    private String macAddress;//mac地址
    private String queryType;//查询类型
    private String city;
    private String province;

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

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

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

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getInAcctNo() {
        return inAcctNo;
    }

    public void setInAcctNo(String inAcctNo) {
        this.inAcctNo = inAcctNo;
    }

    public String getInAcctName() {
        return inAcctName;
    }

    public void setInAcctName(String inAcctName) {
        this.inAcctName = inAcctName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInnerType() {
        return innerType;
    }

    public void setInnerType(String innerType) {
        this.innerType = innerType;
    }

    public String getOriginalMerOrderId() {
        return originalMerOrderId;
    }

    public void setOriginalMerOrderId(String originalMerOrderId) {
        this.originalMerOrderId = originalMerOrderId;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public String getBackCardPhone() {
        return backCardPhone;
    }

    public void setBackCardPhone(String backCardPhone) {
        this.backCardPhone = backCardPhone;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    public String getBankcardType() {
        return bankcardType;
    }

    public void setBankcardType(String bankcardType) {
        this.bankcardType = bankcardType;
    }

    public BigDecimal getBackFee() {
        return backFee;
    }

    public void setBackFee(BigDecimal backFee) {
        this.backFee = backFee != null ? backFee.setScale(2) : null;
    }

    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee != null ? payFee.setScale(2) : null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBankCardPhone() {
        return bankCardPhone;
    }

    public void setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getBackCardNum() {
        return backCardNum;
    }

    public void setBackCardNum(String backCardNum) {
        this.backCardNum = backCardNum;
    }

    public String getBackBankCode() {
        return backBankCode;
    }

    public void setBackBankCode(String backBankCode) {
        this.backBankCode = backBankCode;
    }

    public String getTerminalMerName() {
        return terminalMerName;
    }

    public void setTerminalMerName(String terminalMerName) {
        this.terminalMerName = terminalMerName;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardholderPhone() {
        return cardholderPhone;
    }

    public void setCardholderPhone(String cardholderPhone) {
        this.cardholderPhone = cardholderPhone;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBenefitName() {
        return benefitName;
    }

    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBankBranchNum() {
        return bankBranchNum;
    }

    public void setBankBranchNum(String bankBranchNum) {
        this.bankBranchNum = bankBranchNum;
    }

    public Integer getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(Integer bankCardType) {
        this.bankCardType = bankCardType;
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

    public String getSignMsg() {
        return signMsg;
    }

    public void setSignMsg(String signMsg) {
        this.signMsg = signMsg;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {

        this.currency = currency ==null ? null : currency.toUpperCase();
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(String expireYear) {
        this.expireYear = expireYear;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(String expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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
