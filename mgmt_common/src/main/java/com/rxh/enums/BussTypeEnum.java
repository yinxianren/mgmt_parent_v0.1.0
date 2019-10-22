package com.rxh.enums;

public enum BussTypeEnum {

    ADDCUS("ADDCUS","进件"),
    BONDCARD("BONDCARD","绑卡");

    private String bussType;
    private String remark;

    BussTypeEnum(String bussType,String remark){
        this.bussType = bussType ;
        this.remark = remark;
    }
}
