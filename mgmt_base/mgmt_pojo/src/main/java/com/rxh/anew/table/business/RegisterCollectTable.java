package com.rxh.anew.table.business;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午10:31
 * Description:
 */

@TableName("6_register_collect_table")
@Data
public class RegisterCollectTable implements Serializable {
    private Long id ;//表主键',
    private String platformOrderId;//平台订单号',
    private Long  ritId;//主表id',
    private String  merchantId;//商户号',
    private String  terminalMerId;//终端商户号',
    private String  merOrderId;//商户订单号',
    private String  category;//经营类目编号',
    private byte[]  miMerCertPic1;//资质影印件',
    private byte[]  miMerCertPic2;//资质影印件',
    private String  bankCode;//银行简称	如：中国农业银行： ABC，中国工商银行： ICBC',
    private Integer bankCardType;//卡号类型	1借记卡  2信用卡',
    private String  cardHolderName;//持卡人姓名',
    private String  bankCardNum;//银行卡号',
    private String  bankCardPhone;//'银行卡手机号',
    private BigDecimal  payFee;//扣款手续费,用户扣款费率，单位： %，如2.8',
    private BigDecimal  backFee;//代付手续费	用户还款费率,单位：元/笔,保留两位小数',
    private String  channelRespResult;//通道响应结果',
    private String  crossRespResult;//'cross响应结果',
    private Integer status;//状态 0：success ,1:fail',
    private Date   createTime;//创建时间',
    private Date   updateTime;//更新时间',

}
