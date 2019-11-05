package com.rxh.controller.anew.merchant;

import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.pojo.Result;
import com.rxh.service.AnewMerchantRateService;
import com.rxh.service.ConstantService;
import com.rxh.service.square.MerchantSquareRateService;
import com.rxh.square.pojo.MerchantRate;
import com.rxh.square.pojo.MerchantSetting;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
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
    private AnewMerchantRateService anewMerchantRateService;

    @RequestMapping("/search")
    public ResponseVO search(@RequestBody MerchantRateTable merchantRateTable){
        return  anewMerchantRateService.getList(merchantRateTable);
    }

    @RequestMapping("/update")
    public ResponseVO update(@RequestBody  List<MerchantRateTable> param ){
        return anewMerchantRateService.batchUpdate(param);
    }

    @RequestMapping("/init")
    public ResponseVO init(){
        return anewMerchantRateService.init();
    }
}
