package com.rxh.controller.oldKuaijie;


import com.rxh.exception.PayException;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.service.oldKuaijie.SweepCodePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SweepCodeNotifyController {


    private final SweepCodePaymentService sweepCodePaymentService;
    @Autowired
    public SweepCodeNotifyController(SweepCodePaymentService sweepCodePaymentService) {
        this.sweepCodePaymentService = sweepCodePaymentService;
    }

    @RequestMapping(value = "/sweepCodeNotify", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Result bankNotify(@RequestBody BankResult bankResult) throws PayException {
        return sweepCodePaymentService.updateOrderAndDoNotify(bankResult);
    }




}
