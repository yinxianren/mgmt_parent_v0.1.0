//package com.rxh.controller.trading;
//
//import com.rxh.service.trading.TransProductDetailService;
//import com.rxh.square.pojo.TransBankInfo;
//import com.rxh.square.pojo.TransProductDetail;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
///**
// * @ClassName : TransProductDetailController
// * @Author : zoe
// * @Date : 2019/5/19 16:36
// */
//@RestController
//public class TransProductDetailController {
//    @Resource
//    private TransProductDetailService transProductDetailService;
//
//
//    @RequestMapping(value = "/transProductDetail")
//    @ResponseBody
//    public TransProductDetail transProductDetail(@RequestBody String payId){
//        return transProductDetailService.selectByPrimaryKey(payId);
//    }
//}
