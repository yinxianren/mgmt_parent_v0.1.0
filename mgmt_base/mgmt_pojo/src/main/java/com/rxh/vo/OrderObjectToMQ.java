package com.rxh.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *   收单 和 代付  订单对象，用于发送到mq 队列
 * @author  panda
 * @date 20190721
 *
 */
public class OrderObjectToMQ implements Serializable {

    //商户信息
    private String merId;
    private String parentId; //商户上级代理

    //通道信息
    private String channelId;
    private Integer channelType;
    private String channelTransCode;

    //订单信息
    private String payType;
    private String merOrderId;
    private BigDecimal amount;
    private BigDecimal payFee;//支付费率
    private String terminalMerId;
    private Integer orderStatus; //交易状态（0成功、1失败、2未支付、3处理中 20队列处理中）
    private BigDecimal realAmount;
    private BigDecimal terminalFee;

    //平台订单信息
    private String payId;
    private String[] payIds;
    private String transId;

    public String[] getPayIds() {
        return payIds;
    }

    public void setPayIds(String[] payIds) {
        this.payIds = payIds;
    }

    public OrderObjectToMQ lsetPayIds(String[] payIds) {
        this.setPayIds(payIds);
        return this;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
    public OrderObjectToMQ lsetTransId(String transId) {
        this.setTransId(transId);
        return this;
    }
    public BigDecimal getTerminalFee() {
        return terminalFee;
    }

    public void setTerminalFee(BigDecimal terminalFee) {
        this.terminalFee = terminalFee;
    }
    public OrderObjectToMQ lsetTerminalFee(BigDecimal terminalFee) {
        this.setTerminalFee( terminalFee);
        return this;
    }
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }
    public OrderObjectToMQ lsetRealAmount(BigDecimal realAmount) {
        this.setRealAmount( realAmount);
        return this;
    }
    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
    public OrderObjectToMQ lsetOrderStatus(Integer orderStatus) {
        this.setOrderStatus( orderStatus) ;
        return this;
    }
    public String getChannelTransCode() {
        return channelTransCode;
    }

    public void setChannelTransCode(String channelTransCode) {
        this.channelTransCode = channelTransCode;
    }
    public OrderObjectToMQ lsetChannelTransCode(String channelTransCode) {
        this.setChannelTransCode( channelTransCode);
        return this;
    }
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public OrderObjectToMQ lsetParentId(String parentId) {
        this.setParentId( parentId);
        return this;
    }

    public String getTerminalMerId() {
        return terminalMerId;
    }

    public void setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
    }
    public OrderObjectToMQ lsetTerminalMerId(String terminalMerId) {
        this.setTerminalMerId(terminalMerId);
        return this;
    }


    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }
    public OrderObjectToMQ lsetPayId(String payId) {
        this.setPayId( payId) ;
        return this;
    }


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public OrderObjectToMQ lsetChannelId(String channelId) {
        this.setChannelId( channelId);
        return this;
    }


    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }
    public OrderObjectToMQ lsetChannelType(Integer channelType) {
        this.setChannelType( channelType);
        return this;
    }


    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }
    public OrderObjectToMQ lsetPayFee(BigDecimal payFee) {
        this.setPayFee( payFee);
        return this;
    }
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
    public OrderObjectToMQ lsetPayType(String payType) {
        this.setPayType(payType);
        return this;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public OrderObjectToMQ lsetAmount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }
    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }
    public OrderObjectToMQ lsetMerId(String merId) {
        this.setMerId( merId);
        return this;
    }
    public String getMerOrderId() {
        return merOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
    }

    public OrderObjectToMQ lsetMerOrderId(String merOrderId) {
        this.setMerOrderId( merOrderId) ;
        return this;
    }


    @Override
    public String toString() {
        return "OrderObjectToMQ{" +
                "merId='" + merId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", channelType=" + channelType +
                ", channelTransCode='" + channelTransCode + '\'' +
                ", payType='" + payType + '\'' +
                ", merOrderId='" + merOrderId + '\'' +
                ", amount=" + amount +
                ", payFee=" + payFee +
                ", terminalMerId='" + terminalMerId + '\'' +
                ", orderStatus=" + orderStatus +
                ", realAmount=" + realAmount +
                ", terminalFee=" + terminalFee +
                ", payId='" + payId + '\'' +
                ", transId='" + transId + '\'' +
                '}';
    }
}
