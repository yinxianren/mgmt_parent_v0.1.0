package com.rxh.service.kuaijie;

import com.alibaba.fastjson.JSONObject;
import com.rxh.activeMQ.ConsumerMessageListener;
import com.rxh.activeMQ.TransOrderMQ;
import com.rxh.exception.PayException;
import com.rxh.i18.I18Component;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.*;
import com.rxh.service.oldKuaijie.GetOrderWalletService;
import com.rxh.service.oldKuaijie.RecordPaymentSquareService;
import com.rxh.service.oldKuaijie.SweepCodePaymentService;
import com.rxh.service.square.NotifyOrderService;
import com.rxh.service.trading.PayOrderService;
import com.rxh.service.trading.TransOrderService;
import com.rxh.square.pojo.*;
import com.rxh.utils.CheckMd5Utils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import com.rxh.vo.OrderObjectToMQ;
import net.sf.ehcache.search.expression.IsNull;
import org.apache.activemq.ScheduledMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Service
public class GetOrderNotifyService {

    public final static String SUCCEED = "SUCCEED";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JmsTemplate queueJmsTemplate;
    private final I18Component i18Component;
    private final RecordPaymentSquareService recordPaymentSquareService;
    private final GetOrderWalletService getOrderWalletService;
    private final SweepCodePaymentService sweepCodePaymentService;
    private final String HAIYI_NOTIFY = "haiyiNotify";


    @Autowired
    public GetOrderNotifyService(JmsTemplate queueJmsTemplate, I18Component i18Component, RecordPaymentSquareService recordPaymentSquareService, GetOrderWalletService getOrderWalletService, SweepCodePaymentService sweepCodePaymentService) {
        this.queueJmsTemplate = queueJmsTemplate;
        this.i18Component = i18Component;
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.getOrderWalletService = getOrderWalletService;
        this.sweepCodePaymentService = sweepCodePaymentService;
    }

    @Autowired
    private TransOrderMQ transOrderMQ;
    @Resource
    private KuaijiePayQueryService kuaijiePayQueryService;
    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private TransOrderService transOrderService;
    @Autowired
    private NotifyOrderService notifyOrderService;

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

