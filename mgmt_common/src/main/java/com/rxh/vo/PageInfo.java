package com.rxh.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageInfo implements Serializable {

    private Integer pageNum;
    private Integer pageSize;
    private Integer totalNum;
    private Integer totalPages;

}
