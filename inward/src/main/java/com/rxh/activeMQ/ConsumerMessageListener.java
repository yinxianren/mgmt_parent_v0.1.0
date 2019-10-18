package com.rxh.activeMQ;

import com.rxh.cache.ehcache.BaseCache;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.MerchantNotify;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.haiyi.HaiYiPayService;
import com.rxh.service.kuaijie.MerchantSquareNotifyService;
import com.rxh.service.kuaijie.PaymentSquareNotifyService;
import com.rxh.square.pojo.NotifyOrder;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.StringUtils;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.jms.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/9/21
 * Time: 10:03
 * Project: Management
 * Package: com.rxh.activeMQ
 *
 * transOderFirst,transOderTwo
 */
public class ConsumerMessageListener implements MessageListener {
    public final static String MERCHANT_NOTIFY = "merchantNotify";
    public final static String ORDER_NOTIFY = "orderNotify";
    //订单已经受理队列
    public final static String  TRANS_ODER_FIRST="transOderFirst";
    //查询过的订单，主要是成功和失败的订单
    public final static String  TRANS_ODER_TWO="transOderTwo";
    public final static String HAIYI_NOTIFY = "haiyiNotify";



    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MerchantSquareNotifyService merchantSquareNotifyService;
    @Resource
    private PaymentSquareNotifyService paymentSquareNotifyService;
    @Autowired
    private List<BaseCache> baseCaches;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private HaiYiPayService haiYiPayService;
    @Autowired
    private TransOrderMQ transOrderMQ;
    @Autowired
    private  TransOrderMQService transOrderMQService;

