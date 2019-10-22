package com.rxh.anew.table.business;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午10:44
 * Description:
 */
@TableName("6_register_info_table")
@Data
public class RegisterInfoTable implements Serializable {
    private Long id ;//表主键,
    private String merchantId ;// 商户号,
    private String terminalMerId;// 终端商户号,
    private String terminalMerName ;// 终端商户名,
    private String userName ;// 身份证用户名,
    private String userShortName ;// 用户名简称,
    private Integer identityType ;//身份证类型，1身份证、2护照、3港澳回乡证、4台胞证、5军官证,
    private String  identityNum ;// 证件号,
    private String  phone ;// 手机号,
    private String  merchantType ;// 用户类型：00公司商户，01个体商户,
    private String  province ;// 省份编号,
    private String  city ;// 城市编号,
    private String  address ;// 详细地址,
    private Integer status ;// 0：启用 ,1:禁用，2：未审核,
    private Date createTime ;// 创建时间,
    private Date  updateTime ;// 更新时间,
}
