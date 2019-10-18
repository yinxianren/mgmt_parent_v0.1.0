package com.rxh.controller;

import com.rxh.pojo.Result;
import com.rxh.service.square.MerchantSquareRateService;
import com.rxh.square.pojo.MerchantRate;
import com.rxh.square.pojo.MerchantSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/merchantSquareRate")
public class MerchantSquareRateController {

    @Autowired
    private MerchantSquareRateService merchantSquareRateService;

    @RequestMapping("/search")
    public List<MerchantRate> search(@RequestBody MerchantSetting merchantSetting){

        List<MerchantRate> result = merchantSquareRateService.search(merchantSetting.getMerId());

        return result;

    }
    @RequestMapping("/merSearch")
    public List<MerchantRate> merSearch(String merId){

        List<MerchantRate> result = merchantSquareRateService.merSearch(merId);

        return result;

    }


    @RequestMapping("/update")
    public Result update(@RequestBody  List<MerchantRate> param ){


        Result result = merchantSquareRateService.update(param);

        return result;

    }
}
