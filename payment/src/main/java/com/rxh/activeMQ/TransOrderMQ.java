package com.rxh.activeMQ;


import org.apache.activemq.Message;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

/**
 *  描述： 订单队列，用于处理钱包
 * @author  panda
 * @date 20190721
 */
@Component
public class TransOrderMQ {

    @Autowired
    private JmsTemplate  jmsTemplate;

    /**
     *  向队列发送订单对象
     * @param object
     */
    public void sendObjectMessageToTransOderMQ(final Serializable object) {
        jmsTemplate.send( "transWallet", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(object);
//                //延迟10秒发送
//                objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,20000L);
                return (Message) objectMessage;
            }
        });
    }

    /**
     *  向队列发送收单订单
     * @param object
     */
    public void sendObjectMessageToPayOderMsgMQ(final Serializable object) {
        jmsTemplate.send( "payOderMsg", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(object);
                return (Message) objectMessage;
            }
        });
    }


    /**
     *  向队列发送代付订单
     * @param object
     */
    public void sendObjectMessageToTransOderMsgMQ(final Serializable object) {
        jmsTemplate.send( "transOderMsg", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(object);
                return (Message) objectMessage;
            }
        });
    }


    /**
     *  向查询队列发送收单订单对象
     * @param time 重发间隔时间 秒（可空）
     * @param object
     */
    public void sendObjectMessageToQueryPayOderMQ(final Serializable object,Long time) {
        jmsTemplate.send( "queryPayOrder", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(object);
                //延迟10秒发送
                if (time != null)
                objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time*1000);
                return (Message) objectMessage;
            }
        });
    }

    /**
     *  向查询队列发送代付订单对象
     * @param object
     * @param time 重发间隔时间 秒（可空）
     */
    public void sendObjectMessageToQueryTransOderMQ(final Serializable object,Long time) {
        jmsTemplate.send( "queryTransOrder", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(object);
                //延迟time秒发送
                if (time != null)
                objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time*1000);
                return (Message) objectMessage;
            }
        });
    }



}
