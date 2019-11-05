package com.rxh.anew.table.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "2_merchants_details_table" )
@Getter
public class MerchantsDetailsTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String merchantId;//商户号
	private String productId;//产品类型ID
	private String merOrderId;//商户订单号
	private String platformOrderId;//平台订单号
	private BigDecimal amount;//订单金额
	private BigDecimal inAmount;//入账金额
	private BigDecimal outAmount;//出账金额
	private BigDecimal rateFee;//单笔费率
	private BigDecimal fee;//手续费
	private BigDecimal feeProfit;//手续费利润
	private BigDecimal totalBalance;//总金额
	private Long timestamp;//数据插入的时间点，保证并发排序有序
	private Integer status;//状态 0：success,1:fail
	private Date createTime;//创建时间
	private Date updateTime;//更新时间


	public MerchantsDetailsTable setId(Long id) {
		this.id = id;
		return this;
	}

	public MerchantsDetailsTable setMerchantId(String merchantId) {
		this.merchantId = merchantId;
		return this;
	}

	public MerchantsDetailsTable setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public MerchantsDetailsTable setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
		return this;
	}

	public MerchantsDetailsTable setPlatformOrderId(String platformOrderId) {
		this.platformOrderId = platformOrderId;
		return this;
	}

	public MerchantsDetailsTable setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public MerchantsDetailsTable setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
		return this;
	}

	public MerchantsDetailsTable setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
		return this;
	}

	public MerchantsDetailsTable setRateFee(BigDecimal rateFee) {
		this.rateFee = rateFee;
		return this;
	}

	public MerchantsDetailsTable setFee(BigDecimal fee) {
		this.fee = fee;
		return this;
	}

	public MerchantsDetailsTable setFeeProfit(BigDecimal feeProfit) {
		this.feeProfit = feeProfit;
		return this;
	}

	public MerchantsDetailsTable setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
		return this;
	}

	public MerchantsDetailsTable setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public MerchantsDetailsTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public MerchantsDetailsTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public MerchantsDetailsTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
