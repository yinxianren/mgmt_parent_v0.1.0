package com.rxh.allinpay;


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
@RequestMapping("/allinPay")
public class Pay {

    @Autowired
    private AllinPayApply allinPayApply;
    @Autowired
    private  AllinPaySMS allinPaySMS;
    @Autowired
    private  AllinPayConfirmPay allinPayConfirmPay;
    @Autowired
    private  AllinPayWithdrawQuery allinPayWithdrawQuery;
    @Autowired
    private AllinPayFreeSMS allinPayFreeSMS;

    @RequestMapping("/pay")
    @ResponseBody
    public BankResult trade(@RequestBody SquareTrade trade) throws UnsupportedEncodingException, PayException, ParseException, InterruptedException {
        switch (trade.getTradeObjectSquare().getInnerType()){
            case "P001":
                return allinPayApply.trade(trade);
            case "P002":
                return allinPaySMS.trade(trade);
            case "P003":
                return allinPayConfirmPay.trade(trade);
            case "P004":
                return allinPayApply.queryOrder(trade);
            case "P005":
                return allinPayWithdrawQuery.trade(trade);
            case "P006":
                return allinPayFreeSMS.trade(trade);
            default:
                BankResult bankResult = new BankResult();
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("参数请求错误");
                return bankResult;
        }

    }
}
