package com.rxh.anew.table.merchant;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("5_merchant_setting_table")
public class MerchantSettingTable implements Serializable {

  private Long id;
  private String merchantId;
  private String productId;
  private String organizationId;
  private String channelId;
  private Integer status;
  private Date createTime;
  private Date updateTime;

}
