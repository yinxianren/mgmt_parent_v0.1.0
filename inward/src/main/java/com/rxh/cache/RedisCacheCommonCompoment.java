package com.rxh.cache;

import com.rxh.cache.redis.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheCommonCompoment {


    public final MerchantCardCache merchantCardCache;
    public final MerchantInfoCache merchantInfoCache;
    public final MerchantSettingCache merchantSettingCache;
    public final ChannelInfoCache channelInfoCache;
    public final ExtraChannelInfoCache extraChannelInfoCache;
    public final MerchantRegisterCollectCache merchantRegisterCollectCache;
    public final MerchantRegisterInfoCache merchantRegisterInfoCache;
    public final MerchantRateCache merchantRateCache;
    public final AgentMerchantSettingCahce agentMerchantSettingCahce;
    public final MerchantQuotaRiskCache merchantQuotaRiskCache;
    public final OrganizationInfoCache organizationInfoCache;

    @Autowired
    public RedisCacheCommonCompoment(MerchantCardCache merchantCardCache
            ,MerchantInfoCache merchantInfoCache
            ,MerchantSettingCache merchantSettingCache
            ,ChannelInfoCache channelInfoCache
            ,ExtraChannelInfoCache extraChannelInfoCache
            ,MerchantRegisterCollectCache merchantRegisterCollectCache
            ,MerchantRegisterInfoCache merchantRegisterInfoCache
            ,MerchantRateCache merchantRateCache
            ,AgentMerchantSettingCahce agentMerchantSettingCahce
            ,MerchantQuotaRiskCache merchantQuotaRiskCache
            ,OrganizationInfoCache organizationInfoCache
    ){
        this.merchantCardCache=merchantCardCache;
        this.merchantInfoCache=merchantInfoCache;
        this.merchantSettingCache=merchantSettingCache;
        this.channelInfoCache=channelInfoCache;
        this.extraChannelInfoCache=extraChannelInfoCache;
        this.merchantRegisterCollectCache=merchantRegisterCollectCache;
        this.merchantRegisterInfoCache=merchantRegisterInfoCache;
        this.merchantRateCache=merchantRateCache;
        this.agentMerchantSettingCahce=agentMerchantSettingCahce;
        this.merchantQuotaRiskCache=merchantQuotaRiskCache;
        this.organizationInfoCache=organizationInfoCache;
    }


}
