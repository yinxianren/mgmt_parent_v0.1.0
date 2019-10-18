package com.rxh.service.trading;


import com.rxh.square.pojo.PayProductDetail;

/**
 * @author ：zoe
 * @Date ：Created in 2019/5/19 15:06
 */
public interface PayProductDetailService {

    PayProductDetail selectByPrimaryKey(String payId);

}
