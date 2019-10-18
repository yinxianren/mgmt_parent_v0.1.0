package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MerchantRate implements Serializable {
    // 
    private Integer id;

    // 商户域名表主键/商户表主键
    private Integer refId;

    // 1:商户 2:域名
    private Short refType;

    // 常量组PayMode提供
    private String payMode;

    // 常量组PayMode子组提供
    private String payType;

    // 
    private Short bailCycle;

    // 万分比值
    private BigDecimal bailRate;

    // 万分比值
    private BigDecimal rate;

    // 可选,收单机构表主键
    private Integer acquirerId;

    // 可选,收单账户主键
    private Integer accountId;

    // 常量组Status提供
    private Short status;

    // 创建人
    private String creator;

    // 创建时间
    private Date createTime;

    // 最后修改时间
    private String modifier;

    // 最后修改用户
    private Date modifyTime;

    // 二抛收单机构ID
    private Long secondAcquirerId;

    // 二抛收单账户ID
    private Integer secondAccountId;

    // 是否二抛：1否，0是
    private Integer secondAcquirerStatus;

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
     * 商户域名表主键/商户表主键
     *
     * @return ref_id 商户域名表主键/商户表主键
     */
    public Integer getRefId() {
        return refId;
    }

    /**
     * 商户域名表主键/商户表主键
     *
     * @param refId 商户域名表主键/商户表主键
     */
    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    /**
     * 1:商户 2:域名
     *
     * @return ref_type 1:商户 2:域名
     */
    public Short getRefType() {
        return refType;
    }

    /**
     * 1:商户 2:域名
     *
     * @param refType 1:商户 2:域名
     */
    public void setRefType(Short refType) {
        this.refType = refType;
    }

    /**
     * 常量组PayMode提供
     *
     * @return pay_mode 常量组PayMode提供
     */
    public String getPayMode() {
        return payMode;
    }

    /**
     * 常量组PayMode提供
     *
     * @param payMode 常量组PayMode提供
     */
    public void setPayMode(String payMode) {
        this.payMode = payMode == null ? null : payMode.trim();
    }

    /**
     * 常量组PayMode子组提供
     *
     * @return pay_type 常量组PayMode子组提供
     */
    public String getPayType() {
        return payType;
    }

    /**
     * 常量组PayMode子组提供
     *
     * @param payType 常量组PayMode子组提供
     */
    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    /**
     * @return bail_cycle
     */
    public Short getBailCycle() {
        return bailCycle;
    }

    /**
     * @param bailCycle
     */
    public void setBailCycle(Short bailCycle) {
        this.bailCycle = bailCycle;
    }

    /**
     * 万分比值
     *
     * @return bail_rate 万分比值
     */
    public BigDecimal getBailRate() {
        return bailRate;
    }

    /**
     * 万分比值
     *
     * @param bailRate 万分比值
     */
    public void setBailRate(BigDecimal bailRate) {
        this.bailRate = bailRate;
    }

    /**
     * 万分比值
     *
     * @return rate 万分比值
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * 万分比值
     *
     * @param rate 万分比值
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * 可选,收单机构表主键
     *
     * @return acquirer_id 可选,收单机构表主键
     */
    public Integer getAcquirerId() {
        return acquirerId;
    }

    /**
     * 可选,收单机构表主键
     *
     * @param acquirerId 可选,收单机构表主键
     */
    public void setAcquirerId(Integer acquirerId) {
        this.acquirerId = acquirerId;
    }

    /**
     * 可选,收单账户主键
     *
     * @return account_id 可选,收单账户主键
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * 可选,收单账户主键
     *
     * @param accountId 可选,收单账户主键
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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

    /**
     * 二抛收单机构ID
     *
     * @return second_acquirer_id 二抛收单机构ID
     */
    public Long getSecondAcquirerId() {
        return secondAcquirerId;
    }

    /**
     * 二抛收单机构ID
     *
     * @param secondAcquirerId 二抛收单机构ID
     */
    public void setSecondAcquirerId(Long secondAcquirerId) {
        this.secondAcquirerId = secondAcquirerId;
    }

    /**
     * 二抛收单账户ID
     *
     * @return second_account_id 二抛收单账户ID
     */
    public Integer getSecondAccountId() {
        return secondAccountId;
    }

    /**
     * 二抛收单账户ID
     *
     * @param secondAccountId 二抛收单账户ID
     */
    public void setSecondAccountId(Integer secondAccountId) {
        this.secondAccountId = secondAccountId;
    }

    /**
     * 是否二抛：1否，0是
     *
     * @return is_second_acquirer_status 是否二抛：1否，0是
     */
    public Integer getSecondAcquirerStatus() {
        return secondAcquirerStatus;
    }

    /**
     * 是否二抛：1否，0是
     *
     * @param secondAcquirerStatus 是否二抛：1否，0是
     */
    public void setSecondAcquirerStatus(Integer secondAcquirerStatus) {
        this.secondAcquirerStatus = secondAcquirerStatus;
    }
}