package com.rxh.anew.table.terminal;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("4_terminal_merchants_wallet_table")
public class TerminalMerchantsWalletTable implements Serializable {

  private Long id;
  private String merchantId;
  private String terminalMerId;
  private BigDecimal totalAmount;
  private BigDecimal incomeAmount;
  private BigDecimal outAmount;
  private BigDecimal totalBalance;
  private BigDecimal totalAvailableAmount;
  private BigDecimal totalUnavailableAmount;
  private BigDecimal totalFee;
  private BigDecimal totalMargin;
  private BigDecimal totalFreezeAmount;
  private Integer status;
  private Date createTime;
  private Date updateTime;

}
