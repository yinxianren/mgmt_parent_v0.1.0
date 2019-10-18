package com.rxh.service.oldKuaijie;

import com.rxh.activeMQ.ConsumerMessageListener;
import com.rxh.exception.PayException;
import com.rxh.i18.I18Component;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.GetOrderPayNotify;
import com.rxh.pojo.payment.NoCardPayNotify;
import com.rxh.pojo.payment.SweepCodeNotify;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.*;
import com.rxh.utils.CheckMd5Utils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.SystemConstant;
import org.apache.activemq.ScheduledMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.ObjectMessage;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Service
public class NoVerifyGetOrderNotifyService {

    public final static String SUCCEED = "SUCCEED";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JmsTemplate queueJmsTemplate;
    private final I18Component i18Component;
    private final RecordPaymentSquareService recordPaymentSquareService;
    private final GetOrderWalletService getOrderWalletService;
    private final SweepCodePaymentService sweepCodePaymentService;


    @Autowired
    public NoVerifyGetOrderNotifyService(JmsTemplate queueJmsTemplate, I18Component i18Component, RecordPaymentSquareService recordPaymentSquareService, GetOrderWalletService getOrderWalletService, SweepCodePaymentService sweepCodePaymentService) {
        this.queueJmsTemplate = queueJmsTemplate;
        this.i18Component = i18Component;
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.getOrderWalletService = getOrderWalletService;
        this.sweepCodePaymentService = sweepCodePaymentService;
    }

    /**
     * 发送商户通知
     *
     * @param order         平台订单对象
     * @param bankResult    银行结果对象
     */
    void sendMerchantNotify(PayOrder order, MerchantInfo merchantInfo, BankResult bankResult, SystemOrderTrack systemOrderTrack)   {

        try {

            GetOrderPayNotify merchantNotify = new GetOrderPayNotify();
            TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
//            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSquare.parse(systemOrderTrack);
            merchantNotify.setReturnJson(getReturnJson(order,merchantInfo,bankResult,tradeObjectSquare));
            merchantNotify.setNotifyUrl(systemOrderTrack.getNoticeUrl());
            merchantNotify.setNotifyCycle(3 * SystemConstant.SECOND_TO_MILLISECOND);
            merchantNotify.setNotifyTimes(4);
            // 将通知商户信息添加到消息队列
            queueJmsTemplate.send(ConsumerMessageListener.GETORDEY_NOTIFY, session -> {
                ObjectMessage objectMessage = session.createObjectMessage(merchantNotify);
                // 重复投递次数3次
                objectMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 3);
                // 重复投递时间间隔
//                objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 3 * SystemConstant.HOUR_TO_MILLISECOND);
                // 5秒后发送异步通知，减少异步通知比同步通知快的情况。
//                objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5000);
                return objectMessage;
            });
        } catch (Exception e) {
            logger.error("添加商户通知至消息队列失败！", e);
        }
    }







    private String getReturnJson(PayOrder order, MerchantInfo merchantInfo, BankResult bankResult,  TradeObjectSquare tradeObjectSquare) {

        String merId = merchantInfo.getMerId();
        String merOrderId = order.getMerOrderId();
        String amount = order.getAmount().toString();
        Short status = bankResult.getStatus();
        Map<String, Object> resultMap = new TreeMap<>();
        resultMap.put("merId",merId);
        resultMap.put("merOrderId",merOrderId);
        resultMap.put("orderId;",order.getPayId());
        resultMap.put("terminalMerId",order.getTerminalMerId());
        resultMap.put("amount;",amount);
        resultMap.put("status;",status);
        resultMap.put("msg;",bankResult.getBankResult());
        resultMap.put("signMsg;", CheckMd5Utils.getMd5Sign(resultMap));
        String result  = JsonUtils.objectToJson(resultMap);




        return result;




    }

    private String getNoCardPayReturnJson(PayOrder order, MerchantInfo merchantInfo, BankResult bankResult,  TradeObjectSquare tradeObjectSquare) {

        String merId = merchantInfo.getMerId();
        String merOrderId = order.getMerOrderId();
        String amount = order.getAmount().toString();
        Short status = bankResult.getStatus();
        String secretKey = merchantInfo.getSecretKey();
        String md5Str=merId+merOrderId+amount+order+status+secretKey;
        md5Str=DigestUtils.md5Hex(md5Str);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("MerId",merId);
        resultMap.put("MerOrderId",merOrderId);
        resultMap.put("TradeTime;",order.getTradeTime());
        resultMap.put("Amount;",amount);
        resultMap.put("Status;",status);
        resultMap.put("Msg;",bankResult.getBankResult());
        resultMap.put("SignMsg;",md5Str);

        String result  = JsonUtils.objectToJson(resultMap);



        return result;




    }






    /**
     * 重发商户通知
     *
     * @param merchantNotify 商户通知对象
     */
    public void resendMerchantNotify(SweepCodeNotify merchantNotify) {
        queueJmsTemplate.send(ConsumerMessageListener.MERCHANT_NOTIFY, session -> {
            ObjectMessage objectMessage = session.createObjectMessage(merchantNotify);
            // 重复投递时间间隔
            objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, merchantNotify.getNotifyCycle());
            return objectMessage;
        });
    }

    /**
     * 重发商户通知
     *
     * @param merchantNotify 商户通知对象
     */
    public void resendNoCardPayNotify(NoCardPayNotify merchantNotify) {
        queueJmsTemplate.send(ConsumerMessageListener.MERCHANT_NOTIFY, session -> {
            ObjectMessage objectMessage = session.createObjectMessage(merchantNotify);
            // 重复投递时间间隔
            objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, merchantNotify.getNotifyCycle());
            return objectMessage;
        });
    }

    public Result getOrderNotify(BankResult bankResult) throws PayException {
        Long orderId = bankResult.getOrderId();
        PayOrder payOrder= recordPaymentSquareService.getPayOrderById(orderId.toString());
        ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId().toString());
        MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(payOrder.getMerId());
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(payOrder.getMerId());
        SystemOrderTrack systemOrderTrack= recordPaymentSquareService.getSystemOrderTrack(payOrder.getPayId());
        recordPaymentSquareService.UpdatePayOrder(payOrder);
        if(!Objects.equals(payOrder.getOrderStatus(), Integer.valueOf(bankResult.getStatus()))&&bankResult.getStatus()==0){
            getOrderWalletService.notifyUpdateWallet(bankResult);
            sweepCodePaymentService.updateRiskQuotaData(payOrder,channelInfo,merchantQuotaRisk);
        }
        //订单状态为成功 异步通知不为成功
        if(payOrder.getOrderStatus()==0&&bankResult.getStatus()!=0){
            getOrderWalletService.notifyRevertWallet(bankResult);
        }
        payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
//        if (bankResult.getRealAmount()!=null){
//            payOrder.setRealAmount(bankResult.getRealAmount());
//        }
        payOrder.setOrderStatus(Integer.valueOf(bankResult.getStatus()));
        recordPaymentSquareService.UpdatePayOrder(payOrder);
      /*  PayOrder payOrder = new PayOrder();
        MerchantInfo merchantInfo = new MerchantInfo();
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();*/

        sendMerchantNotify(payOrder,merchantInfo,bankResult,systemOrderTrack);
        return new Result(Result.SUCCESS, "订单更新完成！");

    }
}
