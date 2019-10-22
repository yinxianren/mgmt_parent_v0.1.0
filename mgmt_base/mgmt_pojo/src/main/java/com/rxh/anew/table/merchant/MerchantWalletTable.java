package com.rxh.anew.table.merchant;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("2_merchant_wallet_table")
public class MerchantWalletTable implements Serializable {

  private Long id;
  private String merchantId;
  private BigDecimal totalAmount;
  private BigDecimal incomeAmount;
  private BigDecimal outAmount;
  private BigDecimal totalFee;
  private BigDecimal feeProfit;
  private BigDecimal totalMargin;
  private BigDecimal totalBalance;
  private BigDecimal totalAvailableAmount;
  private BigDecimal totalUnavailableAmount;
  private BigDecimal totalFreezeAmount;
  private Integer status;
  private Date createTime;
  private Date updateTime;


}
