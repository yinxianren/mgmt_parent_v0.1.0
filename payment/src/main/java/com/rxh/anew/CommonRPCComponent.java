package com.rxh.anew;

import com.rxh.service.anew.agent.ApiAgentMerchantInfoService;
import com.rxh.service.anew.agent.ApiAgentMerchantSettingService;
import com.rxh.service.anew.agent.ApiAgentMerchantWalletService;
import com.rxh.service.anew.agent.ApiAgentMerchantsDetailsService;
import com.rxh.service.anew.business.*;
import com.rxh.service.anew.channel.*;
import com.rxh.service.anew.merchant.*;
import com.rxh.service.anew.system.*;
import com.rxh.service.anew.terminal.ApiTerminalMerchantsDetailsService;
import com.rxh.service.anew.terminal.ApiTerminalMerchantsWalletService;
import com.rxh.service.anew.transaction.ApiPayOrderBusinessTransactionService;
import com.rxh.service.anew.transaction.ApiTransOrderBusinessTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/21
 * Time: 上午9:06
 * Description:
 */

@Component
@AllArgsConstructor
public  class CommonRPCComponent {
    public final ApiBankRateService           apiBankRateService;
    public final ApiBankCodeService           apiBankCodeService;
    public final ApiRiskQuotaService          apiRiskQuotaService;
    public final ApiChannelInfoService        apiChannelInfoService;
    public final ApiMerchantInfoService       apiMerchantInfoService;
    public final ApiMerchantCardService       apiMerchantCardService;
    public final ApiPayOrderInfoService       apiPayOrderInfoService;
    public final ApiRegisterInfoService       apiRegisterInfoService;
    public final ApiMerchantRateService       apiMerchantRateService;
    public final ApiChannelWalletService      apiChannelWalletService;
    public final ApiChannelDetailsService     apiChannelDetailsService;
    public final ApiTransOrderInfoService     apiTransOrderInfoService;
    public final ApiMerchantWalletService     apiMerchantWalletService;
    public final ApiChannelHistoryService     apiChannelHistoryService;
    public final ApiRegisterCollectService    apiRegisterCollectService;
    public final ApiMerchantSettingService    apiMerchantSettingService;
    public final ApiChannelExtraInfoService   apiChannelExtraInfoService;
    public final ApiSystemOrderTrackService   apiSystemOrderTrackService;
    public final ApiOrganizationInfoService   apiOrganizationInfoService;
    public final ApiMerchantsDetailsService   apiMerchantsDetailsService;
    public final ApiMerchantQuotaRiskService  apiMerchantQuotaRiskService;
    public final ApiAgentMerchantInfoService  apiAgentMerchantInfoService;
    public final ApiProductTypeSettingService apiProductTypeSettingService;
    public final ApiAgentMerchantWalletService      apiAgentMerchantWalletService;
    public final ApiAgentMerchantSettingService     apiAgentMerchantSettingService;
    public final ApiAgentMerchantsDetailsService    apiAgentMerchantsDetailsService;
    public final ApiTerminalMerchantsWalletService  apiTerminalMerchantsWalletService;
    public final ApiTerminalMerchantsDetailsService apiTerminalMerchantsDetailsService;
    public final ApiPayOrderBusinessTransactionService   apiPayOrderBusinessTransactionService;
    public final ApiTransOrderBusinessTransactionService apiTransOrderBusinessTransactionService;
}
