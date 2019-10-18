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
 *  描述： 订单队列
 * @author  panda
 * @date 20190713
 */
@Component
public class TransOrderMQ {

    @Autowired
    private JmsTemplate  jmsTemplate;

    /**
     *
     *  描述：向订单队列存放订单对象,该队列保存的是订单受理
     * merchantNotify,orderNotify,transOderFirst,transOderTwo 按照排列顺序取到transOder队列，并且向transOder队列存放对象
     *
     * @param object  object 必须序列化
     *
     */
    public void sendObjectMessageToTransOderFirst(final Serializable object) {
        jmsTemplate.send( "transOderFirst", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(object);
                //延迟20秒发送
                objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,20000L);
                return (Message) objectMessage;
            }
        });
    }
    /**
     *
     *  描述：向订单队列存放订单对象,该队列保存的是查询成功的订单，主要保存订单状态为成功和失败的订单
     * merchantNotify,orderNotify,transOderFirst,transOderTwo 按照排列顺序取到transOder队列，并且向transOder队列存放对象
     *
     * @param object  object 必须序列化
     *
     */
    public void sendObjectMessageToTransOderTwo(final Serializable object) {
        jmsTemplate.send( "transOderTwo", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return (Message) session.createObjectMessage(object);
            }
        });
    }

    /**
     *
     *  描述：向订单队列重新存放订单对象,该队列保存的是订单受理
     * merchantNotify,orderNotify,transOderFirst,transOderTwo 按照排列顺序取到transOder队列，并且向transOder队列存放对象
     *
     * @param object  object 必须序列化
     * @param time  time 间隔时间，单位秒
     *
     */
    public void reSendObjectMessageToTransOderFirst(final Serializable object,Long time) {
        jmsTemplate.send( "transOderFirst", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(object);
                //延迟time秒发送
                objectMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time*1000);
                return (Message) objectMessage;
            }
        });
    }


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
