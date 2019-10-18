package com.rxh.service.trading;


import com.rxh.square.pojo.TransBankInfo;
import com.rxh.square.pojo.TransProductDetail;

/**
 * @author ：zoe
 * @Date ：Created in 2019/5/19 16:34
 */
public interface TransProductDetailService {

    TransProductDetail selectByPrimaryKey(String transId);
}
