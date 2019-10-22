package com.rxh.anew.table.channel;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("5_product_type_setting_table")
@Data
public class ProductSettingTable implements Serializable {
     private Long id ;//主键',
     private String productId;//'产品类型ID',
     private String productName;//产品类型名称',
     private BigDecimal productFee;//产品费率',
     private String organizationId;//机构ID',
     private Integer status;// '状态 0：启用 ,1:禁用',
     private Date createTime;//'创建时间',
     private Date updateTime;//'更新时间',
}
