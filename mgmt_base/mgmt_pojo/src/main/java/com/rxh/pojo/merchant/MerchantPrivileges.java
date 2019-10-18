package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.List;

public class MerchantPrivileges implements Serializable {
    // 权限主键
    private Long id;

    // 权限名称
    private String name;

    // 权限描述
    private String description;

    // 父级名称
    private Long parentId;

    // 页面菜单：声明/描述
    private String stateName;

    // 页面菜单：图标class
    private String iconFont;

    // 是否可用
    private Boolean available;

    // 子菜单
    private List<MerchantPrivileges> submenu;

    /**
     * 权限主键
     *
     * @return id 权限主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 权限主键
     *
     * @param id 权限主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 权限名称
     *
     * @return name 权限名称
     */
    public String getName() {
        return name;
    }

    /**
     * 权限名称
     *
     * @param name 权限名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 权限描述
     *
     * @return description 权限描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 权限描述
     *
     * @param description 权限描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 父级名称
     *
     * @return parent_id 父级名称
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 父级名称
     *
     * @param parentId 父级名称
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 页面菜单：声明/描述
     *
     * @return state_name 页面菜单：声明/描述
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * 页面菜单：声明/描述
     *
     * @param stateName 页面菜单：声明/描述
     */
    public void setStateName(String stateName) {
        this.stateName = stateName == null ? null : stateName.trim();
    }

    /**
     * 页面菜单：图标class
     *
     * @return icon_font 页面菜单：图标class
     */
    public String getIconFont() {
        return iconFont;
    }

    /**
     * 页面菜单：图标class
     *
     * @param iconFont 页面菜单：图标class
     */
    public void setIconFont(String iconFont) {
        this.iconFont = iconFont == null ? null : iconFont.trim();
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

    public List<MerchantPrivileges> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(List<MerchantPrivileges> submenu) {
        this.submenu = submenu;
    }
}