    @Override
    public void onMessage(Message message) {
        try {
            Destination jmsDestination = message.getJMSDestination();
            if (jmsDestination instanceof ActiveMQTopic) {
                TextMessage textMessage = (TextMessage) message;
                String  text=textMessage.getText();
                if(StringUtils.isEmpty(text)){
                    throw new NullPointerException("系统执行public void onMessage(Message message)方法，TextMessage文本信息记录的BeanName为 null");
                }
                text=text.substring(0,1).toLowerCase()+text.substring(1,text.length());
                logger.info("系统执行public void onMessage(Message message)方法，TextMessage文本信息记录的BeanName为:[{}]",text);
                BaseCache baseCache = applicationContext.getBean(text, BaseCache.class);
                baseCache.cleanCache();
                baseCache.refreshCache();
                logger.info("刷新缓存：" + textMessage.getText() + "成功！");
            } else if (jmsDestination instanceof ActiveMQQueue) {
                ActiveMQQueue activeMQQueue = (ActiveMQQueue) jmsDestination;
                ActiveMQObjectMessage objectMessage = (ActiveMQObjectMessage) message;
                switch (activeMQQueue.getQueueName()) {
                    case ORDER_NOTIFY:
                        orderNotifyHandle(objectMessage);
                        break;
                    case MERCHANT_NOTIFY:
                        sendMerchantNotify(objectMessage);
                        break;
                    case TRANS_ODER_FIRST: //订单已经受理队列
                        handlerTransOrderFirst(objectMessage);
                        break;
                    case TRANS_ODER_TWO: //查询过的订单，主要是成功和失败的订单
                        handlerTransOrderTwo(objectMessage);
                        break;
                    case HAIYI_NOTIFY :
                        sendHaiyiNotify(objectMessage);
                        break;
                    default:
                        break;
                }
            }
        } catch (JMSException e) {
            logger.error("ActiveMQ异常", e);
        } catch (PayException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单已经受理队列
     * @param objectMessage
     */
    private void handlerTransOrderFirst(ActiveMQObjectMessage objectMessage){
        TransOrder  transOrder= null;
        try {
            transOrder = (TransOrder)objectMessage.getObject();
            logger.info("####################transOrder##########################");
            logger.info(transOrder.toString());
            transOrderMQService.doTransOrderFirst(transOrder);
        } catch (Exception e) {
            //队列消费异常重新入库，不超过3次
            if (transOrder.getMQTimes() < 3){
                transOrder.setMQTimes(transOrder.getMQTimes()+1);
                if(null != transOrder) transOrderMQ.reSendObjectMessageToTransOderFirst(transOrder,transOrder.getMQTimes()*60L);
            }
            e.printStackTrace();
            logger.error("ActiveMQ异常", e);
        }
    }

    /**
     * 查询过的订单，主要是成功和失败的订单
     * @param objectMessage
     */
    private void handlerTransOrderTwo(ActiveMQObjectMessage objectMessage){
        TransOrder  transOrder= null;
        try {
            transOrder= (TransOrder)objectMessage.getObject();
            boolean how= haiYiPayService.haiYiPayHandleSucessAndFailTransOrder(transOrder);
            if(how) {
                logger.info("【海懿代付订单队列处理】成功消费了一条订单：[{}]",JsonUtils.objectToJson(transOrder));
            }
//            else {
//                logger.info("【海懿代付订单队列处理】 处理失败一条订单：[{}]",JsonUtils.objectToJson(transOrder));
//                transOrderMQ.sendObjectMessageToTransOderTwo(transOrder);
//            }
        }catch (Exception e){
//            if(null != transOrder)  transOrderMQ.sendObjectMessageToTransOderTwo(transOrder);
            e.printStackTrace();
        }
    }

    private void orderNotifyHandle(ActiveMQObjectMessage objectMessage) throws PayException {
        try {
            logger.info("orderNotifyHandle监听到了消息=========");
            BankResult bankResult = (BankResult) objectMessage.getObject();
            //            paymentNotifyService.updateOrderAndDoNotify(bankResult);
            paymentSquareNotifyService.updateOrder(bankResult);
        } catch (JMSException e) {
            logger.error("ActiveMQ异常", e);
        }
    }

    private void sendMerchantNotify(ActiveMQObjectMessage objectMessage) {
        try {
            logger.info("sendMerchantNotify监听到了消息");
            MerchantNotify merchantNotify = (MerchantNotify) objectMessage.getObject();
            Map<String, String> contentMap = BeanUtils.describe(merchantNotify);
            contentMap.remove("class");
            contentMap.remove("notifyTimes");
            contentMap.remove("notifyCycle");
            logger.info("通知商户参数：" + contentMap.toString());
            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), merchantNotify.getNotifyUrl(),merchantNotify.getResultJson());
            // 通知返回结果为空或不含有“Succeed”字符则重新通知
            if (StringUtils.isBlank(result) || !StringUtils.containsIgnoreCase(result, MerchantSquareNotifyService.SUCCEED)) {
                logger.warn("通知商户失败！" + "剩余通知商户次数：" + merchantNotify.getNotifyTimes());
                // 判断是否超过最大通知次数
                if (merchantNotify.getNotifyTimes() > 0) {
                    merchantNotify.setNotifyTimes(merchantNotify.getNotifyTimes() - 1);
                    merchantSquareNotifyService.resendMerchantNotify(merchantNotify);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("BeanUtils异常", e);
        } catch (JMSException e) {
            logger.error("ActiveMQ异常", e);
        }
    }

    private void initAllCache() {
        baseCaches.forEach(baseCache -> {
            logger.info("初始化缓存：" + baseCache.getClass().getName());
            baseCache.getCache();
        });
    }
    private String getTestJson(){

        TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
        tradeObjectSquare.setMerOrderId("123");
        tradeObjectSquare.setAmount(new BigDecimal(200));
        tradeObjectSquare.setMerId("1115");

        String s = JsonUtils.objectToJson(tradeObjectSquare);


        return s;
    }

    private void sendHaiyiNotify(ActiveMQObjectMessage objectMessage) throws JMSException {
        logger.info("sendHaiyiNotify=========");
        NotifyOrder notifyOrder = (NotifyOrder) objectMessage.getObject();
        try {
            transOrderMQService.doSendHaiyiNotify(notifyOrder);
        } catch (Exception e) {
            //通知处理异常，重新放入队列执行
//            notifyOrder.setNotifyNum(notifyOrder.getNotifyNum() +1);
//            if (notifyOrder.getNotifyNum() < 3) {
//                merchantSquareNotifyService.reSendHaiyiNotify(notifyOrder,notifyOrder.getNotifyNum()*10L);
//            }
            logger.error("ActiveMQ异常", e);
        }
    }
}