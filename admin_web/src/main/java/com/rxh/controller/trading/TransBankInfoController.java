//package com.rxh.controller.trading;
//
//import com.rxh.service.trading.TransBankInfoService;
//import com.rxh.square.pojo.PayProductDetail;
//import com.rxh.square.pojo.TransBankInfo;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
///**
// * @ClassName : TransBankInfoController
// * @Author : zoe
// * @Date : 2019/5/19 16:24
// */
//@RestController
//public class TransBankInfoController {
//
//    @Resource
//    private TransBankInfoService transBankInfoService;
//
//    @RequestMapping(value = "/transBankInfo")
//    @ResponseBody
//    public TransBankInfo transBankInfo(@RequestBody String payId){
//        return transBankInfoService.selectByPrimaryKey(payId);
//    }
//}
