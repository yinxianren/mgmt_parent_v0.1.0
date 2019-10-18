package com.rxh.controller;

import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.MerchantCard;
import com.rxh.utils.SystemConstant;

import java.util.List;

/**
 *   描述：办卡默认方法
 * @author  panda
 * @date  20190729
 *
 */
public interface IBondCardControll {

    /**
     *
      * @param trade
     * @param result
     * @return
     */
   default MerchantCard settingBondCardParam(SquareTrade trade, BankResult result) {
        MerchantCard merchantCard = trade.getMerchantCard();
        if (merchantCard.getStatus() != SystemConstant.BANK_STATUS_SUCCESS){
            merchantCard.setStatus(Integer.valueOf(result.getStatus()));
        }
        merchantCard.setResult(result.getBankData());
        merchantCard.setBackData(result.getParam());
       return merchantCard;
    }

}
