package com.rxh.anew.table.channel;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("8_channel_history_table")
public class ChannelHistoryTable implements Serializable {

  private Long id;
  private String channelId;
  private String merchantId;
  private String productId;
  private BigDecimal totalAmount;
  private Long totalCount;
  private Date createTime;
  private Date updateTime;

}
