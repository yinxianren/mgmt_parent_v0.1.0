package com.rxh.anew.table.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-10-31 
 */

@TableName ( "1_bank_rate_table" )
@Data
public class BankRateTable  implements Serializable {
   
	private Long id;//表主键
	private String bankCode;//银行编码
	private String productId;//产品id
	private String organizationId;//组织id
	private BigDecimal bankRate;//银行费率
	private Integer status;//0:可用，1：不可用
	private Date tradeTime;//交易时间
	private Date createTime;//创建时间

}
