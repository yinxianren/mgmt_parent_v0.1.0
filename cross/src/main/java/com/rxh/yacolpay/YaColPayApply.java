package com.rxh.yacolpay;

import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.utils.SystemConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yacolPay")
public class YaColPayApply {

    @RequestMapping("/apply")
    public  BankResult apply(@RequestBody SquareTrade squareTrade) throws Exception {
        String type = squareTrade.getTradeObjectSquare().getInnerType();
        BankResult bankResult = new BankResult();
        YacolPayFastPay yacolPayFastPay = new YacolPayFastPay();
        switch(type){
            case SystemConstant.PAY_APPLY:
                bankResult = yacolPayFastPay.bankCardPay(squareTrade);
                break;
            case SystemConstant.PAY_SMS:
                bankResult = yacolPayFastPay.paySMS(squareTrade);
                break;
            case SystemConstant.PAY_CONFIRM:
                bankResult = yacolPayFastPay.advancePay(squareTrade);
                break;
            default:
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("支付失败，接口编码错误");
                break;
        }
        return bankResult;
    }


}
