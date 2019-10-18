package com.rxh.square.pojo;

import java.io.Serializable;
import java.util.Date;

public class BankCode implements Serializable {
    //ID
    private int id;
    //银行编码
    private String bankCode;
    //银行全称
    private String bankName;
    //银行简称
    private String bankShortName;
    //银行三位编码
    private String bankThreeCode;
    //扩展字段1
    private String extendParameter1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankShortName() {
        return bankShortName;
    }

    public void setBankShortName(String bankShortName) {
        this.bankShortName = bankShortName;
    }

    public String getBankThreeCode() {
        return bankThreeCode;
    }

    public void setBankThreeCode(String bankThreeCode) {
        this.bankThreeCode = bankThreeCode;
    }

    public String getExtendParameter1() {
        return extendParameter1;
    }

    public void setExtendParameter1(String extendParameter1) {
        this.extendParameter1 = extendParameter1;
    }

    public String getExtendParameter2() {
        return extendParameter2;
    }

    public void setExtendParameter2(String extendParameter2) {
        this.extendParameter2 = extendParameter2;
    }

    public String getExtendParameter3() {
        return extendParameter3;
    }

    public void setExtendParameter3(String extendParameter3) {
        this.extendParameter3 = extendParameter3;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    //扩展字段2
    private String extendParameter2;
    //扩展字段3
    private String extendParameter3;
    //更新时间
    private Date createTime;
    //备注
    private  String remark;



}
