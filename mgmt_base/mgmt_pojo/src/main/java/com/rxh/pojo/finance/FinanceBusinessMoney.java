package com.rxh.pojo.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FinanceBusinessMoney implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 总金额
     */
    private BigDecimal amount;

    /**
     * 可用金额
     */
    private BigDecimal usableAmount;

    /**
     * 不可用金额
     */
    private BigDecimal disabledAmount;

    /**
     * 待审核金额
     */
    private BigDecimal auditAmount;

    /**
     * 成功提现金额
     */
    private BigDecimal successAmount;

    /**
     * 账面余额
     */
    private BigDecimal bookBalance;

    /**
     * 币种
     */
    private String currency;

    /**
     * 商户号
     */
    private Integer refId;

    /**
     * 拒付总金额
     */
    private BigDecimal protestAmount;

    /**
     * 退款总金额
     */
    private BigDecimal refundAmount;

    /**
     * 拒付处理费总金额
     */
    private BigDecimal protestChargeAmount;

    /**
     * 手续费总金额
     */
    private BigDecimal deduct;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 保证金未释放总金额
     */
    private BigDecimal bond;

    /**
     * 退款手续费
     */
    private BigDecimal refundChargeAmount;

    /**
     * 归还手续费
     */
    private BigDecimal returnDeductAmount;

    /**
     * core_balance_change 除冻结资金外，其他类别的资金变动统一计算到‘其他费用’（商户余额表增加此字段‘其他费用’：others）
     */
    private BigDecimal other;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 总金额
     *
     * @return amount 总金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 总金额
     *
     * @param amount 总金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 可用金额
     *
     * @return usable_amount 可用金额
     */
    public BigDecimal getUsableAmount() {
        return usableAmount;
    }

    /**
     * 可用金额
     *
     * @param usableAmount 可用金额
     */
    public void setUsableAmount(BigDecimal usableAmount) {
        this.usableAmount = usableAmount;
    }

    /**
     * 不可用金额
     *
     * @return disabled_amount 不可用金额
     */
    public BigDecimal getDisabledAmount() {
        return disabledAmount;
    }

    /**
     * 不可用金额
     *
     * @param disabledAmount 不可用金额
     */
    public void setDisabledAmount(BigDecimal disabledAmount) {
        this.disabledAmount = disabledAmount;
    }

    /**
     * 待审核金额
     *
     * @return audit_amount 待审核金额
     */
    public BigDecimal getAuditAmount() {
        return auditAmount;
    }

    /**
     * 待审核金额
     *
     * @param auditAmount 待审核金额
     */
    public void setAuditAmount(BigDecimal auditAmount) {
        this.auditAmount = auditAmount;
    }

    /**
     * 成功提现金额
     *
     * @return success_amount 成功提现金额
     */
    public BigDecimal getSuccessAmount() {
        return successAmount;
    }

    /**
     * 成功提现金额
     *
     * @param successAmount 成功提现金额
     */
    public void setSuccessAmount(BigDecimal successAmount) {
        this.successAmount = successAmount;
    }

    /**
     * 账面余额
     *
     * @return book_balance 账面余额
     */
    public BigDecimal getBookBalance() {
        return bookBalance;
    }

    /**
     * 账面余额
     *
     * @param bookBalance 账面余额
     */
    public void setBookBalance(BigDecimal bookBalance) {
        this.bookBalance = bookBalance;
    }

    /**
     * 币种
     *
     * @return currency 币种
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 币种
     *
     * @param currency 币种
     */
    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    /**
     * 商户号
     *
     * @return ref_id 商户号
     */
    public Integer getRefId() {
        return refId;
    }

    /**
     * 商户号
     *
     * @param refId 商户号
     */
    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    /**
     * 拒付总金额
     *
     * @return protest_amount 拒付总金额
     */
    public BigDecimal getProtestAmount() {
        return protestAmount;
    }

    /**
     * 拒付总金额
     *
     * @param protestAmount 拒付总金额
     */
    public void setProtestAmount(BigDecimal protestAmount) {
        this.protestAmount = protestAmount;
    }

    /**
     * 退款总金额
     *
     * @return refund_amount 退款总金额
     */
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    /**
     * 退款总金额
     *
     * @param refundAmount 退款总金额
     */
    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * 拒付处理费总金额
     *
     * @return protest_charge_amount 拒付处理费总金额
     */
    public BigDecimal getProtestChargeAmount() {
        return protestChargeAmount;
    }

    /**
     * 拒付处理费总金额
     *
     * @param protestChargeAmount 拒付处理费总金额
     */
    public void setProtestChargeAmount(BigDecimal protestChargeAmount) {
        this.protestChargeAmount = protestChargeAmount;
    }

    /**
     * 手续费总金额
     *
     * @return deduct 手续费总金额
     */
    public BigDecimal getDeduct() {
        return deduct;
    }

    /**
     * 手续费总金额
     *
     * @param deduct 手续费总金额
     */
    public void setDeduct(BigDecimal deduct) {
        this.deduct = deduct;
    }

    /**
     * 创建时间
     *
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 保证金未释放总金额
     *
     * @return bond 保证金未释放总金额
     */
    public BigDecimal getBond() {
        return bond;
    }

    /**
     * 保证金未释放总金额
     *
     * @param bond 保证金未释放总金额
     */
    public void setBond(BigDecimal bond) {
        this.bond = bond;
    }

    /**
     * 退款手续费
     *
     * @return refund_charge_amount 退款手续费
     */
    public BigDecimal getRefundChargeAmount() {
        return refundChargeAmount;
    }

    /**
     * 退款手续费
     *
     * @param refundChargeAmount 退款手续费
     */
    public void setRefundChargeAmount(BigDecimal refundChargeAmount) {
        this.refundChargeAmount = refundChargeAmount;
    }

    /**
     * 归还手续费
     *
     * @return return_deduct_amount 归还手续费
     */
    public BigDecimal getReturnDeductAmount() {
        return returnDeductAmount;
    }

    /**
     * 归还手续费
     *
     * @param returnDeductAmount 归还手续费
     */
    public void setReturnDeductAmount(BigDecimal returnDeductAmount) {
        this.returnDeductAmount = returnDeductAmount;
    }

    /**
     * core_balance_change 除冻结资金外，其他类别的资金变动统一计算到‘其他费用’（商户余额表增加此字段‘其他费用’：others）
     *
     * @return other core_balance_change 除冻结资金外，其他类别的资金变动统一计算到‘其他费用’（商户余额表增加此字段‘其他费用’：others）
     */
    public BigDecimal getOther() {
        return other;
    }

    /**
     * core_balance_change 除冻结资金外，其他类别的资金变动统一计算到‘其他费用’（商户余额表增加此字段‘其他费用’：others）
     *
     * @param other core_balance_change 除冻结资金外，其他类别的资金变动统一计算到‘其他费用’（商户余额表增加此字段‘其他费用’：others）
     */
    public void setOther(BigDecimal other) {
        this.other = other;
    }
}