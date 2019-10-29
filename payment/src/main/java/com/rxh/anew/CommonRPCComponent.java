package com.rxh.anew;

import com.rxh.service.anew.agent.ApiAgentMerchantInfoService;
import com.rxh.service.anew.agent.ApiAgentMerchantSettingService;
import com.rxh.service.anew.business.ApiMerchantCardService;
import com.rxh.service.anew.business.ApiPayOrderInfoService;
import com.rxh.service.anew.business.ApiRegisterCollectService;
import com.rxh.service.anew.business.ApiRegisterInfoService;
import com.rxh.service.anew.channel.ApiChannelExtraInfoService;
import com.rxh.service.anew.channel.ApiChannelHistoryService;
import com.rxh.service.anew.channel.ApiChannelInfoService;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import com.rxh.service.anew.merchant.*;
import com.rxh.service.anew.system.ApiMerchantSettingService;
import com.rxh.service.anew.system.ApiRiskQuotaService;
import com.rxh.service.anew.system.ApiSystemOrderTrackService;
import com.rxh.service.anew.terminal.ApiTerminalMerchantsDetailsService;
import com.rxh.service.anew.terminal.ApiTerminalMerchantsWalletService;
import com.rxh.service.anew.transaction.ApiPayOrderBusinessTransactionService;
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
    public final ApiMerchantInfoService       apiMerchantInfoService;
    public final ApiRegisterCollectService    apiRegisterCollectService;
    public final ApiMerchantSettingService    apiMerchantSettingService;
    public final ApiChannelInfoService        apiChannelInfoService;
    public final ApiProductTypeSettingService apiProductTypeSettingService;
    public final ApiChannelExtraInfoService   apiChannelExtraInfoService;
    public final ApiRegisterInfoService       apiRegisterInfoService;
    public final ApiSystemOrderTrackService   apiSystemOrderTrackService;
    public final ApiMerchantCardService       apiMerchantCardService;
    public final ApiPayOrderInfoService       apiPayOrderInfoService;
    public final ApiChannelHistoryService     apiChannelHistoryService;
    public final ApiMerchantQuotaRiskService  apiMerchantQuotaRiskService;
    public final ApiRiskQuotaService          apiRiskQuotaService;
    public final ApiAgentMerchantInfoService  apiAgentMerchantInfoService;
    public final ApiAgentMerchantSettingService apiAgentMerchantSettingService;
    public final ApiMerchantRateService       apiMerchantRateService;
    public final ApiPayOrderBusinessTransactionService apiPayOrderBusinessTransactionService;
    public final ApiMerchantWalletService     apiMerchantWalletService;
    public final ApiMerchantsDetailsService   apiMerchantsDetailsService;
    public final ApiTerminalMerchantsWalletService apiTerminalMerchantsWalletService;
    public final ApiTerminalMerchantsDetailsService apiTerminalMerchantsDetailsService;
}
