package com.rxh.service;

import com.internal.playment.common.page.Page;
import com.rxh.vo.ResponseVO;

public interface AnewTransOrderService {

    ResponseVO page(Page page);

    ResponseVO getTransBankInfo(String transId);
}
