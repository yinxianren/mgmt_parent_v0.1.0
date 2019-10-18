package com.rxh.pojo.payment;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/10
 * Time: 10:09
 * Project: Management
 * Package: com.rxh.pojo.payment
 */
public class ParamRule implements Serializable {
    /*
     * 参数类型：
     * // 可空
     * private static final int OTHER = 0;
     * // 必填项
     * private static final int REQUIRED = 1;
     * // 数字输入
     * private static final int NUMBER = 2;
     * // 金钱格式
     * private static final int MONEY = 3;
     * // 邮箱格式
     * private static final int EMAIL = 4;
     * // IP格式
     * private static final int IP = 5;
     * // 货物列表
     * private static final int GOOD_LIST = 6;
     * // 卡号
     * private static final int CARD_NO = 7;
     * // 有效月份
     * private static final int EXPIRE_MONTH = 8;
     * // 有效年份
     * private static final int EXPIRE_YEAR = 9;
     * // 安全码
     * private static final int SECURITY_NUM = 10;
     */
    private Integer type;
    // 参数最小长度
    private Integer minLength;
    // 参数最大长度
    private Integer maxLength;
    // 异常代码，详见国际化properties文件
    private Integer exceptionCode;

    public ParamRule(Integer type, Integer exceptionCode) {
        this.type = type;
        this.exceptionCode = exceptionCode;
    }

    public ParamRule(Integer type, Integer maxLength, Integer exceptionCode) {
        this.type = type;
        this.maxLength = maxLength;
        this.exceptionCode = exceptionCode;
    }

    public ParamRule(Integer type, Integer minLength, Integer maxLength, Integer exceptionCode) {
        this.type = type;
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.exceptionCode = exceptionCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(Integer exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
