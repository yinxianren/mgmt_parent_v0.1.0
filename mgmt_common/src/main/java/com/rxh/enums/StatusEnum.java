package com.rxh.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 上午11:01
 * Description:
 */
@Getter
public enum  StatusEnum {
    _0(0,"成功"),
    _1(1,"失败"),
    _2(2,"未处理"),
    _3(3,",处理中")
    ;

    private int status;
    private String remark;

    StatusEnum(int status,String remark){
        this.status = status ;
        this.remark = remark;
    }
}
