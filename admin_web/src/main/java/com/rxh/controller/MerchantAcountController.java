package com.rxh.controller;

import com.rxh.pojo.Result;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.square.MerchantAcountService;
import com.rxh.square.pojo.MerchantAcount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchantAcount")
public class MerchantAcountController {

    @Autowired
    private MerchantAcountService merchantAcountService;

    @RequestMapping("/update")
    public Result update(@RequestBody  MerchantAcount merchantAcount){
      return   merchantAcountService.update(merchantAcount);
    }
    @RequestMapping("/search")
    public Result search(@RequestBody MerchantAcount merchantAcount){
            return merchantAcountService.search(merchantAcount.getMerId());
    }
}
