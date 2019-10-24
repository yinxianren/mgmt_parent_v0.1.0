package com.rxh.anew.table.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("5_product_type_setting_table")
@Getter
public class ProductSettingTable implements Serializable {
     @TableId(type= IdType.AUTO)
     private Long id ;//主键
     private String productId;//'产品类型ID
     private String productName;//产品类型名称
     private BigDecimal productFee;//产品费率
     private String organizationId;//机构ID
     private Integer status;// '状态 0：启用 ,1:禁用
     private Date createTime;//'创建时间
     private Date updateTime;//'更新时间

     public ProductSettingTable setId(Long id) {
          this.id = id;
          return this;
     }

     public ProductSettingTable setProductId(String productId) {
          this.productId = productId;
          return this;
     }

     public ProductSettingTable setProductName(String productName) {
          this.productName = productName;
          return this;
     }

     public ProductSettingTable setProductFee(BigDecimal productFee) {
          this.productFee = productFee;
          return this;
     }

     public ProductSettingTable setOrganizationId(String organizationId) {
          this.organizationId = organizationId;
          return this;
     }

     public ProductSettingTable setStatus(Integer status) {
          this.status = status;
          return this;
     }

     public ProductSettingTable setCreateTime(Date createTime) {
          this.createTime = createTime;
          return this;
     }

     public ProductSettingTable setUpdateTime(Date updateTime) {
          this.updateTime = updateTime;
          return this;
     }
}
