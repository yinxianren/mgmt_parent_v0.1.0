package com.rxh.controller;

import com.rxh.pojo.Result;
import com.rxh.service.ConstantService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.service.square.MerchantSquareSettingService;
import com.rxh.service.square.OrganizationService;
import com.rxh.square.pojo.MerchantSetting;
import com.internal.playment.common.enums.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
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

    @RequestMapping("/search")
    public Result search(@RequestBody MerchantSetting merchantSetting){
        Result result = merhantSquareSettingService.search(merchantSetting.getMerId());
        return result;
    }

    @RequestMapping("/merSearch")
    public Result merSearch(@RequestBody String merId){
        Result result = merhantSquareSettingService.merSearch(merId);
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
    @RequestMapping("/getAll")
    @ResponseBody
    public List<MerchantSetting> getAll() {
        List<MerchantSetting> all = merhantSquareSettingService.getAll();
        return all;
    }
}
