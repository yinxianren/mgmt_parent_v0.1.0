package com.rxh.cache;

import com.rxh.cache.redis.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheCommonCompoment {


    public final AgentMerchantSettingCache agentMerchantSettingCache;
    public final MerchantInfoCache merchantInfoCache;
    public final MerchantSettingCache merchantSettingCache;
    public final ChannelInfoCache channelInfoCache;
    public final ExtraChannelInfoCache extraChannelInfoCache;
    public final MerchantQuotaRiskCache merchantQuotaRiskCache;
    public final MerchantRateCache merchantRateCache;
    public final OrganizationInfoCache organizationInfoCache;

    @Autowired
    public RedisCacheCommonCompoment(AgentMerchantSettingCache agentMerchantSettingCache
            ,MerchantInfoCache merchantInfoCache
            ,MerchantSettingCache merchantSettingCache
            ,ChannelInfoCache channelInfoCache
            ,ExtraChannelInfoCache extraChannelInfoCache
            ,MerchantQuotaRiskCache merchantQuotaRiskCache
            ,MerchantRateCache merchantRateCache
            ,OrganizationInfoCache organizationInfoCache
    ){
        this.agentMerchantSettingCache=agentMerchantSettingCache;
        this.merchantInfoCache=merchantInfoCache;
        this.merchantSettingCache=merchantSettingCache;
        this.channelInfoCache=channelInfoCache;
        this.extraChannelInfoCache=extraChannelInfoCache;
        this.merchantQuotaRiskCache=merchantQuotaRiskCache;
        this.merchantRateCache=merchantRateCache;
        this.organizationInfoCache=organizationInfoCache;
    }


}
