package com.rxh.pojo.risk;

import java.io.Serializable;
import java.util.Date;

public class RiskRefuse implements Serializable {
     // 
    private Long id;

     // 商户域名表主键/商户表主键/收单机构表主键
    private Integer refId;

     // 0: 通用 1:收单机构 2:商户 3:域名
    private Short refType;

     // 常量组stop提供
    private String element;

     //  : 国家 - 常量Country的first_value, 发卡行 - 发卡行表的主键, 商务卡 - true, false
    private String elementValue;

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
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 商户域名表主键/商户表主键/收单机构表主键
     * @return ref_id 商户域名表主键/商户表主键/收单机构表主键
     */
    public Integer getRefId() {
        return refId;
    }

    /**
     * 商户域名表主键/商户表主键/收单机构表主键
     * @param refId 商户域名表主键/商户表主键/收单机构表主键
     */
    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    /**
     * 0: 通用 1:收单机构 2:商户 3:域名
     * @return ref_type 0: 通用 1:收单机构 2:商户 3:域名
     */
    public Short getRefType() {
        return refType;
    }

    /**
     * 0: 通用 1:收单机构 2:商户 3:域名
     * @param refType 0: 通用 1:收单机构 2:商户 3:域名
     */
    public void setRefType(Short refType) {
        this.refType = refType;
    }

    /**
     * 常量组stop提供
     * @return element 常量组stop提供
     */
    public String getElement() {
        return element;
    }

    /**
     * 常量组stop提供
     * @param element 常量组stop提供
     */
    public void setElement(String element) {
        this.element = element == null ? null : element.trim();
    }

    /**
     *  : 国家 - 常量Country的first_value, 发卡行 - 发卡行表的主键, 商务卡 - true, false
     * @return element_value  : 国家 - 常量Country的first_value, 发卡行 - 发卡行表的主键, 商务卡 - true, false
     */
    public String getElementValue() {
        return elementValue;
    }

    /**
     *  : 国家 - 常量Country的first_value, 发卡行 - 发卡行表的主键, 商务卡 - true, false
     * @param elementValue  : 国家 - 常量Country的first_value, 发卡行 - 发卡行表的主键, 商务卡 - true, false
     */
    public void setElementValue(String elementValue) {
        this.elementValue = elementValue == null ? null : elementValue.trim();
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
     *
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     *
     * @param remark
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