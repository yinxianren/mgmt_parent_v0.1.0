package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.util.Date;

public class MerchantWebsiteProType implements Serializable {
    // 主键ID
    private Long id;

    // 产品类型编码
    private Long productTypeId;

    // 产品类型名称
    private String productTypeName;

    // 备注
    private String remark;

    // 
    private Date createTime;

    // 
    private String createUser;

    // 
    private Date modifyTime;

    // 
    private String modifyUser;

    /**
     * 主键ID
     *
     * @return id 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 产品类型编码
     *
     * @return product_type_id 产品类型编码
     */
    public Long getProductTypeId() {
        return productTypeId;
    }

    /**
     * 产品类型编码
     *
     * @param productTypeId 产品类型编码
     */
    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    /**
     * 产品类型名称
     *
     * @return product_type_name 产品类型名称
     */
    public String getProductTypeName() {
        return productTypeName;
    }

    /**
     * 产品类型名称
     *
     * @param productTypeName 产品类型名称
     */
    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName == null ? null : productTypeName.trim();
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
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return create_user
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * @return modify_time
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return modify_user
     */
    public String getModifyUser() {
        return modifyUser;
    }

    /**
     * @param modifyUser
     */
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
    }
}