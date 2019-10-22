package com.rxh.anew.table.agent;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "3_agent_merchant_setting_table" )
@Data
public class AgentMerchantSettingTable  implements Serializable {
   
	private Long id;//表主键
	private String agentMerchantId;//代理商ID
	private String productId;//产品类型ID
	private BigDecimal singleFee;//单笔费用，元/笔
	private BigDecimal rateFee;//单笔费率
	private BigDecimal marginRatio;//保证金比例
	private String marginCycle;//保证金周期
	private String settleCycle;//结算周期
	private Long status;

}
