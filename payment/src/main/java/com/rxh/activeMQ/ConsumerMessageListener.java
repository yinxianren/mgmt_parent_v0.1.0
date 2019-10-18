package com.rxh.activeMQ;

import com.rxh.cache.ehcache.BaseCache;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.GetOrderPayNotify;
import com.rxh.pojo.payment.NoCardPayNotify;
import com.rxh.pojo.payment.SweepCodeNotify;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.kuaijie.GetOrderNotifyService;
import com.rxh.service.kuaijie.KuaijiePayQueryService;
import com.rxh.service.oldKuaijie.SweepCodeNotifyService;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.StringUtils;
import com.rxh.vo.OrderObjectToMQ;
import com.rxh.vo.QueryOrderObjectToMQ;
import com.rxh.wallet.HandleWalletServe;
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


public class ConsumerMessageListener implements MessageListener {
    public final static String MERCHANT_NOTIFY = "merchantNotify";
    public final static String NOCARDPAY_NOTIFY = "nocardPayNotify";
    public final static String SWEEPCODE_NOTIFY = "SweepCodeNotify";
    public final static String ORDER_NOTIFY = "orderNotify";
    public final static String GETORDEY_NOTIFY = "getOrdeyNotify";
    public final static String TRANS_WALLET = "transWallet";
    public final static String QUERYPAY = "queryPayOrder";
    public final static String QUERYTRANS = "queryTransOrder";
    private static  Object object=new Object();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SweepCodeNotifyService sweepCodeNotifyService;

    @Autowired
    private HandleWalletServe handleWalletServe;

    @Autowired
    private List<BaseCache> baseCaches;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private KuaijiePayQueryService kuaijiePayQueryService;


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
                    case SWEEPCODE_NOTIFY:
                        sendSweepcodeNotify(objectMessage);
                        break;
                    case NOCARDPAY_NOTIFY:
                        sendNoCardPayNotify(objectMessage);
                        break;
                    case GETORDEY_NOTIFY:
                        GetOrderPayNotify(objectMessage);
                        break;
                    case TRANS_WALLET://处理钱包业务
                        handleWalletService(objectMessage);
                        break;
                    case QUERYPAY://查询收单业务
                        handleQueryPayService(objectMessage);
                        break;
                    case QUERYTRANS://查询代付业务
                        handleQueryTransService(objectMessage);
                        break;
                    default:
                        break;
                }
            }
        } catch (JMSException e) {
            logger.error("ActiveMQ异常", e);
            e.printStackTrace();
        } catch (PayException e) {
            e.printStackTrace();
        }
    }


    private void orderNotifyHandle(ActiveMQObjectMessage objectMessage) throws PayException {
        try {

            logger.info("orderNotifyHandle监听到了消息=========");
            BankResult bankResult = (BankResult) objectMessage.getObject();
//                        paymentNotifyService.updateOrderAndDoNotify(bankResult);
//             paymentSquareNotifyService.updateOrder(bankResult);
        } catch (JMSException e) {
            logger.error("ActiveMQ异常", e);
        }
    }
    private void sendSweepcodeNotify(ActiveMQObjectMessage objectMessage) {
        try {
            logger.info("sendMerchantNotify监听到了消息");
            SweepCodeNotify merchantNotify = (SweepCodeNotify) objectMessage.getObject();
            Map<String, String> contentMap = BeanUtils.describe(merchantNotify);
            contentMap.remove("class");
            contentMap.remove("notifyTimes");
            contentMap.remove("notifyCycle");
            logger.info("通知商户参数：" + contentMap.toString());
            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), merchantNotify.getNotifyUrl(), merchantNotify.getReturnJson());
