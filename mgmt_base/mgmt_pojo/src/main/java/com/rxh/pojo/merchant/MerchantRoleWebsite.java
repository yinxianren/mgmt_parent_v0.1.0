package com.rxh.pojo.merchant;

import java.io.Serializable;

public class MerchantRoleWebsite implements Serializable {
     // ID
    private Long id;

     // 角色ID
    private String roleId;

     // 菜单ID
    private Integer siteId;

     // 备注
    private String remark;

     // 
    private Integer merId;

    /**
     * ID
     * @return id ID
     */
    public Long getId() {
        return id;
    }

    /**
     * ID
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 角色ID
     * @return role_id 角色ID
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 角色ID
     * @param roleId 角色ID
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * 菜单ID
     * @return site_id 菜单ID
     */
    public Integer getSiteId() {
        return siteId;
    }

    /**
     * 菜单ID
     * @param siteId 菜单ID
     */
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 
     * @return mer_id 
     */
    public Integer getMerId() {
        return merId;
    }

    /**
     * 
     * @param merId 
     */
    public void setMerId(Integer merId) {
        this.merId = merId;
    }
}