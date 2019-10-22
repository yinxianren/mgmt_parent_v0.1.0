package com.rxh.anew.table.agent;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("3_agent_merchant_setting_table")
public class AgentMerchantSettingTable implements Serializable {

  private Long id;
  private String agentMerchantId;
  private String productId;
  private BigDecimal singleFee;
  private BigDecimal rateFee;
  private BigDecimal marginRatio;
  private String marginCycle;
  private String settleCycle;
  private Integer status;

}
