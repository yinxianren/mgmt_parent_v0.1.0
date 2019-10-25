package com.rxh.anew.table.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "5_organization_info_table" )
@Data
public class OrganizationInfoTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String organizationId;//机构ID
	private String organizationName;//机构名称
	private Integer status;//状态 0：启用 ,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间
	private String creator;//创建者
	@TableField(exist = false)
	private String productIds;
	@TableField(exist = false)
	private List<ProductSettingTable> productTypes;

}
