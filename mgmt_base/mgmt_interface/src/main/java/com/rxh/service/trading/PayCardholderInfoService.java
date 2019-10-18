package com.rxh.service.trading;


import com.rxh.square.pojo.PayCardholderInfo;

/**
 * @author ：zoe
 * @Date ：Created in 2019/5/19 15:41
 */
public interface PayCardholderInfoService {

    PayCardholderInfo selectByPrimaryKey(String payId);

}
