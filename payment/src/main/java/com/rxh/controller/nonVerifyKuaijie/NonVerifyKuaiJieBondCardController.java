package com.rxh.controller.nonVerifyKuaijie;

import com.alibaba.fastjson.JSONObject;
import com.rxh.controller.AbstractCommonController;
import com.rxh.controller.IBondCardControll;
import com.rxh.exception.PayException;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.nonVerifyKuaijie.NonVerifyKuaiJieBondCardService;
import com.rxh.square.pojo.MerchantCard;
import com.rxh.square.pojo.SystemOrderTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *  描述：绑卡相关操作
 * @author  panda
 * @date 20190729
 */


@RestController
@RequestMapping("/nvkuaijie")
public class NonVerifyKuaiJieBondCardController extends AbstractCommonController implements INonVerifyKuaiJieController , IBondCardControll {


    @Autowired
    private NonVerifyKuaiJieBondCardService bondCardService;

    /**
     *    无验证快捷绑卡
     * @param req
     * @param tradeInfo
     * @return
     */
    @RequestMapping(value = "/bondCard", produces = "text/html;charset=UTF-8")
    public  String bondCardOperator(HttpServletRequest req, @RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        String result=null;
        TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
        SquareTrade squareTrade=null;
        BankResult bankResult=null;
        try{
            systemOrderTrack =getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = bondCardService.verifyParam(systemOrderTrack,bondCardService.alliancePayBondCardValidate);
            tradeObjectSquare.setBizType("6");
            squareTrade=bondCardService.getBondCardParam(systemOrderTrack,tradeObjectSquare);
            bankResult=bondCardService.doPostOnCross(squareTrade);
            MerchantCard merchantCard=settingBondCardParam(squareTrade,bankResult);
            bondCardService.updateBondStatus(merchantCard);
            result=bondCardService.settingBondCardReturnJson(squareTrade,bankResult);
            systemOrderTrack.setTradeResult( result.length()>=500 ? result.substring(0,499) : result );
        }catch (Exception e){
            systemOrderTrack.setOrderTrackStatus(1);
            systemOrderTrack.setTradeResult( e.getMessage().length() >=500 ? e.getMessage().substring(0,499) :  e.getMessage());
            if(null == result) result = bondCardService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage());
            if(e instanceof PayException) logger.warn("【无验证快捷绑卡】 提示：{}",e.getMessage());
            else e.printStackTrace();
        } finally {
            if (systemOrderTrack != null)  recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            return result;
        }
    }

    @RequestMapping(value = "/bondCardNotify", produces = "text/html;charset=UTF-8")
    public String notify(@RequestBody BankResult bankResult) throws Exception {
        logger.info("绑卡异步返回结果："+ JSONObject.toJSONString(bankResult));
        Result result = bondCardService.bondCardNotify(bankResult);
        return JSONObject.toJSONString(result);
    }



}
