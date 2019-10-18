package com.rxh.exception;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/8/21
 * Time: 10:28
 * Project: Management
 * Package: com.rxh.exception
 */
public class PayException extends Exception {
    // 异常代码，详见错误信息国际化配置文件
    private Integer code;
    // 异常附加信息，不需要国际化的信息，如字段名称
    private String payExceptionMsg;

    public PayException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public PayException(String message, Integer code, String payExceptionMsg) {
        super(message);
        this.code = code;
        this.payExceptionMsg = payExceptionMsg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getPayExceptionMsg() {
        return payExceptionMsg;
    }

    public void setPayExceptionMsg(String payExceptionMsg) {
        this.payExceptionMsg = payExceptionMsg;
    }
}
