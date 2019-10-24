package com.rxh.anew;

import com.rxh.service.anew.business.ApiMerchantCardService;
import com.rxh.service.anew.business.ApiRegisterCollectService;
import com.rxh.service.anew.business.ApiRegisterInfoService;
import com.rxh.service.anew.channel.ApiChannelExtraInfoService;
import com.rxh.service.anew.channel.ApiChannelInfoService;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import com.rxh.service.anew.merchant.ApiMerchantInfoService;
import com.rxh.service.anew.system.ApiMerchantSettingService;
import com.rxh.service.anew.system.ApiSystemOrderTrackService;
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
}
