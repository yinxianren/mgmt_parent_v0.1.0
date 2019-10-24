package com.rxh.anew.table.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "2_merchant_info_table" )
@Data
public class MerchantInfoTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String merchantId;//商户号
	private String merchantName;//商户名称
	private String merchantShortName;//商户简称
	private String type;//商户类型：00公司商户，01个体商户
	private String agentMerchantId;//代理商ID
	private String secretKey;//签名key值
	private String privateKey;//私钥
	private String publicKey;//公钥
	private Integer identityType;//证件类型： 1身份证、2护照、3港澳回乡证、4台胞证、5军官证
	private String identityNum;//证件号
	private String identityPath;//证件图片存放路径
	private String phone;//手机
	private Integer phoneStatus;//0：验证，1：未验证
	private String email;//邮箱
	private Integer emailStatus;//0：验证，1：未验证
	private String qq;//qq
	private Integer status;//0：启用 ,1:禁用，2：未审核
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
