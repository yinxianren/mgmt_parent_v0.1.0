package com.rxh.anew.table.merchant;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("5_merchant_quota_risk_table")
public class MerchantQuotaRiskTable implements Serializable {

  private Long id;
  private String merchantId;
  private BigDecimal singleQuotaAmount;
  private BigDecimal dayQuotaAmount;
  private BigDecimal monthQuotaAmount;
  private Integer status;
  private Date createTime;
  private Date updateTime;

}
