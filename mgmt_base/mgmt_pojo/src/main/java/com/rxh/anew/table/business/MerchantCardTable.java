package com.rxh.anew.table.business;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("7_merchant_card_table")
public class MerchantCardTable implements Serializable {

  private Long id;
  private String platformOrderId;
  private String merchantId;
  private String merOrderId;
  private String terminalMerId;
  private String userName;
  private Long identityType;
  private String identityNum;
  private String bankCode;
  private Long bankCardType;
  private String bankCardNum;
  private String bankCardPhone;
  private String validDate;
  private String securityCode;
  private String channelRespResult;
  private String crossRespResult;
  private Long status;
  private Date createTime;
  private Date updateTime;



}
