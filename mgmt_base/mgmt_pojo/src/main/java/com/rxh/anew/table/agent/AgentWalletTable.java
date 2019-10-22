package com.rxh.anew.table.agent;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("3_agent_wallet_table")
public class AgentWalletTable implements Serializable {

  private Long id;
  private String agentMerchantId;
  private BigDecimal totalAmount;
  private BigDecimal incomeAmount;
  private BigDecimal outAmount;
  private BigDecimal totalBalance;
  private BigDecimal totalAvailableAmount;
  private BigDecimal totalUnavailableAmount;
  private BigDecimal totalFee;
  private BigDecimal totalFreezeAmount;
  private Integer status;
  private Date createTime;
  private Date updateTime;

}
