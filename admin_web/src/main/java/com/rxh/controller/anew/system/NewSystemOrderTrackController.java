package com.rxh.controller.anew.system;

import com.rxh.pojo.base.Page;
import com.rxh.service.merchant.AnewMerchantInfoService;
import com.rxh.service.system.NewSystemOrderTrackService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/systemOrderTrack")
public class NewSystemOrderTrackController {

    @Autowired
    private AnewMerchantInfoService merchantInfoService;
    @Autowired
    private NewSystemOrderTrackService newSystemOrderTrackService;

    @SystemLogInfo(description = "订单信息追踪查询")
    @RequestMapping(value="/findSystemOrder")
    public ResponseVO findSystemOrder(@RequestBody Page page ) {
        return newSystemOrderTrackService.page(page);
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("merchants", merchantInfoService.getMerchants(null).getData());
        return init;
    }
}
