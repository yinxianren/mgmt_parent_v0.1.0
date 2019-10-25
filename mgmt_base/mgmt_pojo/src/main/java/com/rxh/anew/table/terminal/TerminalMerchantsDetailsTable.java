package com.rxh.anew.table.terminal;

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

@TableName ( "4_terminal_merchants_details_table" )
@Data
public class TerminalMerchantsDetailsTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String merchantId;//商户号
	private String terminalMerId;//终端商户号
	private String productId;//产品类型ID
	private String merOrderId;//商户订单号
	private String platformOrderId;//平台订单号
	private BigDecimal amount;//订单金额
	private BigDecimal inAmount;//入账金额
	private BigDecimal outAmount;//出账金额
	private BigDecimal rateFee;//单笔费率
	private BigDecimal fee;//手续费
	private BigDecimal totalBalance;//总金额
	private String timestamp;//数据插入的时间点，保证并发排序有序
	private Integer status;//状态 0：success,1:fail
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
