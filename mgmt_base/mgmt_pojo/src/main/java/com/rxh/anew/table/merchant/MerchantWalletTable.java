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

@TableName ( "2_merchant_wallet_table" )
@Data
public class MerchantWalletTable  implements Serializable {
   
	private Long id;//表主键
	private String merchantId;//商户号
	private BigDecimal totalAmount;//订单总金额
	private BigDecimal incomeAmount;//入账总金额
	private BigDecimal outAmount;//出帐总金额
	private BigDecimal totalFee;//总手续
	private BigDecimal feeProfit;//总手续利润
	private BigDecimal totalMargin;//总保证金
	private BigDecimal totalBalance;//总金额
	private BigDecimal totalAvailableAmount;//总可用金额
	private BigDecimal totalUnavailableAmount;//总可不用金额
	private BigDecimal totalFreezeAmount;//总可冻结金额
	private Integer status;//状态 0：启用,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
