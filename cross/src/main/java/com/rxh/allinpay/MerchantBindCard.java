package com.rxh.allinpay;


import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/merchantBindCard")
public class MerchantBindCard {

    AllinPayBindCard allinPayBindCard = new AllinPayBindCard();

    @PostMapping("/bindCard")
    @ResponseBody
    public BankResult merRequest(@RequestBody SquareTrade squareTrade) throws UnsupportedEncodingException, PayException {
        switch (squareTrade.getTradeObjectSquare().getInnerType()){
            case "B001":
             return    allinPayBindCard.bondCard(squareTrade);
            case "B002":
                return  allinPayBindCard.reGetCode(squareTrade);
            case "B003":
                return    allinPayBindCard.confirmBond(squareTrade);
            default:
                throw new PayException("参数错误,找不到对应类型",1);
        }
    }
}
