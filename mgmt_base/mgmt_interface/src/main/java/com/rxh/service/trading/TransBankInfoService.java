package com.rxh.service.trading;


import com.rxh.square.pojo.TransBankInfo;

/**
 * @author ：zoe
 * @Date ：Created in 2019/5/19 16:21
 */
public interface TransBankInfoService {

    TransBankInfo selectByPrimaryKey(String transId);
}
