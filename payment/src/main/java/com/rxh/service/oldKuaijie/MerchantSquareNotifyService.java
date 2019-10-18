package com.rxh.service.oldKuaijie;

import com.rxh.activeMQ.ConsumerMessageListener;
import com.rxh.i18.I18Component;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.MerchantNotify;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.SystemConstant;
import org.apache.activemq.ScheduledMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.ObjectMessage;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/18
 * Time: 10:55
 * Project: Management
 * Package: com.rxh.service
 */
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
     * @param transOrder         平台订单对象
     * @param bankResult    银行结果对象
     */
    void sendMerchantNotify(TransOrder transOrder,MerchantInfo merchantInfo, BankResult bankResult) {
        // CoreAcquirerAccountParam param = JsonUtils.jsonToPojo(acquirerAccount.getParams(), CoreAcquirerAccountParam.class);
        // assert param != null;
        MerchantNotify merchantNotify = new MerchantNotify();
        merchantNotify.setBillNo(transOrder.getMerOrderId());
        merchantNotify.setTradeNo(Long.valueOf(transOrder.getTransId()));
        // merchantNotify.setCurrency(order.getCurrency());
        merchantNotify.setAmount(transOrder.getAmount());
        merchantNotify.setSucceed(bankResult.getStatus());
        merchantNotify.setResult(i18Component.getI18Msg(bankResult.getBankCode(), "CN"));
//        merchantNotify.setResult(i18Component.getI18Msg("pay.1047", orderDetail.getLang()));
        merchantNotify.setMd5Info(getNotifyMd5Info(transOrder, merchantInfo));
        merchantNotify.setRemark(bankResult.getBankCode());
        // merchantNotify.setNotifyUrl(merchantOrder.getNoticeUrl());
        merchantNotify.setNotifyCycle(3 * SystemConstant.SECOND_TO_MILLISECOND);
//        merchantNotify.setNotifyCycle(3 * SystemConstant.MINUTE_TO_MILLISECOND);
        merchantNotify.setNotifyTimes(4);
        try {
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
     * 获取通知商户MD5信息
     * @return MD5Info
     */
    String getNotifyMd5Info(TransOrder transOrder, MerchantInfo merchantInfo) {
        return DigestUtils.md5Hex(transOrder.getMerId() + transOrder.getMerOrderId() + transOrder.getCurrency()+transOrder.getAmount()+transOrder.getTransId()+transOrder.getOrderStatus()+merchantInfo.getSecretKey());
    }
}
