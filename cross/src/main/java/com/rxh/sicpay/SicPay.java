package com.rxh.sicpay;

import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/sicPay")
public class SicPay {

    @Autowired
    private SicPayApply sicPayApply;

    @Autowired
    SicPayBindCard sicPayBindCard;

    /**
     * 高汇通支付入口
     * @param trade
     * @return
     */
    @RequestMapping("/pay")
    public BankResult basic (@RequestBody SquareTrade trade) throws Exception {

        String type = trade.getTradeObjectSquare().getInnerType();
        BankResult bankResult = new BankResult();
        //根据不同状态执行相应接口
        switch(type){
            case SystemConstant.PAY_APPLY:
                bankResult = sicPayApply.apply(trade);
                break;
            case SystemConstant.PAY_SMS:
                bankResult = sicPayApply.applySMS(trade);
                break;
            case SystemConstant.PAY_CONFIRM:
                bankResult = sicPayApply.confirmApply(trade);
                break;
            default:
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("收单失败，接口编码错误");
                break;
        }
        return  bankResult;
    }

    @RequestMapping("/bindCard")
    public BankResult merRequest(@RequestBody SquareTrade squareTrade) throws Exception {
        switch (squareTrade.getTradeObjectSquare().getInnerType()){
            case "B001":
                return    sicPayBindCard.bindCard(squareTrade);
            case "B002":
                return  sicPayBindCard.smsRequest(squareTrade);
            case "B003":
                return    sicPayBindCard.smsConfirm(squareTrade);
            default:
                throw new PayException("参数错误,找不到对应类型",1);
        }
    }
}
