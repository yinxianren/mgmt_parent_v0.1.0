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
    TRANS("trans","代付"),
    b1("b1","业务登记"),
    b2("b2","绑定银行卡"),
    b3("b3","业务开通")
    ;

    private String busiType;
    private String remark;
    BusinessTypeEnum(String busiType,String remark){
        this.busiType = busiType;
        this.remark = remark;
    }
}
