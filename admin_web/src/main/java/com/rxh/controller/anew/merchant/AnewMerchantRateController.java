package com.rxh.controller.anew.merchant;

import com.rxh.pojo.Result;
import com.rxh.service.ConstantService;
import com.rxh.service.square.MerchantSquareRateService;
import com.rxh.square.pojo.MerchantRate;
import com.rxh.square.pojo.MerchantSetting;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/merchantSquareRate")
public class AnewMerchantRateController {

    @Autowired
    private MerchantSquareRateService merchantSquareRateService;
    @Autowired
    private ConstantService constantService;

    @RequestMapping("/search")
    public List<MerchantRate> search(@RequestBody MerchantSetting merchantSetting){
        return  merchantSquareRateService.search(merchantSetting.getMerId());
    }

    @RequestMapping("/update")
    public Result update(@RequestBody  List<MerchantRate> param ){
        return merchantSquareRateService.update(param);
    }

    @RequestMapping("/init")
    public Map<String,Object> init(){
        Map<String,Object> init = new HashMap<>();
        init.put("productTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PRODUCTTYPE));
        return init;
    }
}
