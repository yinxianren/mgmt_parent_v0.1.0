package com.rxh.anew.table.merchant;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("5_merchant_rate_table")
public class MerchantRateTable implements Serializable {

  private Long id;
  private String merchantId;
  private String productId;
  private BigDecimal singleFee;
  private BigDecimal rateFee;
  private BigDecimal marginRatio;
  private String marginCycle;
  private String settleCycle;
  private Integer status;
  private Date createTime;
  private Date updateTime;

}
