package com.rxh.controller;

import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.rxh.spring.annotation.SystemLogInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : MerchantChannelHistoryController
 * @Author : zoe
 * @Date : 2019/6/17 20:04
 */
@RestController
@RequestMapping("/merchantChannelHistory")
public class MerchantChannelHistoryController {

    @SystemLogInfo(description = "通道运行监控查询")
    @RequestMapping(value="/findMerchantChannel")
    public ResponseVO findMerchantChannel(@RequestBody Page page ) {
        ResponseVO pageResult = new ResponseVO();
        return pageResult;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        /*init.put("id", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.sysId));
        init.put("merchants", merchantInfoService.getIdsAndName());
        init.put("channels", channelWalletService.getIdsAndName());
        init.put("organizations",organizationService .getIdsAndName());
        // init.put("channelTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.EXTRATYPE));
        init.put("payTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("statusList", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));*/

        return init;
    }

}
