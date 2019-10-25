package com.rxh.square.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rxh.anew.table.system.ProductSettingTable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
lin
机构信息表

organization_info
 */
public class OrganizationInfo implements Serializable {
	//机构ID
    private String organizationId;
  //机构名称
    private String organizationName;
  //创建时间
    private Date createTime;
  //创建人
    private String creator;
  //备注
    private String remark;
    //启用状态
    private Integer status;

    @TableField(exist = false)
    //启用产品
    private String productIds;
    @TableField(exist = false)
    //所有产品
    private List<ProductSettingTable> productTypes;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName == null ? null : organizationName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public List<ProductSettingTable> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductSettingTable> productTypes) {
        this.productTypes = productTypes;
    }
}