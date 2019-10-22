package com.rxh.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午3:26
 * Description:
 */
@Getter
public enum BusinessTypeEnum {

    PAY("pay","收单"),
    TRANS("trans","代付");

    private String busiType;
    private String remark;
    BusinessTypeEnum(String busiType,String remark){
        this.busiType = busiType;
        this.remark = remark;
    }
}
