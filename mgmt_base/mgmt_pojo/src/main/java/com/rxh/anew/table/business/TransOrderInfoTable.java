package com.rxh.anew.table.business;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午11:26
 * Description:
 */
@TableName("9_trans_order_info_table")
@Data
public class TransOrderInfoTable implements Serializable {
    @TableId
    private Long id;//表主键
    private String platformOrderId ;//平台订单号
    private String merchantId ;//商户号
    private String terminalMerId ;//终端号
    private String merOrderId ;//商户订单号
    private String orgMerOrderId ;//原交易商户订单号
    private Integer identityType ;//身份证类型，1身份证、2护照、3港澳回乡证、4台胞证、5军官证
    private String identityNum ;//证件号
    private String bankCode ;//银行简称如：中国农业银行： ABC，中国工商银行： ICBC
    private Integer  bankCardType ;//卡号类型，1借记卡  2信用卡
    private String bankCardNum ;//银行卡号
    private String bankCardPhone ;//银行卡手机号
    private String channelId ;//通道id
    private String productId ;//产品类型ID
    private String productFee ;//产品费率
    private String currency;//币种
    private BigDecimal amount;//订单金额
    private BigDecimal outAmount ;//出账金额
    private BigDecimal backFee ;//单笔固定金额
    private BigDecimal  terRate ;//终端费率
    private BigDecimal terFee ;//终端手续费
    private BigDecimal channelRate ;//通道费率
    private BigDecimal channelFee ;//通道手续费
    private BigDecimal  agentRate ;//代理商费率
    private BigDecimal agentFee ;//代理商手续费
    private BigDecimal merRate ;//商户费率
    private BigDecimal merFee ;//商户手续费
    private BigDecimal platformIncome ;//单笔平台收入
    private String settleCycle;//结算周期
    private Integer  settleStatus;//结算状态，0：已经结算，1：未结算
    private String channelRespResult;//通道响应结果
    private String crossRespResult;//cross响应结果
    private Integer  status;//状态 0：success1:fail
    private Date createTime;//创建时间
    private Date updateTime;//更新时间

}
