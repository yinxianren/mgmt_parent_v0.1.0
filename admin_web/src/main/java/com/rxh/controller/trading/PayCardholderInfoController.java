//package com.rxh.controller.trading;
//
//import com.rxh.service.trading.PayCardholderInfoService;
//import com.rxh.square.pojo.PayCardholderInfo;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
///**
// * @ClassName : PayCardholderInfoController
// * @Author : zoe
// * @Date : 2019/5/19 15:45
// */
//@RestController
//public class PayCardholderInfoController {
//
//    @Resource
//    private PayCardholderInfoService payCardholderInfoService;
//
//    @RequestMapping(value = "/payCardholderInfo")
//    @ResponseBody
//    public PayCardholderInfo payCardholderInfo(@RequestBody String payId){
//        return payCardholderInfoService.selectByPrimaryKey(payId);
//    }
//
//}
