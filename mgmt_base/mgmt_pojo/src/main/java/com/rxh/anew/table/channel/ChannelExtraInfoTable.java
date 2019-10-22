package com.rxh.anew.table.channel;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "5_channel_extra_info_table" )
@Data
public class ChannelExtraInfoTable  implements Serializable {
   
	private Long id;//表主键
	private String extraChannelId;//附属通道ID
	private String extraChannelName;//附属通道名称
	private String organizationId;//机构ID
	private String bussType;// ADDCUS：进件 ,BONDCARD:绑卡
	private String requestUrl;//请求cross的路径
	private String channelParam;//通道配置参数
	private Integer status;//状态 0：启用 ,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

}
