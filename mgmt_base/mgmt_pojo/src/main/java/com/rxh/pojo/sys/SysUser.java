package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.Date;

public class SysUser implements Serializable {
    //
    private Long id;

    // 用户名称
    private String userName;

    // 用户密码
    private String password;

    // 真实姓名
    private String realName;

    // 排序号
    private Integer orderNo;

    // 部门
    private String deptId;

    // 邮箱
    private String email;

    // 电话
    private String telphone;

    // 手机
    private String mobile;

    // 管理员
    private Short adminState;

    // 登陆IP
    private String lastLogonIp;

    // 角色ID
    private Long roleId;

    // 所有者： 0-总部，其他-商户id
    private String belongTo;

    // 会话ID
    private String sessionId;

    // 是否可用
    private Boolean available;

    // 备注
    private String remark;

    // 操作人
    private String creator;

    // 创建时间
    private Date createTime;

    //
    private String modifier;

    // 更新时间
    private Date modifyTime;

    // 用户拥有角色
    private SysRole role;

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
     * 用户名称
     *
     * @return user_name 用户名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 用户名称
     *
     * @param userName 用户名称
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 用户密码
     *
     * @return password 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 用户密码
     *
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 真实姓名
     *
     * @return real_name 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 排序号
     *
     * @return order_no 排序号
     */
    public Integer getOrderNo() {
        return orderNo;
    }

    /**
     * 排序号
     *
     * @param orderNo 排序号
     */
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 部门
     *
     * @return dept_id 部门
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * 部门
     *
     * @param deptId 部门
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }

    /**
     * 邮箱
     *
     * @return email 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 电话
     *
     * @return telphone 电话
     */
    public String getTelphone() {
        return telphone;
    }

    /**
     * 电话
     *
     * @param telphone 电话
     */
    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    /**
     * 手机
     *
     * @return mobile 手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 手机
     *
     * @param mobile 手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 管理员
     *
     * @return admin_state 管理员
     */
    public Short getAdminState() {
        return adminState;
    }

    /**
     * 管理员
     *
     * @param adminState 管理员
     */
    public void setAdminState(Short adminState) {
        this.adminState = adminState;
    }

    /**
     * 登陆IP
     *
     * @return last_logon_ip 登陆IP
     */
    public String getLastLogonIp() {
        return lastLogonIp;
    }

    /**
     * 登陆IP
     *
     * @param lastLogonIp 登陆IP
     */
    public void setLastLogonIp(String lastLogonIp) {
        this.lastLogonIp = lastLogonIp == null ? null : lastLogonIp.trim();
    }

    /**
     * 角色ID
     *
     * @return role_id 角色ID
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 角色ID
     *
     * @param roleId 角色ID
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 所有者： 0-总部，其他-商户id
     *
     * @return belongto 所有者： 0-总部，其他-商户id
     */
    public String getBelongto() {
        return belongTo;
    }

    /**
     * 所有者： 0-总部，其他-商户id
     *
     * @param belongTo 所有者： 0-总部，其他-商户id
     */
    public void setBelongto(String belongTo) {
        this.belongTo = belongTo;
    }

    /**
     * 会话ID
     *
     * @return session_id 会话ID
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 会话ID
     *
     * @param sessionId 会话ID
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }

    /**
     * 是否可用
     *
     * @return available 是否可用
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * 是否可用
     *
     * @param available 是否可用
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * 备注
     *
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 操作人
     *
     * @return creator 操作人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 操作人
     *
     * @param creator 操作人
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
     * @return modifier
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * @param modifier
     */
    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    /**
     * 更新时间
     *
     * @return modify_time 更新时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 更新时间
     *
     * @param modifyTime 更新时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }
}