package com.rxh.square.pojo;

import java.io.Serializable;

public class AgentMerchantInfo implements Serializable {
    private String agentMerchantId;

    //代理商名称
    private String agentMerchantName;

    // 代理商简称
    private String agentMerchantShortName;

    // 证件类型
    private Integer agentIdentityType;

    //证件号码
    private String agentIdentityNum;

    //图片地址
    private String agentIdentityUrl;

    // 电话
    private String agentPhone;

    //电话审核状态
    private Integer agentPhoneStatus;

    //邮箱
    private String agentEmail;

    //邮箱审核状态
    private Integer agentEmailStatus;

    //qq
    private String agentQq;

    //商户审核状态
    private Integer agentStatus;

    private String password;

    private String loginName;



    private AgentMerchantSetting agentMerchantSetting;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAgentMerchantId() {
        return agentMerchantId;
    }

    public void setAgentMerchantId(String agentMerchantId) {
        this.agentMerchantId = agentMerchantId == null ? null : agentMerchantId.trim();
    }

    public String getAgentMerchantName() {
        return agentMerchantName;
    }

    public void setAgentMerchantName(String agentMerchantName) {
        this.agentMerchantName = agentMerchantName == null ? null : agentMerchantName.trim();
    }

    public String getAgentMerchantShortName() {
        return agentMerchantShortName;
    }

    public void setAgentMerchantShortName(String agentMerchantShortName) {
        this.agentMerchantShortName = agentMerchantShortName == null ? null : agentMerchantShortName.trim();
    }

    public Integer getAgentIdentityType() {
        return agentIdentityType;
    }

    public void setAgentIdentityType(Integer agentIdentityType) {
        this.agentIdentityType = agentIdentityType;
    }

    public String getAgentIdentityNum() {
        return agentIdentityNum;
    }

    public void setAgentIdentityNum(String agentIdentityNum) {
        this.agentIdentityNum = agentIdentityNum == null ? null : agentIdentityNum.trim();
    }

    public String getAgentIdentityUrl() {
        return agentIdentityUrl;
    }

    public void setAgentIdentityUrl(String agentIdentityUrl) {
        this.agentIdentityUrl = agentIdentityUrl == null ? null : agentIdentityUrl.trim();
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone == null ? null : agentPhone.trim();
    }

    public Integer getAgentPhoneStatus() {
        return agentPhoneStatus;
    }

    public void setAgentPhoneStatus(Integer agentPhoneStatus) {
        this.agentPhoneStatus = agentPhoneStatus;
    }

    public String getAgentEmail() {
        return agentEmail;
    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail == null ? null : agentEmail.trim();
    }

    public Integer getAgentEmailStatus() {
        return agentEmailStatus;
    }

    public void setAgentEmailStatus(Integer agentEmailStatus) {
        this.agentEmailStatus = agentEmailStatus;
    }

    public String getAgentQq() {
        return agentQq;
    }

    public void setAgentQq(String agentQq) {
        this.agentQq = agentQq == null ? null : agentQq.trim();
    }

    public Integer getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(Integer agentStatus) {
        this.agentStatus = agentStatus;
    }

    public AgentMerchantSetting getAgentMerchantSetting() {
        return agentMerchantSetting;
    }

    public void setAgentMerchantSetting(AgentMerchantSetting agentMerchantSetting) {
        this.agentMerchantSetting = agentMerchantSetting;
    }
}