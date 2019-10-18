package com.rxh.pojo.statisticsdata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: 王德明
 * @Date: 2018/11/21
 * @Time: 15:17
 * @Project: Management_new
 * @Package: com.rxh.pojo.statisticsdata
 */
public class StatisticsData implements Serializable {


    /**
     *  商户号
     */
    private String merchantId;

    /**
     *  订单数
     */
    private Integer orderCount;

    /**
     *  去重交易数
     */
    private Integer noRepetitionCount;

    /**
     *  成功订单数
     */
    private BigDecimal successOrderCount;

    /**
     *  成功金额
     */
    private BigDecimal successOrderAmount;

    /**
     *  交易金额
     */
    private BigDecimal orderAmount;

    /**
     *  交易日期
     */
    private Date tradeTime;//1557072000000

    /**
     *  卡种交易类型
     */
    private String payType;

    /**
     *  今日退款处理中
     */
    private BigDecimal refundProcessingAmount;

    /**
     *  今日退款待审核
     */
    private BigDecimal refundUnauditedAmount;

    /**
     *  今日拒付总金额
     */
    private BigDecimal refusedAmount;

    /**
     *  今日拒付总笔数
     */
    private Integer refusedCount;

    /**
     *  今日拒付待审核
     */
    private BigDecimal refusedUnauditedAmount;

    /**
     *  今日提现待审核
     */
    private BigDecimal financeUnauditedAmount;

    /**
     *  今日提现待复核
     */
    private BigDecimal financeReviewAmount;

    /**
     *  今日提现待划款
     */
    private BigDecimal financePendingAmount;

    /**
     * 订单变更总数
     */
    private Integer orderChangeCount;

    /**
     * 订单变更总金额
     */
    private BigDecimal orderChangeAmount;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getNoRepetitionCount() {
        return noRepetitionCount;
    }

    public void setNoRepetitionCount(Integer noRepetitionCount) {
        this.noRepetitionCount = noRepetitionCount;
    }

    public BigDecimal getSuccessOrderCount() {
        return successOrderCount;
    }

    public void setSuccessOrderCount(BigDecimal successOrderCount) {
        this.successOrderCount = successOrderCount;
    }

    public BigDecimal getSuccessOrderAmount() {
        return successOrderAmount;
    }

    public void setSuccessOrderAmount(BigDecimal successOrderAmount) {
        this.successOrderAmount = successOrderAmount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getRefundProcessingAmount() {
        return refundProcessingAmount;
    }

    public void setRefundProcessingAmount(BigDecimal refundProcessingAmount) {
        this.refundProcessingAmount = refundProcessingAmount;
    }

    public BigDecimal getRefundUnauditedAmount() {
        return refundUnauditedAmount;
    }

    public void setRefundUnauditedAmount(BigDecimal refundUnauditedAmount) {
        this.refundUnauditedAmount = refundUnauditedAmount;
    }

    public BigDecimal getRefusedAmount() {
        return refusedAmount;
    }

    public void setRefusedAmount(BigDecimal refusedAmount) {
        this.refusedAmount = refusedAmount;
    }

    public Integer getRefusedCount() {
        return refusedCount;
    }

    public void setRefusedCount(Integer refusedCount) {
        this.refusedCount = refusedCount;
    }

    public BigDecimal getRefusedUnauditedAmount() {
        return refusedUnauditedAmount;
    }

    public void setRefusedUnauditedAmount(BigDecimal refusedUnauditedAmount) {
        this.refusedUnauditedAmount = refusedUnauditedAmount;
    }

    public BigDecimal getFinanceUnauditedAmount() {
        return financeUnauditedAmount;
    }

    public void setFinanceUnauditedAmount(BigDecimal financeUnauditedAmount) {
        this.financeUnauditedAmount = financeUnauditedAmount;
    }

    public BigDecimal getFinanceReviewAmount() {
        return financeReviewAmount;
    }

    public void setFinanceReviewAmount(BigDecimal financeReviewAmount) {
        this.financeReviewAmount = financeReviewAmount;
    }

    public BigDecimal getFinancePendingAmount() {
        return financePendingAmount;
    }

    public void setFinancePendingAmount(BigDecimal financePendingAmount) {
        this.financePendingAmount = financePendingAmount;
    }

    public Integer getOrderChangeCount() {
        return orderChangeCount;
    }

    public void setOrderChangeCount(Integer orderChangeCount) {
        this.orderChangeCount = orderChangeCount;
    }

    public BigDecimal getOrderChangeAmount() {
        return orderChangeAmount;
    }

    public void setOrderChangeAmount(BigDecimal orderChangeAmount) {
        this.orderChangeAmount = orderChangeAmount;
    }
}
