package com.rxh.controller.trading.util;

/**
 * 状态码实体类
 *
 * @author zoe
 * @date 2019-01-03
 */
public class StatusCode {

    public static final int OK=20000;//成功
    public static final int ERROR=20001;//失败
    public static final int LOGINERROR=20002;//用户名或密码错误
    public static final int ACCESSERROR=20003;//权限不足
    public static final int REEMOTERROR=20004;//远程调用失败
    public static final int REPERROR=20005;//重复操作

}
