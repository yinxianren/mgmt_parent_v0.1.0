package com.rxh.anew.table.channel;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("5_channel_info_table")
public class ChannelInfoTable implements Serializable {

    private Long id;//表主键,
    private String channelId;//通道id,
    private String channelName;//通道名称,
    private String channelTab;//通道标识,
    private String  busiType;//'业务类型，pay :收单，trans:代付'
    private String organizationId;//机构ID,
    private String productId;//产品类型ID,
    private Integer channelLevel;//通道等级，数字越大等级越高，不超过99,
    private String channelTransCode;//通道交易码,
    private String requestUrl;//请求cross的路径,
    private String channelParam;//通道配置参数,
    private String settleCycle;//结算周期,
    private BigDecimal productFee;//产品费率,
    private BigDecimal  channelSingleFee;// decimal(6,2)  0.00 ;//通道单笔费用，元/笔,
    private BigDecimal channelRateFee;//通道单笔费率,
    private BigDecimal singleMinAmount;//单笔最小金额,
    private BigDecimal singleMaxAmount;//单笔最大金额,
    private BigDecimal dayQuotaAmount;//单日最大金额,
    private BigDecimal monthQuotaAmount;//单月最大金额,
    private Integer status;//状态 0：启用 ,1:禁用,
    private Date createTime ;//创建时间,
    private Date updateTime ;//更新时间,
}
