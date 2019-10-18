package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class RepayInfo implements Serializable {
    //出款订单号
    private String transId;
    //收款方银行账户名称
    private String inAcctName;
    //收款方身份证号
    private String identityNum;
    //收款方银行账号
    private String inAcctNo;
    //收款方开户银行
    private String bankName;
    //银行代码
    private String bankCode;
    //省份
    private String province;
    //城市
    private String city;
    //支行名称
    private String bankBranchName;
    //金额
    private BigDecimal amount;
    //银行卡属性
    private String bankAccProp;
    //银行卡类型
    private String bankCardType;
    //摘要
    private String remark;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getInAcctName() {
        return inAcctName;
    }

    public void setInAcctName(String inAcctName) {
        this.inAcctName = inAcctName;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public String getInAcctNo() {
        return inAcctNo;
    }

    public void setInAcctNo(String inAcctNo) {
        this.inAcctNo = inAcctNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBankAccProp() {
        return bankAccProp;
    }

    public void setBankAccProp(String bankAccProp) {
        this.bankAccProp = bankAccProp;
    }

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "RepayInfo{" +
                "transId='" + transId + '\'' +
                ", inAcctName='" + inAcctName + '\'' +
                ", identityNum='" + identityNum + '\'' +
                ", inAcctNo='" + inAcctNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", bankBranchName='" + bankBranchName + '\'' +
                ", amount=" + amount +
                ", bankAccProp='" + bankAccProp + '\'' +
                ", bankCardType='" + bankCardType + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
