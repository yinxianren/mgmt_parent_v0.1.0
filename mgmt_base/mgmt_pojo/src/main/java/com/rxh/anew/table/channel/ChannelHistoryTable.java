package com.rxh.anew.table.channel;

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

@TableName ( "8_channel_history_table" )
@Data
public class ChannelHistoryTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String channelId;//通道id
	private String merchantId;//商户号
	private String productId;//产品类型ID
	private BigDecimal totalAmount;//累计订单金额
	private Long totalCount;//累计使用次数
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
