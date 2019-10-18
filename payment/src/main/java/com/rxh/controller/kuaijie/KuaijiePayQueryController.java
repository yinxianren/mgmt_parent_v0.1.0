package com.rxh.controller.kuaijie;

import com.rxh.controller.AbstractCommonController;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantBasicInformationRegistration;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.kuaijie.AlliancePayPamentPayService;
import com.rxh.service.kuaijie.KuaiJiePayPaymentPayService;
import com.rxh.service.kuaijie.KuaijiePayQueryService;
import com.rxh.square.pojo.SystemOrderTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/kuaijie")
public class KuaijiePayQueryController extends AbstractCommonController {


    @Autowired
    private KuaijiePayQueryService kuaijiePayQueryService;
    @Autowired
    private KuaiJiePayPaymentPayService kuaiJiePayPaymentPayService;
    @Autowired
    protected AlliancePayPamentPayService alliancePayPamentPayService;

    @RequestMapping("/queryBalance")
    public String queryBalance(HttpServletRequest req ,@RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        MerchantBasicInformationRegistration merchantBasicInformationRegistration=null;
        String result=null;
        TradeObjectSquare tradeObjectSquare=null;
        try{
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaijiePayQueryService.kuaijieGetOrderPay(systemOrderTrack);
            result=kuaijiePayQueryService.kuaiJieToQueryOrder(systemOrderTrack,tradeObjectSquare);
        }catch (Exception e){

        }finally {
            return result;
        }
    }

    @RequestMapping(value = "/queryOrder",produces = "text/html;charset=UTF-8")
    public String queryOrder(HttpServletRequest req ,@RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        String result=null;
        TradeObjectSquare tradeObjectSquare=null;
        try{
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaijiePayQueryService.kuaijieQueryOrder(systemOrderTrack);
            result=kuaijiePayQueryService.kuaiJieToQueryOrder(systemOrderTrack,tradeObjectSquare);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
        }catch (Exception e){
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            if (e instanceof IllegalArgumentException || e instanceof NoSuchElementException) {
                logger.warn("【快捷支付---->查询任务】 提示：{}", e.getMessage());
            } else if(e instanceof PayException){
                logger.warn("【快捷支付---->收单任务】 提示：{}",e.getMessage());
                if(null == result) result = alliancePayPamentPayService.resultToString1(systemOrderTrack,2,((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = alliancePayPamentPayService.resultToString1(systemOrderTrack,2);
                e.printStackTrace();
            }
        }finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
            return result;
        }
    }

    @RequestMapping(value = "/queryCusInfo",produces = "text/html;charset=UTF-8")
    public String queryFulfillment(HttpServletRequest req ,@RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        String result=null;
        TradeObjectSquare tradeObjectSquare=null;
        try{
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaijiePayQueryService.queryFulfillment(systemOrderTrack);
            result=kuaijiePayQueryService.kuaiJieToQueryFulfillment(systemOrderTrack,tradeObjectSquare);
        }catch (Exception e){
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            if (e instanceof IllegalArgumentException || e instanceof NoSuchElementException) {
                logger.warn("【业务开通---->查询任务】 提示：{}", e.getMessage());
            } else if(e instanceof PayException){
                logger.warn("【业务开通---->查询任务】 提示：{}",e.getMessage());
                if(null == result) result = alliancePayPamentPayService.resultToString1(systemOrderTrack,2,((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = alliancePayPamentPayService.resultToString1(systemOrderTrack,2);
                e.printStackTrace();
            }
        }finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
            return result;
        }
    }

    @RequestMapping(value = "/queryBondCard",produces = "text/html;charset=UTF-8")
    public String queryBindCard(HttpServletRequest req ,@RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        String result=null;
        TradeObjectSquare tradeObjectSquare=null;
        try{
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaijiePayQueryService.queryBindCard(systemOrderTrack);
            result=kuaijiePayQueryService.kuaiJieToQueryBindCard(systemOrderTrack,tradeObjectSquare);
        }catch (Exception e){
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            if (e instanceof IllegalArgumentException || e instanceof NoSuchElementException) {
                logger.warn("【绑卡业务---->查询任务】 提示：{}", e.getMessage());
            } else if(e instanceof PayException){
                logger.warn("【绑卡业务---->查询任务】 提示：{}",e.getMessage());
                if(null == result) result = alliancePayPamentPayService.resultToString1(systemOrderTrack,2,((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = alliancePayPamentPayService.resultToString1(systemOrderTrack,2);
                e.printStackTrace();
            }
        }finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
            return result;
        }
    }


}
