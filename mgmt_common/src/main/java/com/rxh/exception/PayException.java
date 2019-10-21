package com.rxh.exception;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/8/21
 * Time: 10:28
 * Project: Management
 * Package: com.rxh.exception
 */
@Data
public class PayException extends Exception {

    private Integer code;  // 异常代码，详见错误信息国际化配置文件
    private String payExceptionMsg;  // 异常附加信息，不需要国际化的信息，如字段名称
    private String resultCode;

    public PayException(String message){
        super(message);
    }

    public PayException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public PayException(String message, String resultCode) {
        super(message);
        this.resultCode = resultCode;
    }

    public PayException(String message, Integer code, String payExceptionMsg) {
        super(message);
        this.code = code;
        this.payExceptionMsg = payExceptionMsg;
    }

}
