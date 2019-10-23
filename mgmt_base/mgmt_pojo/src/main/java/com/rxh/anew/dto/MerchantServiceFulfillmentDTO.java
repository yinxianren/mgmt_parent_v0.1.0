package com.rxh.anew.dto;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午8:51
 * Description:
 */
@Data
public class MerchantServiceFulfillmentDTO {
    private String charset;//	字符集编码
    private String signType;//	签名类型
    private String merId;//	商户号
    private String merOrderId;//	商户订单号
    private String platformOrderId;//	平台流水号
    private String terminalMerId;//	子商户id
    private String signMsg;//	签名字符串
}
