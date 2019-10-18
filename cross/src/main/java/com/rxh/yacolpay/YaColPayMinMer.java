package com.rxh.yacolpay;

import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.utils.SystemConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zoe
 **/
@RestController
@RequestMapping("pay_min")
public class YaColPayMinMer {

    @RequestMapping("/mer")
    public BankResult apply(@RequestBody SquareTrade squareTrade) throws Exception {
        BankResult bankResult = new BankResult();
        YaColPayAuthentication yaColPayAuthentication = new YaColPayAuthentication();
        switch(squareTrade.getTradeObjectSquare().getInnerType()){
            case SystemConstant.INFORMATION_REGISTRATION:
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("商户信息登记成功");
                break;
            case SystemConstant.BANKCARD_REGISTRATION:
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("商户绑定银行卡成功");
                break;
            case SystemConstant.SERVICE_FULFILLMENT:
                bankResult = yaColPayAuthentication.validate(squareTrade);
                break;
            default:
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("进件失败，接口编码错误");
                break;
        }
        return bankResult;
    }
}