    public void sendPayNoyifyMQ(PayOrder order, MerchantInfo merchantInfo, BankResult bankResult, SystemOrderTrack systemOrderTrack) {
        if (null != systemOrderTrack) {
            Map map = new TreeMap();
            map.put("merId", order.getMerId());
            map.put("merOrderId", order.getMerOrderId());
            map.put("orderId", order.getPayId());
            map.put("amount", order.getAmount().setScale(2));
            map.put("status", bankResult.getStatus());
            map.put("msg", bankResult.getBankResult());
            map.put("terminalMerId", order.getTerminalMerId());
            String signMsg = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo == null ? "" : merchantInfo.getSecretKey());
            map.put("signMsg", signMsg);
            NotifyOrder notifyOrder = new NotifyOrder();
            notifyOrder.setNotifyId(UUID.createId());
            notifyOrder.setMerId(order.getMerId());
            notifyOrder.setMerOrderId(order.getMerOrderId());
            notifyOrder.setNotifyNum(1);
            notifyOrder.setNotifyUrl(systemOrderTrack.getNoticeUrl());
            notifyOrder.setNotifyResult(JSONObject.toJSONString(map));
            notifyOrder.setNotifyStatus(String.valueOf(SystemConstant.BANK_STATUS_FAIL));
            notifyOrder.setNotifyTime(new Date());
            notifyOrder.setOriginalOrderId(order.getPayId());
            notifyOrder.setRemark(null);
            InetAddress address = null;
            try {
                address = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.getMessage();
            }
            String ip = address.getHostAddress();
            notifyOrder.setIp(ip);
            notifyOrderService.insert(notifyOrder);
            //将需要异步通知的订单放入队列
            queueJmsTemplate.send(HAIYI_NOTIFY, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    ObjectMessage objectMessage = session.createObjectMessage(notifyOrder);
                    return objectMessage;
                }
            });
        }
    }

    public void sendTransNoyifyMQ(TransOrder order, MerchantInfo merchantInfo, BankResult bankResult, SystemOrderTrack systemOrderTrack) {
        if (null != systemOrderTrack) {
            Map map = new TreeMap();
            map.put("merId", order.getMerId());
            map.put("merOrderId", order.getMerOrderId());
            map.put("orderId", order.getTransId());
            map.put("amount", order.getAmount().setScale(2));
            map.put("status", bankResult.getStatus());
            map.put("msg", bankResult.getBankResult());
            map.put("terminalMerId", order.getTerminalMerId());
            String signMsg = CheckMd5Utils.getMd5SignWithKey(map, merchantInfo == null ? "" : merchantInfo.getSecretKey());
            map.put("signMsg", signMsg);
            NotifyOrder notifyOrder = new NotifyOrder();
            notifyOrder.setNotifyId(UUID.createId());
            notifyOrder.setMerId(order.getMerId());
            notifyOrder.setMerOrderId(order.getMerOrderId());
            notifyOrder.setNotifyNum(1);
            notifyOrder.setNotifyUrl(systemOrderTrack.getNoticeUrl());
            notifyOrder.setNotifyResult(JSONObject.toJSONString(map));
            notifyOrder.setNotifyStatus(String.valueOf(SystemConstant.BANK_STATUS_FAIL));
            notifyOrder.setNotifyTime(new Date());
            notifyOrder.setOriginalOrderId(order.getTransId());
            notifyOrder.setRemark(null);
            InetAddress address = null;
            try {
                address = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.getMessage();
            }
            String ip = address.getHostAddress();
            notifyOrder.setIp(ip);
            //将需要异步通知的订单放入队列
            queueJmsTemplate.send(HAIYI_NOTIFY, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    ObjectMessage objectMessage = session.createObjectMessage(notifyOrder);
                    return objectMessage;
                }
            });
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

    public Result getPayOrderNotify(BankResult bankResult) throws PayException {
        Long orderId = bankResult.getOrderId();
        PayOrder payOrder= recordPaymentSquareService.getPayOrderById(orderId.toString());
        ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId().toString());
        MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(payOrder.getMerId());
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(payOrder.getMerId());
        SystemOrderTrack systemOrderTrack= recordPaymentSquareService.getSystemOrderTrack(payOrder.getPayId());
        //订单状态为成功 异步通知为失败
        if(payOrder.getOrderStatus()==0&&bankResult.getStatus()==1){
            // 订单冲正
            SquareTrade squareTrade = new SquareTrade();
            BigDecimal bigDecimal = new BigDecimal(0);
            payOrder.setAmount(bigDecimal.subtract(payOrder.getAmount()));
            payOrder.setOrderStatus(20);
            payOrder.setPayType("6");
            payOrder.setTradeTime(new Date());
            payOrder.setBankTime(bankResult.getBankTime());
            TradeObjectSquare objectSquare = new TradeObjectSquare();
            squareTrade.setPayOrder(payOrder);
            objectSquare.setPayFee(payOrder.getTerminalFee().multiply(new BigDecimal(100)).divide(payOrder.getAmount()).setScale(2));
            squareTrade.setTradeObjectSquare(objectSquare);
            squareTrade.setChannelInfo(channelInfo);
            squareTrade.setMerchantInfo(merchantInfo);
            OrderObjectToMQ orderObjectToMQ = kuaijiePayQueryService.getOrderObjectToMQ(squareTrade);
            transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
            payOrderService.updateByPrimaryKey(payOrder);
        }
        //订单状态为失败 异步通知为成功
        if(payOrder.getOrderStatus()==1&&bankResult.getStatus()==0){
            // 订单入库
            SquareTrade squareTrade = new SquareTrade();
            BigDecimal bigDecimal = new BigDecimal(0);
            PayOrder newOrder = new PayOrder();
            newOrder = payOrder;

            newOrder.setAmount(bigDecimal.subtract(newOrder.getAmount()));
            newOrder.setOrderStatus(20);
            newOrder.setPayType("5");
            newOrder.setPayId(String.valueOf(UUID.createKey("pay_order")));
            newOrder.setTradeTime(new Date());
//            if (bankResult.getRealAmount()!=null){
//                newOrder.setRealAmount(bankResult.getRealAmount());
//            }
            newOrder.setBankTime(bankResult.getBankTime());
            TradeObjectSquare objectSquare = new TradeObjectSquare();
            squareTrade.setPayOrder(newOrder);
            objectSquare.setPayFee(newOrder.getTerminalFee().multiply(new BigDecimal(100)).divide(newOrder.getAmount()).setScale(2));
            squareTrade.setTradeObjectSquare(objectSquare);
            squareTrade.setChannelInfo(channelInfo);
            squareTrade.setMerchantInfo(merchantInfo);
            OrderObjectToMQ orderObjectToMQ = kuaijiePayQueryService.getOrderObjectToMQ(squareTrade);
            transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
            payOrderService.insertBean(newOrder);
        }
        payOrder.setOrderStatus((int)bankResult.getStatus());
        //异步通知队列
        // sendPayNoyifyMQ(payOrder,merchantInfo,bankResult,systemOrderTrack);
        return new Result(Result.SUCCESS, "订单更新完成！");

    }

    public Result getTransOrderNotify(BankResult bankResult) throws Exception {
        Long orderId = bankResult.getOrderId();
        TransOrder transOrder= transOrderService.getTransOrderByPrimaryId(orderId.toString());
        if (transOrder == null){
            return null;
        }
        ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(transOrder.getChannelId().toString());
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(transOrder.getMerId());
        SystemOrderTrack systemOrderTrack= recordPaymentSquareService.getSystemOrderTrack(transOrder.getTransId());
        //订单状态为成功 异步通知为失败
        if(transOrder.getOrderStatus()==0&&bankResult.getStatus()==1){
            // 订单冲正
            SquareTrade squareTrade = new SquareTrade();
            BigDecimal bigDecimal = new BigDecimal(0);
            transOrder.setAmount(bigDecimal.subtract(transOrder.getAmount()));
            transOrder.setOrderStatus(30);
            transOrder.setPayType("6");
            transOrder.setTradeTime(new Date());
            transOrder.setBankTime(bankResult.getBankTime());
            TradeObjectSquare objectSquare = new TradeObjectSquare();
            squareTrade.setTransOrder(transOrder);
            objectSquare.setPayFee(new BigDecimal(JSONObject.parseObject(systemOrderTrack.getTradeInfo()).getString("backFee")));
            squareTrade.setTradeObjectSquare(objectSquare);
            squareTrade.setChannelInfo(channelInfo);
            squareTrade.setMerchantInfo(merchantInfo);
            PayOrder payOrder = payOrderService.seleteBymerOrderId(transOrder.getMerId(),transOrder.getOriginalMerOrderId(),transOrder.getTerminalMerId());
            squareTrade.setPayOrder(payOrder);
            OrderObjectToMQ orderObjectToMQ = kuaijiePayQueryService.getOrderObjectToMQ(squareTrade);
            transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
            transOrderService.updateByPrimaryKey(transOrder);
//            getOrderWalletService.notifyRevertWallet(bankResult);
            transOrderService.updateByPrimaryKey(transOrder);
        }
        //订单状态为失败 异步通知为成功
        if(transOrder.getOrderStatus()==1 &&bankResult.getStatus()==0){
            // 订单入库
            SquareTrade squareTrade = new SquareTrade();
            TransOrder newOrder = new TransOrder();
            newOrder = transOrder;
            newOrder.setAmount(newOrder.getAmount());
            newOrder.setOrderStatus(30);
            newOrder.setPayType("4");
            newOrder.setTransId(String.valueOf(UUID.createKey("trans_order")));
            newOrder.setTradeTime(new Date());
//            if (bankResult.getRealAmount()!=null){
//                newOrder.setRealAmount(bankResult.getRealAmount());
//            }
            newOrder.setBankTime(bankResult.getBankTime());
            TradeObjectSquare objectSquare = new TradeObjectSquare();
            squareTrade.setTransOrder(newOrder);
            objectSquare.setPayFee(new BigDecimal(JSONObject.parseObject(systemOrderTrack.getTradeInfo()).getString("backFee")));
            squareTrade.setTradeObjectSquare(objectSquare);
            squareTrade.setChannelInfo(channelInfo);
            squareTrade.setMerchantInfo(merchantInfo);
            PayOrder payOrder = payOrderService.seleteBymerOrderId(transOrder.getMerId(),transOrder.getOriginalMerOrderId(),transOrder.getTerminalMerId());
            squareTrade.setPayOrder(payOrder);
            OrderObjectToMQ orderObjectToMQ = kuaijiePayQueryService.getOrderObjectToMQ(squareTrade);
            transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
            transOrderService.insertBean(newOrder);
        }
        //订单状态为处理中 异步通知为成功
        if(transOrder.getOrderStatus()==3 && bankResult.getStatus()==0){
            // 更改状态
            SquareTrade squareTrade = new SquareTrade();
            TransOrder newOrder = new TransOrder();
            newOrder = transOrder;
            newOrder.setOrderStatus(30);
            newOrder.setTradeTime(new Date());
//            if (bankResult.getRealAmount()!=null){
//                newOrder.setRealAmount(bankResult.getRealAmount());
//            }
            newOrder.setBankTime(bankResult.getBankTime());
            TradeObjectSquare objectSquare = new TradeObjectSquare();
            squareTrade.setTransOrder(newOrder);
            objectSquare.setPayFee(new BigDecimal(JSONObject.parseObject(systemOrderTrack.getTradeInfo()).getString("backFee")));
            squareTrade.setTradeObjectSquare(objectSquare);
            squareTrade.setChannelInfo(channelInfo);
            squareTrade.setMerchantInfo(merchantInfo);
            PayOrder payOrder = payOrderService.seleteBymerOrderId(transOrder.getMerId(),transOrder.getOriginalMerOrderId(),transOrder.getTerminalMerId());
            squareTrade.setPayOrder(payOrder);
            OrderObjectToMQ orderObjectToMQ = kuaijiePayQueryService.getOrderObjectToMQ(squareTrade);
            transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
            transOrderService.updateByPrimaryKey(newOrder);
        }
        transOrder.setOrderStatus((int)bankResult.getStatus());
        sendTransNoyifyMQ(transOrder,merchantInfo,bankResult,systemOrderTrack);
        return new Result(Result.SUCCESS, "订单更新完成！");

    }

    public Result getOrderNotify(BankResult bankResult) throws Exception {
        logger.info("==订单异步通知返回==status:"+bankResult.getStatus()+",orderId:"+bankResult.getOrderId());
        if (bankResult.getOrderId() == null){
            return null;
        }
        PayOrder payOrder = payOrderService.selectByPayId(bankResult.getOrderId().toString());
        if (payOrder == null){
            return getTransOrderNotify(bankResult);
        }else {
            return getPayOrderNotify(bankResult);
        }
    }
}
