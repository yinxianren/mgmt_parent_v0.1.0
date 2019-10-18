package com.rxh.controller.kuaijie;

import com.rxh.controller.AbstractCommonController;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.kuaijie.AlliancePayPamentPayService;
import com.rxh.service.kuaijie.KuaiJiePayPaymentPayService;
import com.rxh.square.pojo.PayOrder;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.ParseTradeInfoSweepCode;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.QueryOrderObjectToMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;

@Controller
@RequestMapping("/kuaijie/pay")
public class KuaiJiePayPaymentPayController extends AbstractCommonController {



    @Autowired
    private KuaiJiePayPaymentPayService kuaiJiePayPaymentPayService;
    @Autowired
    protected AlliancePayPamentPayService alliancePayPamentPayService;
    /**
     * 支付申请(原先：AlliancePayPaymentController 下面的getOrder)
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     */
    @RequestMapping(value = "/payApply", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String payApply(HttpServletRequest req, @RequestBody String tradeInfo)  throws Exception {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        BankResult bankResult=null;
        SquareTrade trade=null;
        TradeObjectSquare tradeObjectSquare=null;
        String result =null;
        Lock lock=null;
        String merId=null;
        try {
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaiJiePayPaymentPayService.allianceGetOrderPay(systemOrderTrack);
            tradeObjectSquare.setInnerType(SystemConstant.PAY_APPLY);
            merId=systemOrderTrack.getMerId();
            lock= merchantsLock.getLock(merId);
            isNull(lock,format("【快捷支付】商户号：%s,获取到商户锁失败",merId),"RXH00013");
            lock.lock();
            trade=kuaiJiePayPaymentPayService.kuaiJieToSaveOrder( systemOrderTrack, tradeObjectSquare);
            bankResult=kuaiJiePayPaymentPayService.kuaiJieToPay(trade);
            trade.getPayOrder().setTradeResult(bankResult.getParam());
            result =  kuaiJiePayPaymentPayService. kuaiJieToUpdataPayInfoAndPayWallet_MQ(trade,  bankResult);
//            if(bankResult.getStatus()==3) {
//                //保存同步结果，推送到MQ做查询
//                PayOrder payOrder = trade.getPayOrder();
//                payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
//                trade.setPayOrder(payOrder);
//                QueryOrderObjectToMQ queryOrderObjectToMQ = kuaijiePayQueryService.getQueryOrderObjectToMQ(trade);
//                transOrderMQ.sendObjectMessageToQueryPayOderMQ(queryOrderObjectToMQ, 20L);
//            }
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
        } catch (Exception e) {
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            if (e instanceof IllegalArgumentException || e instanceof NoSuchElementException) {
                logger.warn("【快捷支付---->收单任务】 提示：{}", e.getMessage());
            } else if(e instanceof PayException){
                logger.warn("【快捷支付---->收单任务】 提示：{}",e.getMessage());
                if(null == result) result = kuaiJiePayPaymentPayService.getReturnJson_1(systemOrderTrack,trade,bankResult,e.getMessage(),((PayException) e).getResultCode());
            }else {
                if(null == result) result = kuaiJiePayPaymentPayService.getReturnJson_1(systemOrderTrack,trade,bankResult,e.getMessage());
                e.printStackTrace();
            }
        } finally {

            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
            if(isNotNull(lock)) lock.unlock();
            return result;
        }
    }

    /**
     * 付款确认
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     */

    @RequestMapping(value = "/confirmPay", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String confirmPay(HttpServletRequest req, @RequestBody String tradeInfo) throws Exception {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        TradeObjectSquare tradeObjectSquare =null;
        SquareTrade squareTrade=null;
        BankResult bankResult=null;
        String result =null;
        Lock lock=null;
        String merId=null;
        try {
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaiJiePayPaymentPayService.alliancePayConfirmPay(systemOrderTrack);
            tradeObjectSquare.setInnerType(SystemConstant.PAY_CONFIRM);

            merId=systemOrderTrack.getMerId();
            lock= merchantsLock.getLock(merId);
            isNull(lock,format("【绑定银行卡】商户号：%s,获取到商户锁失败",merId));
            lock.lock();
            squareTrade=kuaiJiePayPaymentPayService.getConfirmPayParam(systemOrderTrack,tradeObjectSquare);
            systemOrderTrack.setOrderId(squareTrade.getPayOrder().getPayId());
            bankResult=kuaiJiePayPaymentPayService.kuaiJieToPay(squareTrade);
            if (bankResult.getStatus() == (SystemConstant.BANK_STATUS_SUCCESS))
                squareTrade.getPayOrder().setTradeResult(bankResult.getParam());
            result =  kuaiJiePayPaymentPayService.kuaiJieToUpdataPayInfoAndPayWallet_MQ(squareTrade,  bankResult);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
//            String result = alliancePayPamentPayService.toConfirmPay(systemOrderTrack, tradeObjectSquare);
        } catch (Exception e) {
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            if (e instanceof IllegalArgumentException || e instanceof NoSuchElementException) {
                logger.warn("【快捷支付---->收单任务】 提示：{}", e.getMessage());
            } else if(e instanceof PayException){
                logger.warn("【快捷支付---->收单任务】 提示：{}",e.getMessage());
                if(null == result) result = kuaiJiePayPaymentPayService.getReturnJson_1(systemOrderTrack,squareTrade,bankResult,e.getMessage(),((PayException) e).getResultCode());
            } else {
                if(null == result) result = kuaiJiePayPaymentPayService.getReturnJson_1(systemOrderTrack,squareTrade,bankResult,e.getMessage());
                e.printStackTrace();
            }
        }  finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
            if(isNotNull(lock)) lock.unlock();
            return result;
        }
    }
    /**
     *  支付短信重发
     * @return
     *  version 0.0.1
     *    所需参数：商户号（merId）、终端商户号（TerminalMerId）、平台订单号（merOrderId）
     *
     */
    @RequestMapping(value="/confirmSMS", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String confirmSMS(HttpServletRequest req, @RequestBody String tradeInfo) throws Exception {
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        String result = null;
        try{
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSweepCode.alliancePayConfirmSms(systemOrderTrack);
            tradeObjectSquare.setInnerType(SystemConstant.PAY_SMS);
            result = alliancePayPamentPayService.toConfirmSMS(systemOrderTrack, tradeObjectSquare);
            return  result;
        }catch (Exception e){
            systemOrderTrack.setOrderTrackStatus(1);
            String message = e.getMessage() == null ? "null":e.getMessage();
            systemOrderTrack.setTradeResult(message.length()>=500?message.substring(0,499):message);
            e.printStackTrace();
            if(e instanceof PayException){
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

    /**
     *  无验证快捷支付
     * @param req
     * @param tradeInfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/confirmFeePay", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String confirmFeePay(HttpServletRequest req, @RequestBody String tradeInfo) throws Exception {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        TradeObjectSquare tradeObjectSquare =null;
        SquareTrade squareTrade=null;
        BankResult bankResult=null;
        String result =null;
        Lock lock=null;
        String merId=null;
        try {
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaiJiePayPaymentPayService.alliancePayConfirmFeePay(systemOrderTrack);
            tradeObjectSquare.setInnerType("P006");

            merId=systemOrderTrack.getMerId();
            lock= merchantsLock.getLock(merId);
            isNull(lock,format("【免短信支付】商户号：%s,获取到商户锁失败",merId));
            lock.lock();
            squareTrade=kuaiJiePayPaymentPayService.kuaiJieToSaveOrder( systemOrderTrack, tradeObjectSquare);
//            squareTrade=kuaiJiePayPaymentPayService.getConfirmPayParam(systemOrderTrack,tradeObjectSquare);
            systemOrderTrack.setOrderId(squareTrade.getPayOrder().getPayId());
            bankResult=kuaiJiePayPaymentPayService.kuaiJieToFeePay(squareTrade);
            result =  kuaiJiePayPaymentPayService.kuaiJieToUpdataPayInfoAndPayWallet_MQ(squareTrade,  bankResult);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
//            String result = alliancePayPamentPayService.toConfirmPay(systemOrderTrack, tradeObjectSquare);
        } catch (Exception e) {
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            if (e instanceof IllegalArgumentException || e instanceof NoSuchElementException) {
                logger.warn("【快捷支付---->收单任务】 提示：{}", e.getMessage());
            }else if(e instanceof PayException){
                logger.warn("【快捷支付---->收单任务】 提示：{}",e.getMessage());
                if(null == result) result = kuaiJiePayPaymentPayService.getReturnJson_1(systemOrderTrack,squareTrade,bankResult,e.getMessage(),((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = kuaiJiePayPaymentPayService.getReturnJson_1(systemOrderTrack,squareTrade,bankResult,e.getMessage());
                e.printStackTrace();
            }
        }  finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
            if(isNotNull(lock)) lock.unlock();
            return result;
        }
    }

}
