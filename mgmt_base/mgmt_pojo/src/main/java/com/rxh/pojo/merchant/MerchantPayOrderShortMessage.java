package com.rxh.pojo.merchant;

import com.rxh.pojo.AbstractParamModel;

public class MerchantPayOrderShortMessage extends AbstractParamModel {

    //接口编号
    private String bizType;

    public String getBizType() {
        return bizType;
    }
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

}
