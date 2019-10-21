package com.rxh.enums;


import lombok.Getter;

@Getter
public enum ResponseCodeEnum {
     RXH00000("RXH00000","交易成功")
    ,RXH00001("RXH00001","卡号错误")
    ,RXH00002("RXH00002","卡信息或手机号码错误")
    ,RXH00003("RXH00003","请重新获取验证码")
    ,RXH00004("RXH00004","短信验证码错误")
    ,RXH00005("RXH00005","短信验证码发送失败")
    ,RXH00006("RXH00006","协议不存在")
    ,RXH00007("RXH00007","交易异常,请查询交易")
    ,RXH00008("RXH00008","原交易不存在")
    ,RXH00009("RXH00009","订单号已经存在")
    ,RXH00010("RXH00010","余额不足")
    ,RXH00011("RXH00011","代付金额大于支付金额")
    ,RXH00012("RXH00012","单笔金额超过单笔最大额度")
    ,RXH00013("RXH00013","请求参数为空")
    ,RXH00014("RXH00014","缺少必要参数")
    ,RXH00015("RXH00015","字段值长度异常")
    ,RXH00016("RXH00016","字段值格式错误")
    ,RXH00017("RXH00017","该商户号不存在")

    ,RXH99999("RXH99999","其他错误")
    ;
    private String code;
    private String msg;

    ResponseCodeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
