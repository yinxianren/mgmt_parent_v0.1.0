package com.rxh.pojo.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SysRole implements Serializable {
    // ID
    private Long id;

    // 角色名称（系统）
    // ROLE_INTERNAL_ADMIN（内部管理员）ROLE_INTERNAL_USER（内部用户）
    // ROLE_MERCHANT_ADMIN（商户管理员）ROLE_MERCHANT_USER（商户用户）
    private String role;

    // 角色名称（用户，可自定义）
    private String roleName;

    // 属于，内部（0）/商户号
    private String belongTo;

    // 拥有权限ID，以数组形式保存。
    private String privilegesId;

    // 是否可用
    private Boolean available;

    // 备注
    private String remark;

    // 创建人
    private String creator;

    // 创建时间
    private Date createTime;

    // 修改人
    private String modifier;

    // 更新时间
    private Date modifyTime;

    // 角色拥有权限
    private List<SysPrivileges> privileges;

    public SysRole() {
    }

    public SysRole(String role) {
        this.role = role;
    }

    /**
     * ID
     *
     * @return id ID
     */
    public Long getId() {
        return id;
    }

    /**
     * ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 角色名称（系统）
     * ROLE_INTERNAL_ADMIN（内部管理员）ROLE_INTERNAL_USER（内部用户）
     * ROLE_MERCHANT_ADMIN（商户管理员）ROLE_MERCHANT_USER（商户用户）
     *
     * @return role 角色名称（系统）
     * ROLE_INTERNAL_ADMIN（内部管理员）ROLE_INTERNAL_USER（内部用户）
     * ROLE_MERCHANT_ADMIN（商户管理员）ROLE_MERCHANT_USER（商户用户）
     */
    public String getRole() {
        return role;
    }

    /**
     * 角色名称（系统）
     * ROLE_INTERNAL_ADMIN（内部管理员）ROLE_INTERNAL_USER（内部用户）
     * ROLE_MERCHANT_ADMIN（商户管理员）ROLE_MERCHANT_USER（商户用户）
     *
     * @param role 角色名称（系统）
     *             ROLE_INTERNAL_ADMIN（内部管理员）ROLE_INTERNAL_USER（内部用户）
     *             ROLE_MERCHANT_ADMIN（商户管理员）ROLE_MERCHANT_USER（商户用户）
     */
    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    /**
     * 角色名称（用户，可自定义）
     *
     * @return role_name 角色名称（用户，可自定义）
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 角色名称（用户，可自定义）
     *
     * @param roleName 角色名称（用户，可自定义）
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 属于，内部（0）/商户号
     *
     * @return belongto 属于，内部（0）/商户号
     */
    public String getBelongto() {
        return belongTo;
    }

    /**
     * 属于，内部（0）/商户号
     *
     * @param belongTo 属于，内部（0）/商户号
     */
    public void setBelongto(String belongTo) {
        this.belongTo = belongTo;
    }

    /**
     * 拥有权限ID，以数组形式保存。
     *
     * @return privileges_id 拥有权限ID，以数组形式保存。
     */
    public String getPrivilegesId() {
        return privilegesId;
    }

    /**
     * 拥有权限ID，以数组形式保存。
     *
     * @param privilegesId 拥有权限ID，以数组形式保存。
     */
    public void setPrivilegesId(String privilegesId) {
        this.privilegesId = privilegesId == null ? null : privilegesId.trim();
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
     * 修改人
     *
     * @return modifier 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 修改人
     *
     * @param modifier 修改人
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

    public List<SysPrivileges> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<SysPrivileges> privileges) {
        this.privileges = privileges;
    }
}