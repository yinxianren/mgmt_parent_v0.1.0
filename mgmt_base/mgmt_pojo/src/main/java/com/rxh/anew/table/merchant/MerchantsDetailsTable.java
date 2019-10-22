package com.rxh.anew.table.merchant;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@TableName("2_merchants_details_table")
public class MerchantsDetailsTable implements Serializable {

  private Long id;
  private String merchantId;
  private String productId;
  private String merOrderId;
  private String platformOrderId;
  private BigDecimal amount;
  private BigDecimal inAmount;
  private BigDecimal outAmount;
  private BigDecimal rateFee;
  private BigDecimal fee;
  private BigDecimal feeProfit;
  private BigDecimal totalBalance;
  private String timestamp;
  private Integer status;
  private Date createTime;
  private Date updateTime;

}