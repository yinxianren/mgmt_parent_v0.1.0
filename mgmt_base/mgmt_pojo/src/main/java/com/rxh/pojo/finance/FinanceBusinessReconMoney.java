package com.rxh.pojo.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FinanceBusinessReconMoney implements Serializable {
    // 日报
    public final static short RECON_TYPE_DAILY = 1;
    // 月报
    public final static short RECON_TYPE_MONTH = 2;

    /**
     *
     */
    private Long id;

    /**
     * 商户号
     */
    private Integer merId;

    /**
     * 期初可用余额
     */
    private BigDecimal primitiveUsableAmount;

    /**
     * 期初未释放保证金金额
     */
    private BigDecimal primitiveBondAmount;

    /**
     * 期初未清算金额
     */
    private BigDecimal primitiveNoSettleAmount;

    /**
     * 结算增加金额
     */
    private BigDecimal settlePlusAmount;

    /**
     * 未结算增加金额
     */
    private BigDecimal noSettlePlusAmount;

    /**
     * 手续费增加金额
     */
    private BigDecimal deductPlusAmount;

    /**
     * 释放保证金增加金额
     */
    private BigDecimal bondReleaseAmount;

    /**
     * 未释放保证金增加金额
     */
    private BigDecimal bondNoReleaseAmount;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 拒付金额
     */
    private BigDecimal protestAmount;

    /**
     * 拒付处理费金额
     */
    private BigDecimal protestChargeAmount;

    /**
     * 已提待审核金额
     */
    private BigDecimal drawingNoAuditAmount;

    /**
     * 待审核金额
     */
    private BigDecimal drawingAuditAmount;

    /**
     * 可用余额合计
     */
    private BigDecimal finalUsableAmount;

    /**
     * 保证金金额合计
     */
    private BigDecimal finalBondAmount;

    /**
     * 未结算金额合计
     */
    private BigDecimal finalNoSettleAmount;

    /**
     * 对账日期
     */
    private Date reconTime;

    /**
     * 未结算释放保证金金额
     */
    private BigDecimal noSettleBondReleaseAmount;

    /**
     * 对账类型：1：日账单 2：月账单
     */
    private Short reconType;

    /**
     * 退款手续费
     */
    private BigDecimal refundChargeAmount;

    /**
     * 退款归还手续费
     */
    private BigDecimal returnDeductAmount;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 商户号
     *
     * @return mer_id 商户号
     */
    public Integer getMerId() {
        return merId;
    }

    /**
     * 商户号
     *
     * @param merId 商户号
     */
    public void setMerId(Integer merId) {
        this.merId = merId;
    }

    /**
     * 期初可用余额
     *
     * @return primitive_usable_amount 期初可用余额
     */
    public BigDecimal getPrimitiveUsableAmount() {
        return primitiveUsableAmount;
    }

    /**
     * 期初可用余额
     *
     * @param primitiveUsableAmount 期初可用余额
     */
    public void setPrimitiveUsableAmount(BigDecimal primitiveUsableAmount) {
        this.primitiveUsableAmount = primitiveUsableAmount;
    }

    /**
     * 期初未释放保证金金额
     *
     * @return primitive_bond_amount 期初未释放保证金金额
     */
    public BigDecimal getPrimitiveBondAmount() {
        return primitiveBondAmount;
    }

    /**
     * 期初未释放保证金金额
     *
     * @param primitiveBondAmount 期初未释放保证金金额
     */
    public void setPrimitiveBondAmount(BigDecimal primitiveBondAmount) {
        this.primitiveBondAmount = primitiveBondAmount;
    }

    /**
     * 期初未清算金额
     *
     * @return primitive_no_settle_amount 期初未清算金额
     */
    public BigDecimal getPrimitiveNoSettleAmount() {
        return primitiveNoSettleAmount;
    }

    /**
     * 期初未清算金额
     *
     * @param primitiveNoSettleAmount 期初未清算金额
     */
    public void setPrimitiveNoSettleAmount(BigDecimal primitiveNoSettleAmount) {
        this.primitiveNoSettleAmount = primitiveNoSettleAmount;
    }

    /**
     * 结算增加金额
     *
     * @return settle_plus_amount 结算增加金额
     */
    public BigDecimal getSettlePlusAmount() {
        return settlePlusAmount;
    }

    /**
     * 结算增加金额
     *
     * @param settlePlusAmount 结算增加金额
     */
    public void setSettlePlusAmount(BigDecimal settlePlusAmount) {
        this.settlePlusAmount = settlePlusAmount;
    }

    /**
     * 未结算增加金额
     *
     * @return no_settle_plus_amount 未结算增加金额
     */
    public BigDecimal getNoSettlePlusAmount() {
        return noSettlePlusAmount;
    }

    /**
     * 未结算增加金额
     *
     * @param noSettlePlusAmount 未结算增加金额
     */
    public void setNoSettlePlusAmount(BigDecimal noSettlePlusAmount) {
        this.noSettlePlusAmount = noSettlePlusAmount;
    }

    /**
     * 手续费增加金额
     *
     * @return deduct_plus_amount 手续费增加金额
     */
    public BigDecimal getDeductPlusAmount() {
        return deductPlusAmount;
    }

    /**
     * 手续费增加金额
     *
     * @param deductPlusAmount 手续费增加金额
     */
    public void setDeductPlusAmount(BigDecimal deductPlusAmount) {
        this.deductPlusAmount = deductPlusAmount;
    }

    /**
     * 释放保证金增加金额
     *
     * @return bond_release_amount 释放保证金增加金额
     */
    public BigDecimal getBondReleaseAmount() {
        return bondReleaseAmount;
    }

    /**
     * 释放保证金增加金额
     *
     * @param bondReleaseAmount 释放保证金增加金额
     */
    public void setBondReleaseAmount(BigDecimal bondReleaseAmount) {
        this.bondReleaseAmount = bondReleaseAmount;
    }

    /**
     * 未释放保证金增加金额
     *
     * @return bond_no_release_amount 未释放保证金增加金额
     */
    public BigDecimal getBondNoReleaseAmount() {
        return bondNoReleaseAmount;
    }

    /**
     * 未释放保证金增加金额
     *
     * @param bondNoReleaseAmount 未释放保证金增加金额
     */
    public void setBondNoReleaseAmount(BigDecimal bondNoReleaseAmount) {
        this.bondNoReleaseAmount = bondNoReleaseAmount;
    }

    /**
     * 退款金额
     *
     * @return refund_amount 退款金额
     */
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    /**
     * 退款金额
     *
     * @param refundAmount 退款金额
     */
    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * 拒付金额
     *
     * @return protest_amount 拒付金额
     */
    public BigDecimal getProtestAmount() {
        return protestAmount;
    }

    /**
     * 拒付金额
     *
     * @param protestAmount 拒付金额
     */
    public void setProtestAmount(BigDecimal protestAmount) {
        this.protestAmount = protestAmount;
    }

    /**
     * 拒付处理费金额
     *
     * @return protest_charge_amount 拒付处理费金额
     */
    public BigDecimal getProtestChargeAmount() {
        return protestChargeAmount;
    }

    /**
     * 拒付处理费金额
     *
     * @param protestChargeAmount 拒付处理费金额
     */
    public void setProtestChargeAmount(BigDecimal protestChargeAmount) {
        this.protestChargeAmount = protestChargeAmount;
    }

    /**
     * 已提待审核金额
     *
     * @return drawing_no_audit_amount 已提待审核金额
     */
    public BigDecimal getDrawingNoAuditAmount() {
        return drawingNoAuditAmount;
    }

    /**
     * 已提待审核金额
     *
     * @param drawingNoAuditAmount 已提待审核金额
     */
    public void setDrawingNoAuditAmount(BigDecimal drawingNoAuditAmount) {
        this.drawingNoAuditAmount = drawingNoAuditAmount;
    }

    /**
     * 待审核金额
     *
     * @return drawing_audit_amount 待审核金额
     */
    public BigDecimal getDrawingAuditAmount() {
        return drawingAuditAmount;
    }

    /**
     * 待审核金额
     *
     * @param drawingAuditAmount 待审核金额
     */
    public void setDrawingAuditAmount(BigDecimal drawingAuditAmount) {
        this.drawingAuditAmount = drawingAuditAmount;
    }

    /**
     * 可用余额合计
     *
     * @return final_usable_amount 可用余额合计
     */
    public BigDecimal getFinalUsableAmount() {
        return finalUsableAmount;
    }

    /**
     * 可用余额合计
     *
     * @param finalUsableAmount 可用余额合计
     */
    public void setFinalUsableAmount(BigDecimal finalUsableAmount) {
        this.finalUsableAmount = finalUsableAmount;
    }

    /**
     * 保证金金额合计
     *
     * @return final_bond_amount 保证金金额合计
     */
    public BigDecimal getFinalBondAmount() {
        return finalBondAmount;
    }

    /**
     * 保证金金额合计
     *
     * @param finalBondAmount 保证金金额合计
     */
    public void setFinalBondAmount(BigDecimal finalBondAmount) {
        this.finalBondAmount = finalBondAmount;
    }

    /**
     * 未结算金额合计
     *
     * @return final_no_settle_amount 未结算金额合计
     */
    public BigDecimal getFinalNoSettleAmount() {
        return finalNoSettleAmount;
    }

    /**
     * 未结算金额合计
     *
     * @param finalNoSettleAmount 未结算金额合计
     */
    public void setFinalNoSettleAmount(BigDecimal finalNoSettleAmount) {
        this.finalNoSettleAmount = finalNoSettleAmount;
    }

    /**
     * 对账日期
     *
     * @return recon_time 对账日期
     */
    public Date getReconTime() {
        return reconTime;
    }

    /**
     * 对账日期
     *
     * @param reconTime 对账日期
     */
    public void setReconTime(Date reconTime) {
        this.reconTime = reconTime;
    }

    /**
     * 未结算释放保证金金额
     *
     * @return no_settle_bond_release_amount 未结算释放保证金金额
     */
    public BigDecimal getNoSettleBondReleaseAmount() {
        return noSettleBondReleaseAmount;
    }

    /**
     * 未结算释放保证金金额
     *
     * @param noSettleBondReleaseAmount 未结算释放保证金金额
     */
    public void setNoSettleBondReleaseAmount(BigDecimal noSettleBondReleaseAmount) {
        this.noSettleBondReleaseAmount = noSettleBondReleaseAmount;
    }

    /**
     * 对账类型：1：日账单 2：月账单
     *
     * @return recon_type 对账类型：1：日账单 2：月账单
     */
    public Short getReconType() {
        return reconType;
    }

    /**
     * 对账类型：1：日账单 2：月账单
     *
     * @param reconType 对账类型：1：日账单 2：月账单
     */
    public void setReconType(Short reconType) {
        this.reconType = reconType;
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
     * 退款归还手续费
     *
     * @return return_deduct_amount 退款归还手续费
     */
    public BigDecimal getReturnDeductAmount() {
        return returnDeductAmount;
    }

    /**
     * 退款归还手续费
     *
     * @param returnDeductAmount 退款归还手续费
     */
    public void setReturnDeductAmount(BigDecimal returnDeductAmount) {
        this.returnDeductAmount = returnDeductAmount;
    }
}