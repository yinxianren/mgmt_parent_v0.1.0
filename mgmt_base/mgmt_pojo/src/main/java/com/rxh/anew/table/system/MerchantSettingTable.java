package com.rxh.anew.table.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午1:47
 * Description:
 */

@TableName("5_merchant_setting_table")
@Data
public class MerchantSettingTable {
    private Long id  ;//表主键
    private String  merchantId ;//商户号
    private String productId ;//产品类型ID
    private String organizationId ;//机构ID
    private String channelId ;//通道id
    private Integer status;//状态 0：启用1:禁用
    private Date createTime ;//创建时间
    private Date updateTime ;//更新时间
}
