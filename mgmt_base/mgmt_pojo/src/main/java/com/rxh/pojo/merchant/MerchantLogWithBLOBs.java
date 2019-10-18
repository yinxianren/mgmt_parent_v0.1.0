package com.rxh.pojo.merchant;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/12/13
 * Time: 16:05
 * Project: Management
 * Package: com.rxh.pojo.merchant
 */
public class MerchantLogWithBLOBs extends MerchantLog implements Serializable {
    /**
     * 信息/异常栈信息（10条栈信息）
     */
    private String message;

    /**
     * 信息/异常栈信息（10条栈信息）
     *
     * @return message 信息/异常栈信息（10条栈信息）
     */
    public String getMessage() {
        return message;
    }

    /**
     * 信息/异常栈信息（10条栈信息）
     *
     * @param message 信息/异常栈信息（10条栈信息）
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }
}
