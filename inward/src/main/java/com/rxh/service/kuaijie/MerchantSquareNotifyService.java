package com.rxh.service.kuaijie;

import com.rxh.activeMQ.ConsumerMessageListener;
import com.rxh.i18.I18Component;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.MerchantNotify;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.NotifyOrder;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.square.pojo.TransOrder;
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
import java.util.Map;
import java.util.TreeMap;


@Service
public class MerchantSquareNotifyService {

    public final static String SUCCEED = "SUCCEED";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JmsTemplate queueJmsTemplate;
    private final I18Component i18Component;

    @Autowired
    public MerchantSquareNotifyService(JmsTemplate queueJmsTemplate, I18Component i18Component) {
        this.queueJmsTemplate = queueJmsTemplate;
        this.i18Component = i18Component;
    }

    /**
     * 发送商户通知
     *
     * @param transOrder       平台订单对象
     * @param bankResult       银行结果对象
     * @param systemOrderTrack
     */
    void sendMerchantNotify(TransOrder transOrder, MerchantInfo merchantInfo, BankResult bankResult, SystemOrderTrack systemOrderTrack) {
        // CoreAcquirerAccountParam param = JsonUtils.jsonToPojo(acquirerAccount.getParams(), CoreAcquirerAccountParam.class);
        // assert param != null;
        MerchantNotify merchantNotify = new MerchantNotify();
        merchantNotify.setResultJson(getResultJson(transOrder, merchantInfo, bankResult));
        merchantNotify.setNotifyUrl(systemOrderTrack.getNoticeUrl());
        merchantNotify.setNotifyCycle(3 * SystemConstant.SECOND_TO_MILLISECOND);
        merchantNotify.setNotifyTimes(4);
        try {
            // 将通知商户信息添加到消息队列
            queueJmsTemplate.send(ConsumerMessageListener.MERCHANT_NOTIFY, session -> {
                ObjectMessage objectMessage = session.createObjectMessage(merchantNotify);
                // 重复投递次数3次
//                objectMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 3);
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

    private String getResultJson(TransOrder transOrder, MerchantInfo merchantInfo, BankResult bankResult) {
        String merId = merchantInfo.getMerId();
        String merOrderId = transOrder.getMerOrderId();
        String amount = transOrder.getAmount().toString();
        Short status = bankResult.getStatus();
        String transId = transOrder.getTransId();
        Map<String, Object> resultMap = new TreeMap<>();
        resultMap.put("merId", merId);
        resultMap.put("merOrderId", merOrderId);
        resultMap.put("orderId;", transId);
        resultMap.put("amount;", amount);
        resultMap.put("status;", status);
        resultMap.put("msg;", bankResult.getBankResult());
        resultMap.put("signMsg;", CheckMd5Utils.getMd5Sign(resultMap));
        String result = JsonUtils.objectToJson(resultMap);
        return result;

    }

    /**
     * 重发商户通知
     *
     * @param merchantNotify 商户通知对象
     */
    public void resendMerchantNotify(MerchantNotify merchantNotify) {
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
     * @param notifyOrder 商户通知对象
     * @param time   重复投递时间间隔 ，秒
     */
    public void reSendHaiyiNotify(NotifyOrder notifyOrder,Long time) {
        queueJmsTemplate.send(ConsumerMessageListener.HAIYI_NOTIFY, session -> {
            ObjectMessage objectMessage = session.createObjectMessage(notifyOrder);
            // 重复投递时间间隔
            objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time*1000L);
            return objectMessage;
        });
    }

    /**
     * 获取通知商户MD5信息
     *
     * @return MD5Info
     */
    String getNotifyMd5Info(TransOrder transOrder, MerchantInfo merchantInfo) {
        return DigestUtils.md5Hex(transOrder.getMerId() + transOrder.getMerOrderId() + transOrder.getCurrency() + transOrder.getAmount() + transOrder.getTransId() + transOrder.getOrderStatus() + merchantInfo.getSecretKey());
    }
}
