package com.rxh.anew.table.channel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author  monkey
 * @Date 2019-10-29 
 */

@TableName ( "5_channel_details_table" )
@Getter
public class ChannelDetailsTable  implements Serializable {
	@TableId(type= IdType.INPUT)
	private Long id;//表主键
	private String channelId;//通道id
	private String organizationId;//机构ID
	private String productId;//产品类型ID
	private String merOrderId;//商户订单号
	private String platformOrderId;//平台订单号
	private BigDecimal amount;//订单金额
	private BigDecimal inAmount;//入账金额
	private BigDecimal outAmount;//出帐金额
	private BigDecimal rateFee;//单笔费率
	private BigDecimal fee;//手续费
	private BigDecimal feeProfit;//手续利润
	private BigDecimal totalBalance;//总余额
	private Long timestamp;//数据插入的时间点，保证并发排序有序
	private Integer status;//状态 0：success ,1:fail
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	public ChannelDetailsTable setId(Long id) {
		this.id = id;
		return this;
	}

	public ChannelDetailsTable setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public ChannelDetailsTable setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public ChannelDetailsTable setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public ChannelDetailsTable setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
		return this;
	}

	public ChannelDetailsTable setPlatformOrderId(String platformOrderId) {
		this.platformOrderId = platformOrderId;
		return this;
	}

	public ChannelDetailsTable setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public ChannelDetailsTable setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
		return this;
	}

	public ChannelDetailsTable setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
		return this;
	}

	public ChannelDetailsTable setRateFee(BigDecimal rateFee) {
		this.rateFee = rateFee;
		return this;
	}

	public ChannelDetailsTable setFee(BigDecimal fee) {
		this.fee = fee;
		return this;
	}

	public ChannelDetailsTable setFeeProfit(BigDecimal feeProfit) {
		this.feeProfit = feeProfit;
		return this;
	}

	public ChannelDetailsTable setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
		return this;
	}

	public ChannelDetailsTable setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public ChannelDetailsTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public ChannelDetailsTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public ChannelDetailsTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