//            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(),"http://localhost:8102/getMsg", getTestJson());
            // 通知返回结果为空或不含有“Succeed”字符则重新通知
            if (StringUtils.isBlank(result) /*|| !StringUtils.containsIgnoreCase(result, sweepCodeNotifyService.SUCCEED)*/) {
                logger.warn("通知商户失败！" + "剩余通知商户次数：" + merchantNotify.getNotifyTimes());
                // 判断是否超过最大通知次数
                if (merchantNotify.getNotifyTimes() > 0) {
                    merchantNotify.setNotifyTimes(merchantNotify.getNotifyTimes() - 1);
                    sweepCodeNotifyService.resendMerchantNotify(merchantNotify);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("BeanUtils异常", e);
        } catch (JMSException e) {
            logger.error("ActiveMQ异常", e);
        }
    }

    /**
     *    钱包处理
     * @param objectMessage
     */
    private void handleWalletService(ActiveMQObjectMessage objectMessage){
        try{
            synchronized (object){
                OrderObjectToMQ orderObjectToMQ= (OrderObjectToMQ) objectMessage.getObject();
                if(orderObjectToMQ.getOrderStatus()==20) handleWalletServe.payOrderToObject(orderObjectToMQ);
                else if(orderObjectToMQ.getOrderStatus()==30) handleWalletServe.transOrderToObject(orderObjectToMQ);
            }
        }catch ( Exception e){
            e.printStackTrace();
        }
    }

    /**
     *   收单查询处理
     * @param objectMessage
     */
    private void handleQueryPayService(ActiveMQObjectMessage objectMessage){
        logger.info("===handleQueryPayService===");
        try{
            QueryOrderObjectToMQ queryOrderObjectToMQ= (QueryOrderObjectToMQ) objectMessage.getObject();
            kuaijiePayQueryService.kuaijiePayQueryMq(queryOrderObjectToMQ);
        }catch ( Exception e){
            logger.error("ActiveMQ异常", e);
            e.printStackTrace();
        }
    }

    /**
     *   代付查询处理
     * @param objectMessage
     */
    private void handleQueryTransService(ActiveMQObjectMessage objectMessage){
        logger.info("====handleQueryTransService===");
        try{
            QueryOrderObjectToMQ queryOrderObjectToMQ= (QueryOrderObjectToMQ) objectMessage.getObject();
            kuaijiePayQueryService.kuaijieTransQueryMq(queryOrderObjectToMQ);
        }catch ( Exception e){
            logger.error("ActiveMQ异常", e);
            e.printStackTrace();
        }
    }


    private void sendNoCardPayNotify(ActiveMQObjectMessage objectMessage) {
        try {
            logger.info("sendMerchantNotify监听到了消息");
            NoCardPayNotify merchantNotify = (NoCardPayNotify) objectMessage.getObject();
            Map<String, String> contentMap = BeanUtils.describe(merchantNotify);
            contentMap.remove("class");
            contentMap.remove("notifyTimes");
            contentMap.remove("notifyCycle");
            logger.info("通知商户参数：" + contentMap.toString());
//            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), merchantNotify.getNotifyUrl(), merchantNotify.getReturnJson());
            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(),"http://localhost:8102/getMsg", getTestJson());
            // 通知返回结果为空或不含有“Succeed”字符则重新通知
            if (StringUtils.isBlank(result) || !StringUtils.containsIgnoreCase(result, sweepCodeNotifyService.SUCCEED)) {
                logger.warn("通知商户失败！" + "剩余通知商户次数：" + merchantNotify.getNotifyTimes());
                // 判断是否超过最大通知次数
                if (merchantNotify.getNotifyTimes() > 0) {
                    merchantNotify.setNotifyTimes(merchantNotify.getNotifyTimes() - 1);
                    sweepCodeNotifyService.resendNoCardPayNotify(merchantNotify);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("BeanUtils异常", e);
        } catch (JMSException e) {
            logger.error("ActiveMQ异常", e);
        }
    }
    private void GetOrderPayNotify(ActiveMQObjectMessage objectMessage) {
        try {
            logger.info("sendMerchantNotify监听到了消息");
            GetOrderPayNotify merchantNotify = (GetOrderPayNotify) objectMessage.getObject();
            Map<String, String> contentMap = BeanUtils.describe(merchantNotify);
            contentMap.remove("class");
            contentMap.remove("notifyTimes");
            contentMap.remove("notifyCycle");
            logger.info("通知商户参数：" + contentMap.toString());
            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), merchantNotify.getNotifyUrl(), merchantNotify.getReturnJson());
//            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(),"http://localhost:8102/getJson", getTestJson());
            // 通知返回结果为空或不含有“Succeed”字符则重新通知
            if (StringUtils.isBlank(result) || !StringUtils.containsIgnoreCase(result, GetOrderNotifyService.SUCCEED)) {
                logger.warn("通知商户失败！" + "剩余通知商户次数：" + merchantNotify.getNotifyTimes());
                // 判断是否超过最大通知次数
                if (merchantNotify.getNotifyTimes() > 0) {
                    merchantNotify.setNotifyTimes(merchantNotify.getNotifyTimes() - 1);
                    sweepCodeNotifyService.resendGetOrderPayNotify(merchantNotify);
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
}