//package com.rxh.controller;
//
//import com.internal.playment.common.page.Page;
//import com.rxh.pojo.base.PageResult;
//import com.rxh.service.square.FinanceDrawingService;
//import com.rxh.spring.annotation.SystemLogInfo;
//import com.rxh.util.UserInfoUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.rxh.square.pojo.FinanceDrawing;
//
//import java.util.Date;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/financeDrawing")
//public class financeDrawingController {
//
//    @Autowired
//    private FinanceDrawingService financeDrawingService;
//
//    @SystemLogInfo(description = "提现查询")
//    @RequestMapping("/search")
//    @ResponseBody
//    public PageResult search(@RequestBody Page page ) {
//        PageResult pageResult = financeDrawingService.findByPage(page);
//        return pageResult;
//    }
//    @RequestMapping("/update")
//    @ResponseBody
//    public Boolean update(@RequestBody FinanceDrawing param ) {
//        param.setTransferer(UserInfoUtils.getName());
//        param.setTransferTime(new Date());
//        Boolean flag = financeDrawingService.update(param);
//        return flag;
//    }
//    @RequestMapping("/drawMoney")
//    @ResponseBody
//    public Boolean drawMoney(@RequestBody FinanceDrawing param ) {
//        param.setTransferer(UserInfoUtils.getName());
//        Boolean flag = financeDrawingService.drawMoney(param);
//        return flag;
//    }
//
//    @RequestMapping(value = "/getBalanceInit")
//    @ResponseBody
//    public Map<String, Object> getBalanceInit() {
//        return financeDrawingService.getBalanceChangeInit();
//    }
//
//}
