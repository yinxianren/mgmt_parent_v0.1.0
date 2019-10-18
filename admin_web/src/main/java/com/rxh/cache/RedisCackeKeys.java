package com.rxh.cache;


public enum RedisCackeKeys {

    AGENT_MERCHANT_SETTING("agent_merchant_setting","代理商设置"),
    CHANNEL_INFO("channel_info","通道信息"),
    EXTRA_CHANNEL_INFO("extra_channel_info","附属通道信息"),
    MERCHANT_CARD("merchant_card","商户卡信息"),
    MERCHANT_INFO("merchant_info","商户信息"),
    MERCHANT_QUOTA_RISK("merchant_quota_risk","商户配额费率"),
    MERCHANT_RATE("merchant_rate","商户费率"),
    MERCHANT_REGISTER_COLLECT("merchant_register_collect","商户注册附属信息"),
    MERCHANT_REGISTER_INFO("merchant_register_info","商户注册信息"),
    MERCHANT_SETTING("merchant_setting","商户设置"),
    ORGANIZATION_INFO("organization_info","组织信息")
    ;
    private String key;
    private String desc;

    RedisCackeKeys(String key,String desc){
        this.key=key;
        this.desc=desc;
    }

    public  String keyDescription(String key){
        for(RedisCackeKeys rck:RedisCackeKeys.values()){
           if(rck.getKey().equals(key))
               return rck.getDesc();
        }
        return null;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
