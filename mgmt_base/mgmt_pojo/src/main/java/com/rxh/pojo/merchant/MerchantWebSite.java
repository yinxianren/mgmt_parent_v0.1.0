package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantWebSite implements Serializable {
    public final static short INDUSTRY_ID_MATERIAL_OBJECT = 0;
    public final static short INDUSTRY_ID_FICTITIOUS = 1;

    //域名状态 0:禁用 1:启用 2:未审核 3:删除
    public final static short STATUS_UNAUDITED = 2;
    public final static short STATUS_DELETE = 3;

    // 
    private Integer id;

    // 商户表主键
    private Integer merId;

    // 网址域名
    private String siteUrl;

    // 行业表主键
    private Short industryId;

    // 产品类型编码
    private Long productTypeId;

    // 每月结算划款周期(可多填 以,分隔)
    private Short settleCycle;

    // 每月结算划款日期(可多填 以,分隔)
    private String settleDay;

    // 常量组Status提供
    private Short status;

    // 说明
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
     * 
     * @return id 
    */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
    */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 商户表主键
     * @return mer_id 商户表主键
    */
    public Integer getMerId() {
        return merId;
    }

    /**
     * 商户表主键
     * @param merId 商户表主键
    */
    public void setMerId(Integer merId) {
        this.merId = merId;
    }

    /**
     * 网址域名
     * @return site_url 网址域名
    */
    public String getSiteUrl() {
        return siteUrl;
    }

    /**
     * 网址域名
     * @param siteUrl 网址域名
    */
    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl == null ? null : siteUrl.trim();
    }

    /**
     * 行业表主键
     * @return industry_id 行业表主键
    */
    public Short getIndustryId() {
        return industryId;
    }

    /**
     * 行业表主键
     * @param industryId 行业表主键
    */
    public void setIndustryId(Short industryId) {
        this.industryId = industryId;
    }

    /**
     * 产品类型编码
     * @return product_type_id 产品类型编码
    */
    public Long getProductTypeId() {
        return productTypeId;
    }

    /**
     * 产品类型编码
     * @param productTypeId 产品类型编码
    */
    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    /**
     * 每月结算划款周期(可多填 以,分隔)
     * @return settle_cycle 每月结算划款周期(可多填 以,分隔)
    */
    public Short getSettleCycle() {
        return settleCycle;
    }

    /**
     * 每月结算划款周期(可多填 以,分隔)
     * @param settleCycle 每月结算划款周期(可多填 以,分隔)
    */
    public void setSettleCycle(Short settleCycle) {
        this.settleCycle = settleCycle;
    }

    /**
     * 每月结算划款日期(可多填 以,分隔)
     * @return settle_day 每月结算划款日期(可多填 以,分隔)
    */
    public String getSettleDay() {
        return settleDay;
    }

    /**
     * 每月结算划款日期(可多填 以,分隔)
     * @param settleDay 每月结算划款日期(可多填 以,分隔)
    */
    public void setSettleDay(String settleDay) {
        this.settleDay = settleDay == null ? null : settleDay.trim();
    }

    /**
     * 常量组Status提供
     * @return status 常量组Status提供
    */
    public Short getStatus() {
        return status;
    }

    /**
     * 常量组Status提供
     * @param status 常量组Status提供
    */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * 说明
     * @return remark 说明
    */
    public String getRemark() {
        return remark;
    }

    /**
     * 说明
     * @param remark 说明
    */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 创建人
     * @return creator 创建人
    */
    public String getCreator() {
        return creator;
    }

    /**
     * 创建人
     * @param creator 创建人
    */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * 创建时间
     * @return create_time 创建时间
    */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
    */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 最后修改时间
     * @return modifier 最后修改时间
    */
    public String getModifier() {
        return modifier;
    }

    /**
     * 最后修改时间
     * @param modifier 最后修改时间
    */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 最后修改用户
     * @return modify_time 最后修改用户
    */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 最后修改用户
     * @param modifyTime 最后修改用户
    */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}