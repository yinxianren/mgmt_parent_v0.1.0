package com.rxh.creditPay;


import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@Controller
@RequestMapping("/creditPay")
public class CreditPay {

    @Autowired
    private CreditPayBindCard creditPayBindCard;
    @Autowired
    private CreditPayConsume creditPayConsume;
    @Autowired
    private CreditPayConsumeQuery creditPayConsumeQuery;


    @RequestMapping("/pay")
    @ResponseBody
    public BankResult trade(@RequestBody SquareTrade trade) throws UnsupportedEncodingException, PayException, ParseException, InterruptedException,Exception {
        switch (trade.getTradeObjectSquare().getInnerType()){
            //绑卡
            case "C001":
                return creditPayBindCard.trade(trade);
            //消费
            case "C002":
                return creditPayConsume.trade(trade);
            case "C003":
                return creditPayConsumeQuery.trade(trade);
            default:
                BankResult bankResult = new BankResult();
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("参数请求错误");
                return bankResult;
        }

    }
}
