package com.rxh.anew.table.agent;

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

@TableName ( "3_agent_wallet_table" )
@Data
public class AgentWalletTable  implements Serializable {
   
	private Long id;//表主键
	private String agentMerchantId;//代理商ID
	private BigDecimal totalAmount;//订单总金额
	private BigDecimal incomeAmount;//入账总金额
	private BigDecimal outAmount;//出账总金额
	private BigDecimal totalBalance;//总金额
	private BigDecimal totalAvailableAmount;//总可用金额
	private BigDecimal totalUnavailableAmount;//总不可用金额
	private BigDecimal totalFee;//总手续费
	private BigDecimal totalFreezeAmount;//总冻结金额
	private Integer status;//状态 0：启用 ,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
