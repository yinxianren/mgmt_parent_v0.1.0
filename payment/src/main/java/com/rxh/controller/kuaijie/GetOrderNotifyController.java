package com.rxh.controller.kuaijie;


import com.rxh.exception.PayException;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.service.kuaijie.GetOrderNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetOrderNotifyController {


    private final GetOrderNotifyService getOrderNotifyService;
    @Autowired
    public GetOrderNotifyController(GetOrderNotifyService getOrderNotifyService) {
        this.getOrderNotifyService = getOrderNotifyService;
    }
    @RequestMapping(value = "/bankNotify", produces = "application/json;charset=UTF-8")
    public Result bankNotify(@RequestBody BankResult bankResult) throws Exception {
        return getOrderNotifyService.getOrderNotify(bankResult);
    }

//    @RequestMapping(value = "/PayOrderNotify", produces = "application/json;charset=UTF-8")
//    public Result payBankNotify(@RequestBody BankResult bankResult) throws PayException {
//        return getOrderNotifyService.getPayOrderNotify(bankResult);
//    }
//
//
//    @RequestMapping(value = "/transOrderNotify", produces = "application/json;charset=UTF-8")
//    public Result transBankNotify(@RequestBody BankResult bankResult) throws PayException {
//        return getOrderNotifyService.getTransOrderNotify(bankResult);
//    }

}
