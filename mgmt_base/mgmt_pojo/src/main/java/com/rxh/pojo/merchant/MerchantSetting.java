package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantSetting implements Serializable {
    // 与是商户信息表mer_id同值
    private String id;

    // 业务邮箱
    private String email;

    // 等级,0：一级 1：二级 2：三级
    private Short level;

    // 开户费
    private Integer openFee;

    // 年费
    private Integer yearFee;

    // 0:手动 1:自动
    private Short autoRenew;

    // 合同开始时间
    private Date startTime;

    // 合同结束时间
    private Date endTime;

    // 代理商表主键/业务员表主键
    private Integer refId;

    // 0:总部,1:总部业务员 2:代理商 3:代理商业务员
    private Short refType;

    // 币种(常量组Currency提供)
    private String transferCurrency;

    // 单笔(取整)
    private Integer transferMax;

    // 单笔(取整)
    private Short transferFee;

    // 单笔(取整)
    private Short dishonor;

    // 退款手续费(人民币)
    private Short refundFee;

    // 0 填写快递号 1 上传发货单图 2皆可
    private Short trackType;

    // 0:需要 1:不需要
    private Short trackAudit;

    // 0:需要 1:不需要
    private Short correctDistribute;

    // 每月结算划款周期(可多填 以,分隔)
    private Short settleCycle;

    // 每月结算划款日期(可多填 以,分隔)
    private String settleDay;

    // 开户银行
    private String bankName;

    // 开户人名
    private String bankOwner;

    // 开户账户
    private String bankAccount;

    // 最后操作人
    private String modifier;

    // 最后修改时间
    private Date modifyTime;

    // 引入商户的业务人员
    private String salesmanName;

    /**
     * 与是商户信息表mer_id同值
     *
     * @return id 与是商户信息表mer_id同值
     */
    public String getId() {
        return id;
    }

    /**
     * 与是商户信息表mer_id同值
     *
     * @param id 与是商户信息表mer_id同值
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 业务邮箱
     *
     * @return email 业务邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 业务邮箱
     *
     * @param email 业务邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 等级,0：一级 1：二级 2：三级
     *
     * @return level 等级,0：一级 1：二级 2：三级
     */
    public Short getLevel() {
        return level;
    }

    /**
     * 等级,0：一级 1：二级 2：三级
     *
     * @param level 等级,0：一级 1：二级 2：三级
     */
    public void setLevel(Short level) {
        this.level = level;
    }

    /**
     * 开户费
     *
     * @return open_fee 开户费
     */
    public Integer getOpenFee() {
        return openFee;
    }

    /**
     * 开户费
     *
     * @param openFee 开户费
     */
    public void setOpenFee(Integer openFee) {
        this.openFee = openFee;
    }

    /**
     * 年费
     *
     * @return year_fee 年费
     */
    public Integer getYearFee() {
        return yearFee;
    }

    /**
     * 年费
     *
     * @param yearFee 年费
     */
    public void setYearFee(Integer yearFee) {
        this.yearFee = yearFee;
    }

    /**
     * 0:手动 1:自动
     *
     * @return auto_renew 0:手动 1:自动
     */
    public Short getAutoRenew() {
        return autoRenew;
    }

    /**
     * 0:手动 1:自动
     *
     * @param autoRenew 0:手动 1:自动
     */
    public void setAutoRenew(Short autoRenew) {
        this.autoRenew = autoRenew;
    }

    /**
     * 合同开始时间
     *
     * @return start_time 合同开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 合同开始时间
     *
     * @param startTime 合同开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 合同结束时间
     *
     * @return end_time 合同结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 合同结束时间
     *
     * @param endTime 合同结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 代理商表主键/业务员表主键
     *
     * @return ref_id 代理商表主键/业务员表主键
     */
    public Integer getRefId() {
        return refId;
    }

    /**
     * 代理商表主键/业务员表主键
     *
     * @param refId 代理商表主键/业务员表主键
     */
    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    /**
     * 0:总部,1:总部业务员 2:代理商 3:代理商业务员
     *
     * @return ref_type 0:总部,1:总部业务员 2:代理商 3:代理商业务员
     */
    public Short getRefType() {
        return refType;
    }

    /**
     * 0:总部,1:总部业务员 2:代理商 3:代理商业务员
     *
     * @param refType 0:总部,1:总部业务员 2:代理商 3:代理商业务员
     */
    public void setRefType(Short refType) {
        this.refType = refType;
    }

    /**
     * 币种(常量组Currency提供)
     *
     * @return transfer_currency 币种(常量组Currency提供)
     */
    public String getTransferCurrency() {
        return transferCurrency;
    }

    /**
     * 币种(常量组Currency提供)
     *
     * @param transferCurrency 币种(常量组Currency提供)
     */
    public void setTransferCurrency(String transferCurrency) {
        this.transferCurrency = transferCurrency == null ? null : transferCurrency.trim();
    }

    /**
     * 单笔(取整)
     *
     * @return transfer_max 单笔(取整)
     */
    public Integer getTransferMax() {
        return transferMax;
    }

    /**
     * 单笔(取整)
     *
     * @param transferMax 单笔(取整)
     */
    public void setTransferMax(Integer transferMax) {
        this.transferMax = transferMax;
    }

    /**
     * 单笔(取整)
     *
     * @return transfer_fee 单笔(取整)
     */
    public Short getTransferFee() {
        return transferFee;
    }

    /**
     * 单笔(取整)
     *
     * @param transferFee 单笔(取整)
     */
    public void setTransferFee(Short transferFee) {
        this.transferFee = transferFee;
    }

    /**
     * 单笔(取整)
     *
     * @return dishonor 单笔(取整)
     */
    public Short getDishonor() {
        return dishonor;
    }

    /**
     * 单笔(取整)
     *
     * @param dishonor 单笔(取整)
     */
    public void setDishonor(Short dishonor) {
        this.dishonor = dishonor;
    }

    /**
     * 退款手续费(人民币)
     *
     * @return refundFee 退款手续费(人民币)
     */
    public Short getRefundFee() {
        return refundFee;
    }

    /**
     * 退款手续费(人民币)
     *
     * @param refundFee 退款手续费(人民币)
     */
    public void setRefundFee(Short refundFee) {
        this.refundFee = refundFee;
    }

    /**
     * 0 填写快递号 1 上传发货单图 2皆可
     *
     * @return track_type 0 填写快递号 1 上传发货单图 2皆可
     */
    public Short getTrackType() {
        return trackType;
    }

    /**
     * 0 填写快递号 1 上传发货单图 2皆可
     *
     * @param trackType 0 填写快递号 1 上传发货单图 2皆可
     */
    public void setTrackType(Short trackType) {
        this.trackType = trackType;
    }

    /**
     * 0:需要 1:不需要
     *
     * @return track_audit 0:需要 1:不需要
     */
    public Short getTrackAudit() {
        return trackAudit;
    }

    /**
     * 0:需要 1:不需要
     *
     * @param trackAudit 0:需要 1:不需要
     */
    public void setTrackAudit(Short trackAudit) {
        this.trackAudit = trackAudit;
    }

    /**
     * 0:需要 1:不需要
     *
     * @return correct_distribute 0:需要 1:不需要
     */
    public Short getCorrectDistribute() {
        return correctDistribute;
    }

    /**
     * 0:需要 1:不需要
     *
     * @param correctDistribute 0:需要 1:不需要
     */
    public void setCorrectDistribute(Short correctDistribute) {
        this.correctDistribute = correctDistribute;
    }

    /**
     * 每月结算划款周期(可多填 以,分隔)
     *
     * @return settle_cycle 每月结算划款周期(可多填 以,分隔)
     */
    public Short getSettleCycle() {
        return settleCycle;
    }

    /**
     * 每月结算划款周期(可多填 以,分隔)
     *
     * @param settleCycle 每月结算划款周期(可多填 以,分隔)
     */
    public void setSettleCycle(Short settleCycle) {
        this.settleCycle = settleCycle;
    }

    /**
     * 每月结算划款日期(可多填 以,分隔)
     *
     * @return settle_day 每月结算划款日期(可多填 以,分隔)
     */
    public String getSettleDay() {
        return settleDay;
    }

    /**
     * 每月结算划款日期(可多填 以,分隔)
     *
     * @param settleDay 每月结算划款日期(可多填 以,分隔)
     */
    public void setSettleDay(String settleDay) {
        this.settleDay = settleDay == null ? null : settleDay.trim();
    }

    /**
     * 开户银行
     *
     * @return bank_name 开户银行
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 开户银行
     *
     * @param bankName 开户银行
     */
    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    /**
     * 开户人名
     *
     * @return bank_owner 开户人名
     */
    public String getBankOwner() {
        return bankOwner;
    }

    /**
     * 开户人名
     *
     * @param bankOwner 开户人名
     */
    public void setBankOwner(String bankOwner) {
        this.bankOwner = bankOwner == null ? null : bankOwner.trim();
    }

    /**
     * 开户账户
     *
     * @return bank_account 开户账户
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * 开户账户
     *
     * @param bankAccount 开户账户
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    /**
     * 最后操作人
     *
     * @return modifier 最后操作人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 最后操作人
     *
     * @param modifier 最后操作人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 最后修改时间
     *
     * @return modify_time 最后修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 最后修改时间
     *
     * @param modifyTime 最后修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 引入商户的业务人员
     *
     * @return salesman_name 引入商户的业务人员
     */
    public String getSalesmanName() {
        return salesmanName;
    }

    /**
     * 引入商户的业务人员
     *
     * @param salesmanName 引入商户的业务人员
     */
    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName == null ? null : salesmanName.trim();
    }
}