package com.rxh.controller.kuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.service.kuaijie.PaymentSquareNotifyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class PaymentSquareNotifyController {
    private final PaymentSquareNotifyService paymentSquareNotifyService;

    public PaymentSquareNotifyController(PaymentSquareNotifyService paymentSquareNotifyService) {
        this.paymentSquareNotifyService = paymentSquareNotifyService;
    }


    @RequestMapping("/squareNotify")
    @ResponseBody
    public Result bankNotify(@RequestBody BankResult bankResult) throws PayException {
        return paymentSquareNotifyService.updateOrderAndDoNotify(bankResult);
    }

}
