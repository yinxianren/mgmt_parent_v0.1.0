package com.rxh.service;

import com.rxh.pojo.base.Page;
import com.rxh.vo.ResponseVO;

public interface AnewTransOrderService {

    ResponseVO page(Page page);

    ResponseVO getTransBankInfo(String transId);
}
