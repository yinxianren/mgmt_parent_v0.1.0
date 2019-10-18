package com.rxh.spring.aop;

import com.rxh.jedis.CacheEnum;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/8/28
 * Time: 13:55
 * Project: Management
 * Package: com.rxh.spring.aop
 */

public class SendRefreshCacheMessage {

    private final static Logger logger = LoggerFactory.getLogger(SendRefreshCacheMessage.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void after(Caching caching) {
        List<String> beanNames = new ArrayList<>();
        CacheEvict[] evict = caching.evict();
        for (CacheEvict ce : evict) {
            try {
                beanNames.add(CacheEnum.valueOf(ce.value()[0]).getBeanName());
            } catch (IllegalArgumentException e) {
                logger.error("发送刷新缓存异常！枚举类里不存在对象：" + ce.value()[0], e);
            }
        }
        beanNames = beanNames.stream().distinct().collect(Collectors.toList());
        for (String beanName : beanNames) {
            jmsTemplate.send(session -> {
                TextMessage textMessage = session.createTextMessage(beanName);
                // 延迟2秒发送刷新缓存消息，防止数据库操作未提交就刷新缓存导致脏读
                textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 2000L);
                return textMessage;
            });
        }
    }
}