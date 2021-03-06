package com.rxh.controller.trading;


import com.rxh.service.trading.PayProductDetailService;
import com.rxh.square.pojo.PayProductDetail;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName : PayProductDetailController
 * @Author : zoe
 * @Date : 2019/5/19 15:34
 */
@RestController
@RequestMapping("/payProductController")
public class PayProductDetailController {

    @Resource
    private PayProductDetailService payProductDetailService;


    @RequestMapping(value = "/payProductDetail")
    @ResponseBody
    public PayProductDetail payProductDetail(@RequestBody String payId){
        return payProductDetailService.selectByPrimaryKey(payId);
    }
}
