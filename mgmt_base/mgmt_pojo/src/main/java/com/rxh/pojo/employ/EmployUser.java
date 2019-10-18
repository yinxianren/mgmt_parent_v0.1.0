package com.rxh.pojo.employ;

import java.io.Serializable;
import java.util.Date;

public class EmployUser implements Serializable {
     // 用户id
    private String id;

     // 用户角色id
    private String roleId;

     // 代理商表主键/商户表主键
    private String refId;

     // 所有者 1 商户 2 代理商 3代理子商户
    private Short refType;

     // 登录id
    private String access;

     // 用户密码 采用MD5加密
    private String password;

     // 用户姓名
    private String name;

     // 0 女 ；1男
    private Short sex;

     // 邮箱
    private String email;

     // 手机号码
    private String phone;

     // 详细地址
    private String address;

     // 常量组Status提供
    private Short status;

     // 
    private Date lastLoginTime;

     // 是否锁定
    private Boolean locked;

     // 超过6次自动锁定,不允许登录
    private Short allowCount;

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
     * 用户id
     * @return id 用户id
     */
    public String getId() {
        return id;
    }

    /**
     * 用户id
     * @param id 用户id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 用户角色id
     * @return role_id 用户角色id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 用户角色id
     * @param roleId 用户角色id
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
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
     * 所有者 1 商户 2 代理商 3代理子商户
     * @return ref_type 所有者 1 商户 2 代理商 3代理子商户
     */
    public Short getRefType() {
        return refType;
    }

    /**
     * 所有者 1 商户 2 代理商 3代理子商户
     * @param refType 所有者 1 商户 2 代理商 3代理子商户
     */
    public void setRefType(Short refType) {
        this.refType = refType;
    }

    /**
     * 登录id
     * @return access 登录id
     */
    public String getAccess() {
        return access;
    }

    /**
     * 登录id
     * @param access 登录id
     */
    public void setAccess(String access) {
        this.access = access == null ? null : access.trim();
    }

    /**
     * 用户密码 采用MD5加密
     * @return password 用户密码 采用MD5加密
     */
    public String getPassword() {
        return password;
    }

    /**
     * 用户密码 采用MD5加密
     * @param password 用户密码 采用MD5加密
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 用户姓名
     * @return name 用户姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 用户姓名
     * @param name 用户姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 0 女 ；1男
     * @return sex 0 女 ；1男
     */
    public Short getSex() {
        return sex;
    }

    /**
     * 0 女 ；1男
     * @param sex 0 女 ；1男
     */
    public void setSex(Short sex) {
        this.sex = sex;
    }

    /**
     * 邮箱
     * @return email 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 邮箱
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 手机号码
     * @return phone 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 手机号码
     * @param phone 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 详细地址
     * @return address 详细地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 详细地址
     * @param address 详细地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
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
     * @return last_login_time 
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 
     * @param lastLoginTime 
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 是否锁定
     * @return locked 是否锁定
     */
    public Boolean getLocked() {
        return locked;
    }

    /**
     * 是否锁定
     * @param locked 是否锁定
     */
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    /**
     * 超过6次自动锁定,不允许登录
     * @return allow_count 超过6次自动锁定,不允许登录
     */
    public Short getAllowCount() {
        return allowCount;
    }

    /**
     * 超过6次自动锁定,不允许登录
     * @param allowCount 超过6次自动锁定,不允许登录
     */
    public void setAllowCount(Short allowCount) {
        this.allowCount = allowCount;
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