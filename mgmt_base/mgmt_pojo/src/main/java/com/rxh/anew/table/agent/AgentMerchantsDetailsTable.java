package com.rxh.anew.table.agent;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("3_agent_merchants_details_table")
public class AgentMerchantsDetailsTable implements Serializable {

  private Long id;
  private String agentMerchantId;
  private String merOrderId;
  private String platformOrderId;
  private String productId;
  private BigDecimal amount;
  private BigDecimal inAmount;
  private BigDecimal outAmount;
  private BigDecimal rateFee;
  private BigDecimal fee;
  private BigDecimal totalBalance;
  private String timestamp;
  private Integer status;
  private Date createTime;
  private Date updateTime;

}
