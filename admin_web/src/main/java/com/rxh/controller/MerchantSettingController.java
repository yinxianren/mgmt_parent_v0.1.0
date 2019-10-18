package com.rxh.controller;

import com.rxh.pojo.Result;
import com.rxh.service.ConstantService;
import com.rxh.service.square.*;
import com.rxh.square.pojo.MerchantSetting;
import com.rxh.utils.SystemConstant;
import com.rxh.cache.RedisCacheCommonCompoment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/merchantSetting")
public class MerchantSettingController {

    @Autowired
    private MerchantSquareSettingService merhantSquareSettingService;
    @Autowired
    private ConstantService constantService;
    @Autowired
    private ChannelWalletService channelWalletService;
    @Autowired
    private AgentMerchantInfoService agentMerchantInfoService;
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RedisCacheCommonCompoment redisCacheCommonCompoment;

    @RequestMapping("/search")
    public Result search(@RequestBody MerchantSetting merchantSetting){
         //modify by gjm 添加redis  at 20190807 start
        Result<MerchantSetting> result = new Result<>();
        MerchantSetting merchantSettingCache = redisCacheCommonCompoment.merchantSettingCache.getOne(merchantSetting.getMerId());
        if(null != merchantSettingCache){
            result.setCode(Result.SUCCESS);
            result.setMsg("获取商户配置成功");
            result.setData(merchantSettingCache);
        }else {
            result = merhantSquareSettingService.search(merchantSetting.getMerId());
        }
         //modify by gjm 添加redis  at 20190807 end
        return result;
    }

    @RequestMapping("/update")
    public Result update(@RequestBody  MerchantSetting merchantSetting){
        return merhantSquareSettingService.update(merchantSetting);
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchantType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.MERCHANTTYPE));
        init.put("channelLevel", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.channelLevel));
        init.put("payType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("agentMerchants", agentMerchantInfoService.getAllIdAndName());
        init.put("organizations", organizationService.getIdsAndName());
        init.put("channels", channelWalletService.getIdsAndName());
        return init;
    }
}
