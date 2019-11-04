package com.rxh.anew.table.system;

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
 * @Date 2019-10-31 
 */

@TableName ( "1_bank_rate_table" )
@Getter
public class BankRateTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String bankCode;//银行编码
	private String productId;//产品id
	private String organizationId;//组织id
	private BigDecimal bankRate;//银行费率
	private Integer status;//0:可用，1：不可用
	private Date tradeTime;//交易时间
	private Date createTime;//创建时间

	public BankRateTable setId(Long id) {
		this.id = id;
		return this;
	}

	public BankRateTable setBankCode(String bankCode) {
		this.bankCode = bankCode;
		return this;
	}

	public BankRateTable setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public BankRateTable setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public BankRateTable setBankRate(BigDecimal bankRate) {
		this.bankRate = bankRate;
		return this;
	}

	public BankRateTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public BankRateTable setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
		return this;
	}

	public BankRateTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}
}
