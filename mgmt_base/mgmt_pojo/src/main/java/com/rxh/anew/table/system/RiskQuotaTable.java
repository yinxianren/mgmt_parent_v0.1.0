package com.rxh.anew.table.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("8_risk_quota_table")
public class RiskQuotaTable implements Serializable {

  private Long id;
  private String merIdOrChannelId;
  private String bussType;
  private String timeType;
  private BigDecimal amount;
  private Date createTime;
  private Date updateTime;

}
