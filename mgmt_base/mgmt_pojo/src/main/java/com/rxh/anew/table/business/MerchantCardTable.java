package com.rxh.anew.table.business;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "7_merchant_card_table" )
@Data
public class MerchantCardTable  implements Serializable {
   
	private Long id;//表主键
	private String platformOrderId;//平台订单号
	private String merchantId;//商户号
	private String merOrderId;//商户订单号
	private String terminalMerId;//终端商户号
	private String cardHolderName;//身份证用户名
	private Integer identityType;//身份证类型，1身份证、2护照、3港澳回乡证、4台胞证、5军官证
	private String identityNum;//证件号
	private String bankCode;//银行简称	如：中国农业银行： ABC，中国工商银行： ICBC
	private Integer bankCardType;//卡号类型，1借记卡  2信用卡
	private String bankCardNum;//银行卡号
	private String bankCardPhone;//银行卡手机号
	private String validDate;//信用卡必填，格式：MMYY
	private String securityCode;//信用卡必填，格式：信用卡必填，信用卡背面三位安全码
	private String channelRespResult;//通道响应结果
	private String crossRespResult;//cross响应结果
	private Integer status;//状态 0：success ,1:fail
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
