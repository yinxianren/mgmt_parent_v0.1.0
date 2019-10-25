package com.rxh.anew.table.system;

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

@TableName ( "8_risk_quota_table" )
@Data
public class RiskQuotaTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String merIdOrChannelId;//商户号/通道ID
	private String bussType;// C:通道,M:商户 
	private String timeType;//D:日 M:月 Y:年 B:单笔
	private BigDecimal amount;//将对应时间内商户的统计数据转换成CNY后累加
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
