package com.rxh.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseVO implements Serializable {

    private Integer code;
    private String message;
    private Object Data;
    private PageInfo pageinfo;
}
