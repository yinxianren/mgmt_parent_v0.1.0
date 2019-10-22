package com.rxh.anew.table.system;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("5_organization_info_table")
public class OrganizationInfoTable implements Serializable {

  private Long id;
  private String organizationId;
  private String organizationName;
  private Integer status;
  private Date createTime;
  private Date updateTime;

}
