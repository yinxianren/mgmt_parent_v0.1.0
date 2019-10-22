package com.rxh.anew.table.merchant;

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

@TableName ( "5_merchant_rate_table" )
@Data
public class MerchantRateTable  implements Serializable {
   
	private Long id;//表主键
	private String merchantId;//商户号
	private String productId;//产品类型ID
	private BigDecimal singleFee;//单笔费用，元/笔
	private BigDecimal rateFee;//单笔费率
	private BigDecimal marginRatio;//保证金比例
	private String marginCycle;//保证金周期
	private String settleCycle;//结算周期
	private Integer status;//状态 0：启用,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
