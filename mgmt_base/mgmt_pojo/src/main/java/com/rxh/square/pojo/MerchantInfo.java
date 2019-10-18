package com.rxh.square.pojo;

import java.io.Serializable;
import java.util.Date;

public class MerchantInfo implements Serializable {

    private String merId;
    //商户名称
    private String merchantName;
    // 商户简称
    private String merchantShortName;
    //商户类型（A、B）
    private Integer type;
    //上级代理
    private String parentId;
    //商户密钥
    private String secretKey;
    //证件类型
    private Integer identityType;
    //证件号码
    private String identityNum;
    //证件图片地址
    private String identityUrl;
    //电话
    private String phone;
    //电话审核状态
    private Integer phoneStatus;
    //邮箱
    private String email;
    //邮箱审核状态
    private Integer emailStatus;
    //QQ
    private String qq;
    //商户状态（启用、禁用）
    private Integer status;
    //合同开始时间
    private Date agreementStarttime;
    //合同结束时间
    private Date agreementEndtime;
    //创建时间
    private Date createTime;

    private String loginName;
    private  String password;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId == null ? null : merId.trim();
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName == null ? null : merchantName.trim();
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName == null ? null : merchantShortName.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey == null ? null : secretKey.trim();
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

    public String getIdentityUrl() {
        return identityUrl;
    }

    public void setIdentityUrl(String identityUrl) {
        this.identityUrl = identityUrl == null ? null : identityUrl.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(Integer phoneStatus) {
        this.phoneStatus = phoneStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Integer emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAgreementStarttime() {
        return agreementStarttime;
    }

    public void setAgreementStarttime(Date agreementStarttime) {
        this.agreementStarttime = agreementStarttime;
    }

    public Date getAgreementEndtime() {
        return agreementEndtime;
    }

    public void setAgreementEndtime(Date agreementEndtime) {
        this.agreementEndtime = agreementEndtime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MerchantInfo{" +
                "merId='" + merId + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", merchantShortName='" + merchantShortName + '\'' +
                ", type=" + type +
                ", parentId='" + parentId + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", identityType=" + identityType +
                ", identityNum='" + identityNum + '\'' +
                ", identityUrl='" + identityUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneStatus=" + phoneStatus +
                ", email='" + email + '\'' +
                ", emailStatus=" + emailStatus +
                ", qq='" + qq + '\'' +
                ", status=" + status +
                ", agreementStarttime=" + agreementStarttime +
                ", agreementEndtime=" + agreementEndtime +
                ", createTime=" + createTime +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}