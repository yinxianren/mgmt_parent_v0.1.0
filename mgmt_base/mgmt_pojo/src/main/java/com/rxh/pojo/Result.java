package com.rxh.pojo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: 王德明
 * Date: 2018/6/14
 * Time: 13:46
 * Project: Management_new
 * Package: com.rxh.pojo
 */
public class Result<T> implements Serializable {

    public final static short SUCCESS = 1;
    public final static short FAIL = 0;

    private Short code;
    private String msg;
    private T data;

    public Result() {
    }


    public Result(Short code) {
        this.code = code;
    }

    public Result(Short code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(Short code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Short code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Exception T) {
        this.msg = T.getMessage();
        this.code = FAIL;
    }

    public Short getCode() {
        return code;
    }

    public void setCode(Short code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
