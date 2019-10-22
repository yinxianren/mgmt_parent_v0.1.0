package com.rxh.anew.table.channel;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("5_channel_extra_info_table")
@Data
public class ChannelExtraInfoTable implements Serializable {

  private Long id;
  private String extraChannelId;
  private String extraChannelName;
  private String organizationId;
  private Long bussType;
  private String requestUrl;
  private String channelParam;
  private Long status;
  private Date createTime;
  private Date updateTime;

}
