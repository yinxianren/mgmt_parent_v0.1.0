package com.rxh.jedis;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/8/28
 * Time: 14:09
 * Project: Management
 * Package: com.rxh.jedis
 */
public enum CacheEnum {

    core_acquirer_account("coreAcquirerAccountCache"),
    core_acquirer("coreAcquirerCache"),
    core_acquirer_rate("coreAcquirerRateCache"),
    core_bank("coreBankCache"),
    core_trade_rate("coreTradeRateCache"),
    merchant_info("merchantInfoCache"),
    merchant_pay("merchantPayCache"),
    merchant_rate("merchantRateCache"),
    merchant_setting("merchantSettingCache"),
    merchant_web_site("merchantWebSiteCache"),
    merchant_rate_percent("merchantRatePercentCache"),
    risk_business("riskBusinessCache"),
    risk_danger("riskDangerCache"),
    risk_extend("riskExtendCache"),
    risk_issuer("riskIssuerCache"),
    risk_limit("riskLimitCache"),
    risk_quota("riskQuotaCache"),
    risk_quota_data("RiskQuotaDataCache"),
    risk_refuse("riskRefuseCache"),
    sys_constant("sysConstantCache"),
    sys_email_config("sysEmailConfigCache"),
    sys_group("sysGroupCache");

    private String beanName;

    CacheEnum(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
