package com.rxh.vo;

import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.MerchantRegisterCollect;
import com.rxh.square.pojo.PayOrder;
import com.rxh.square.pojo.TransOrder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *   收单 和 代付  订单对象，用于发送到mq 队列
 * @author  monkey
 * @date 20190727
 *
 */
public class QueryOrderObjectToMQ implements Serializable {

    private PayOrder payOrder;
    private TransOrder transOrder;
    private ChannelInfo channelInfo;
    private MerchantRegisterCollect merchantRegisterCollect;
    private BigDecimal payFee;
    private Integer times = 0;//队列重发次数

    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public PayOrder getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(PayOrder payOrder) {
        this.payOrder = payOrder;
    }

    public TransOrder getTransOrder() {
        return transOrder;
    }

    public void setTransOrder(TransOrder transOrder) {
        this.transOrder = transOrder;
    }

    public ChannelInfo getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(ChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
    }

    public MerchantRegisterCollect getMerchantRegisterCollect() {
        return merchantRegisterCollect;
    }

    public void setMerchantRegisterCollect(MerchantRegisterCollect merchantRegisterCollect) {
        this.merchantRegisterCollect = merchantRegisterCollect;
    }

    @Override
    public String toString() {
        return "QueryOrderObjectToMQ{" +
                ", payOrder=" + payOrder +
                ", transOrder=" + transOrder +
                ", channelInfo=" + channelInfo +
                ", merchantRegisterCollect=" + merchantRegisterCollect +
                '}';
    }
}
