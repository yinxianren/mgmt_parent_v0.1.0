package com.rxh.service.oldKuaijie;

import com.rxh.activeMQ.ConsumerMessageListener;
import com.rxh.i18.I18Component;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.GetOrderPayNotify;
import com.rxh.pojo.payment.NoCardPayNotify;
import com.rxh.pojo.payment.SweepCodeNotify;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.PayOrder;
import com.rxh.square.pojo.SystemOrderTrack;
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


@Service
public class SweepCodeNotifyService {

    public final static String SUCCEED = "SUCCEED";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JmsTemplate queueJmsTemplate;
    private final I18Component i18Component;


    @Autowired
    public SweepCodeNotifyService(JmsTemplate queueJmsTemplate, I18Component i18Component) {
        this.queueJmsTemplate = queueJmsTemplate;
        this.i18Component = i18Component;
    }

    /**
     * 发送商户通知
     *
     * @param order         平台订单对象
     * @param bankResult    银行结果对象
     */
    void sendMerchantNotify(PayOrder order, MerchantInfo merchantInfo, BankResult bankResult, SystemOrderTrack systemOrderTrack)   {

        try {

            SweepCodeNotify     merchantNotify = new SweepCodeNotify();
            TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
//            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSquare.parse(systemOrderTrack);
            merchantNotify.setReturnJson(getReturnJson(order,merchantInfo,bankResult,tradeObjectSquare));
//            merchantNotify.setNotifyUrl(tradeObjectSquare.getNoticeUrl());
            merchantNotify.setNotifyUrl("http://localhost:8102/getMsg");
            merchantNotify.setNotifyCycle(3 * SystemConstant.SECOND_TO_MILLISECOND);
            merchantNotify.setNotifyTimes(4);
            // 将通知商户信息添加到消息队列
            queueJmsTemplate.send(ConsumerMessageListener.MERCHANT_NOTIFY, session -> {
                ObjectMessage objectMessage = session.createObjectMessage(merchantNotify);
                // 重复投递次数3次
                // mapMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 3);
                // 重复投递时间间隔
                // mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 3 * SystemConstant.HOUR_TO_MILLISECOND);
                // 5秒后发送异步通知，减少异步通知比同步通知快的情况。
                objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5000);
                return objectMessage;
            });
        } catch (Exception e) {
            logger.error("添加商户通知至消息队列失败！", e);
        }
    }







    private String getReturnJson(PayOrder order, MerchantInfo merchantInfo, BankResult bankResult,  TradeObjectSquare tradeObjectSquare) {

//        String merId = merchantInfo.getMerId();
//        String merOrderId = order.getMerOrderId();
//        String currency = order.getCurrency();
//        String amount = order.getAmount().toString();
//        String transId = order.getPayId();
//        Short status = bankResult.getStatus();
//        String secretKey = merchantInfo.getSecretKey();
//        String md5Str=merId+merOrderId+currency+amount+transId+status+secretKey;
//        md5Str=DigestUtils.md5Hex(md5Str);
//        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
//        resultMap.put("MerId",merId);
//        resultMap.put("MerOrderId",merOrderId);
//        if(tradeObjectSquare.getAuthCode()!=null){
//            resultMap.put("AuthCode;",tradeObjectSquare.getAuthCode());
//        }
//        resultMap.put("TradeTime;",order.getTradeTime());
//        resultMap.put("Currency;",currency);
//        resultMap.put("Amount;",amount);
//        resultMap.put("OrderId;",transId);
//        resultMap.put("Status;",status);
//        resultMap.put("Msg;",bankResult.getBankResult());
//        resultMap.put("SignMsg;",md5Str);
//        String result  = JsonUtils.objectToJson(resultMap);

        String result="aaaaaaaaaaaaaaaaaaaaaaaaa";


        return result;




    }

    private String getNoCardPayReturnJson(PayOrder order, MerchantInfo merchantInfo, BankResult bankResult,  TradeObjectSquare tradeObjectSquare) {

        String merId = merchantInfo.getMerId();
        String merOrderId = order.getMerOrderId();
        String currency = order.getCurrency();
        String amount = order.getAmount().toString();
        String transId = order.getPayId();
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




    public void sendNoCardPayNotify(PayOrder payOrder, MerchantInfo merchantInfo, BankResult bankResult, SystemOrderTrack systemOrderTrack) {
        try {

            NoCardPayNotify merchantNotify = new NoCardPayNotify();
            TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
//            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSquare.parse(systemOrderTrack);
            merchantNotify.setReturnJson(getNoCardPayReturnJson(payOrder,merchantInfo,bankResult,tradeObjectSquare));
//            merchantNotify.setNotifyUrl(tradeObjectSquare.getNoticeUrl());
            merchantNotify.setNotifyUrl("http://localhost:8102/getMsg");
            merchantNotify.setNotifyCycle(3 * SystemConstant.SECOND_TO_MILLISECOND);
            merchantNotify.setNotifyTimes(4);
            // 将通知商户信息添加到消息队列
            queueJmsTemplate.send(ConsumerMessageListener.NOCARDPAY_NOTIFY, session -> {
                ObjectMessage objectMessage = session.createObjectMessage(merchantNotify);
                // 重复投递次数3次
                // mapMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 3);
                // 重复投递时间间隔
                // mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 3 * SystemConstant.HOUR_TO_MILLISECOND);
                // 5秒后发送异步通知，减少异步通知比同步通知快的情况。
                objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5000);
                return objectMessage;
            });
        } catch (Exception e) {
            logger.error("添加商户通知至消息队列失败！", e);
        }
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

    public void resendGetOrderPayNotify(GetOrderPayNotify merchantNotify) {
        queueJmsTemplate.send(ConsumerMessageListener.MERCHANT_NOTIFY, session -> {
            ObjectMessage objectMessage = session.createObjectMessage(merchantNotify);
            // 重复投递时间间隔
            objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, merchantNotify.getNotifyCycle());
            return objectMessage;
        });
    }
}
