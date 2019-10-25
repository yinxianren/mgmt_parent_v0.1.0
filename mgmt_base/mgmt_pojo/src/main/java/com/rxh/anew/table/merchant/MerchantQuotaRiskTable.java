package com.rxh.anew.table.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "2_merchant_quota_risk_table" )
@Data
public class MerchantQuotaRiskTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String merchantId;//商户号
	private BigDecimal singleQuotaAmount;//单笔风控额度
	private BigDecimal dayQuotaAmount;//单日笔风控额度
	private BigDecimal monthQuotaAmount;//单月笔风控额度
	private Integer status;//状态 0：启用,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
