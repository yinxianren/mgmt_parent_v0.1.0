package com.rxh.pojo.risk;

public class RiskMaxmindOutputWithBLOBs extends RiskMaxmindOutput {
     // 针对客户(风险检测信息)
    private String risk;

     // 针对客户
    private String position;

     // 针对客户
    private String proxy;

     // 针对客户
    private String email;

     // 针对客户
    private String bank;

     // 针对客户
    private String contact;

     // 针对平台maxmind账户信息
    private String accessInfo;

    /**
     * 针对客户(风险检测信息)
     * @return risk 针对客户(风险检测信息)
     */
    public String getRisk() {
        return risk;
    }

    /**
     * 针对客户(风险检测信息)
     * @param risk 针对客户(风险检测信息)
     */
    public void setRisk(String risk) {
        this.risk = risk == null ? null : risk.trim();
    }

    /**
     * 针对客户
     * @return position 针对客户
     */
    public String getPosition() {
        return position;
    }

    /**
     * 针对客户
     * @param position 针对客户
     */
    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    /**
     * 针对客户
     * @return proxy 针对客户
     */
    public String getProxy() {
        return proxy;
    }

    /**
     * 针对客户
     * @param proxy 针对客户
     */
    public void setProxy(String proxy) {
        this.proxy = proxy == null ? null : proxy.trim();
    }

    /**
     * 针对客户
     * @return email 针对客户
     */
    public String getEmail() {
        return email;
    }

    /**
     * 针对客户
     * @param email 针对客户
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 针对客户
     * @return bank 针对客户
     */
    public String getBank() {
        return bank;
    }

    /**
     * 针对客户
     * @param bank 针对客户
     */
    public void setBank(String bank) {
        this.bank = bank == null ? null : bank.trim();
    }

    /**
     * 针对客户
     * @return contact 针对客户
     */
    public String getContact() {
        return contact;
    }

    /**
     * 针对客户
     * @param contact 针对客户
     */
    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    /**
     * 针对平台maxmind账户信息
     * @return access_info 针对平台maxmind账户信息
     */
    public String getAccessInfo() {
        return accessInfo;
    }

    /**
     * 针对平台maxmind账户信息
     * @param accessInfo 针对平台maxmind账户信息
     */
    public void setAccessInfo(String accessInfo) {
        this.accessInfo = accessInfo == null ? null : accessInfo.trim();
    }
}