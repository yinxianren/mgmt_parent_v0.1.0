package com.rxh.pojo.risk;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RiskQuota implements Serializable {
    public final static short REF_TYPE_COMMON = 0;
    public final static short REF_TYPE_ACQUIRER_ID = 1;
    public final static short REF_TYPE_MERCHANT_ID = 2;
    public final static short REF_TYPE_SITE_ID = 3;
    public final static int NUMBER_OF_REF_TYPE = 4;
    public final static int LIMIT_TYPE_ORDER_AMOUNT = 0;
    public final static int LIMIT_TYPE_DAY = 1;
    public final static int LIMIT_TYPE_MONTH = 2;
    public final static int LIMIT_TYPE_YEAR = 3;
    public final static int NUMBER_OF_LIMIT_TYPE = 3;
    //
    private Long id;

    // 商户域名表主键/商户表主键/收单机构表主键
    private Integer refId;

    // 0: 通用 1:收单机构 2:商户 3:域名
    private Short refType;

    // 0:笔 1:日 2:月 3:年
    private Short limitType;

    private String payType;

    // 常量组Currency提供
    private String currency;

    //
    private Integer amount;

    // 提醒值,限定额的百分比
    private BigDecimal warning;

    // 常量组Status提供
    private Short status;

    //
    private String remark;

    // 创建人
    private String creator;

    // 创建时间
    private Date createTime;

    // 最后修改时间
    private String modifier;

    // 最后修改用户
    private Date modifyTime;

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
     * 商户域名表主键/商户表主键/收单机构表主键
     *
     * @return ref_id 商户域名表主键/商户表主键/收单机构表主键
     */
    public Integer getRefId() {
        return refId;
    }

    /**
     * 商户域名表主键/商户表主键/收单机构表主键
     *
     * @param refId 商户域名表主键/商户表主键/收单机构表主键
     */
    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    /**
     * 0: 通用 1:收单机构 2:商户 3:域名
     *
     * @return ref_type 0: 通用 1:收单机构 2:商户 3:域名
     */
    public Short getRefType() {
        return refType;
    }

    /**
     * 0: 通用 1:收单机构 2:商户 3:域名
     *
     * @param refType 0: 通用 1:收单机构 2:商户 3:域名
     */
    public void setRefType(Short refType) {
        this.refType = refType;
    }

    /**
     * 0:笔 1:日 2:月 3:年
     *
     * @return limit_type 0:笔 1:日 2:月 3:年
     */
    public Short getLimitType() {
        return limitType;
    }

    /**
     * 0:笔 1:日 2:月 3:年
     *
     * @param limitType 0:笔 1:日 2:月 3:年
     */
    public void setLimitType(Short limitType) {
        this.limitType = limitType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 常量组Currency提供
     *
     * @return currency 常量组Currency提供
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 常量组Currency提供
     *
     * @param currency 常量组Currency提供
     */
    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    /**
     * @return amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * 提醒值,限定额的百分比
     *
     * @return warning 提醒值,限定额的百分比
     */
    public BigDecimal getWarning() {
        return warning;
    }

    /**
     * 提醒值,限定额的百分比
     *
     * @param warning 提醒值,限定额的百分比
     */
    public void setWarning(BigDecimal warning) {
        this.warning = warning;
    }

    /**
     * 常量组Status提供
     *
     * @return status 常量组Status提供
     */
    public Short getStatus() {
        return status;
    }

    /**
     * 常量组Status提供
     *
     * @param status 常量组Status提供
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 创建人
     *
     * @return creator 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
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
     * 最后修改时间
     *
     * @return modifier 最后修改时间
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 最后修改时间
     *
     * @param modifier 最后修改时间
     */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 最后修改用户
     *
     * @return modify_time 最后修改用户
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 最后修改用户
     *
     * @param modifyTime 最后修改用户
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}