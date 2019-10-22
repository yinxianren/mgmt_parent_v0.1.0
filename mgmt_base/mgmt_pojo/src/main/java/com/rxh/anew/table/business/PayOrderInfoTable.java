package com.rxh.anew.table.business;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.xml.internal.bind.v2.model.core.ID;
import lombok.Data;
import sun.management.resources.agent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午11:07
 * Description:
 */
@TableName("8_pay_order_info_table")
@Data
public class PayOrderInfoTable implements Serializable {
    @TableId
    private Long id ;// 表主键,
    private String platformOrderId;// 平台订单号,
    private String merchantId;// 商户号,
    private String terminalMerId;// 终端号,
    private String merOrderId;// 商户订单号,
    private Integer   identityType;// 身份证类型，1身份证、2护照、3港澳回乡证、4台胞证、5军官证,
    private String   identityNum;// 证件号,
    private String bankCode;// 银行简称,如：中国农业银行： ABC，中国工商银行： ICBC,
    private Integer  bankCardType;// 卡号类型，1借记卡  2信用卡,
    private String   bankCardNum;// 银行卡号,
    private String     bankCardPhone;// 银行卡手机号,
    private String validDate;// 信用卡必填，格式：MMYY,
    private String securityCode;// 信用卡必填，格式：信用卡必填，信用卡背面三位安全码,
    private String  deviceId;// 交易设备号,
    private Integer   deviceType;// 交易设备类型(1-电脑;2-手机;3-其他) 字符串类型,
    private Integer   macAddr;// MAC地址,
    private String  channelId;// 通道id,
    private String  productId;// 产品类型ID,
    private BigDecimal productFee ;// 产品费率,
    private String  currency;// 币种,
    private BigDecimal    amount;// 订单金额,
    private BigDecimal  inAmount;// 入账金额,
    private BigDecimal  payFee;// 扣款手续率,
    private BigDecimal  terFee;// 终端手续费,
    private BigDecimal  channelRate;// 通道费率,
    private BigDecimal  channelFee;// 通道手续费,
    private BigDecimal   agentRate;// 通道费率,
    private BigDecimal   agentFee;// 通道手续费,
    private BigDecimal   merRate;// 商户费率,
    private BigDecimal   merFee;// 商户手续费,
    private BigDecimal   platformIncome;// 单笔平台收入,
    private String   settleCycle;// 结算周期,
    private Integer     settleStatus;// 结算状态，0：已经结算，1：未结算,
    private String  channelRespResult;// 通道响应结果,
    private String  crossRespResult;// cross响应结果,
    private Integer status;// 状态 0：success,1:fail,
    private Date createTime;// 创建时间,
    private Date updateTime;// 更新时间,
}
