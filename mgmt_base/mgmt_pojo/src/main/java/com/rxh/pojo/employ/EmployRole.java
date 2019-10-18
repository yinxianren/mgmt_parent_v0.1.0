package com.rxh.pojo.employ;

import java.io.Serializable;
import java.util.Date;

public class EmployRole implements Serializable {
     // 角色id
    private Long id;

     // 角色名
    private String name;

     // 常量组Status提供
    private Short status;

     // 类型 0 管理员 1 普通用户
    private Short type;

     // 代理商表主键/商户表主键
    private String refId;

     // 所有者 1 商户 2 代理商
    private Short refType;

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
     * 角色id
     * @return id 角色id
     */
    public Long getId() {
        return id;
    }

    /**
     * 角色id
     * @param id 角色id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 角色名
     * @return name 角色名
     */
    public String getName() {
        return name;
    }

    /**
     * 角色名
     * @param name 角色名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
     * 类型 0 管理员 1 普通用户
     * @return type 类型 0 管理员 1 普通用户
     */
    public Short getType() {
        return type;
    }

    /**
     * 类型 0 管理员 1 普通用户
     * @param type 类型 0 管理员 1 普通用户
     */
    public void setType(Short type) {
        this.type = type;
    }

    /**
     * 代理商表主键/商户表主键
     * @return ref_id 代理商表主键/商户表主键
     */
    public String getRefId() {
        return refId;
    }

    /**
     * 代理商表主键/商户表主键
     * @param refId 代理商表主键/商户表主键
     */
    public void setRefId(String refId) {
        this.refId = refId == null ? null : refId.trim();
    }

    /**
     * 所有者 1 商户 2 代理商
     * @return ref_type 所有者 1 商户 2 代理商
     */
    public Short getRefType() {
        return refType;
    }

    /**
     * 所有者 1 商户 2 代理商
     * @param refType 所有者 1 商户 2 代理商
     */
    public void setRefType(Short refType) {
        this.refType = refType;
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