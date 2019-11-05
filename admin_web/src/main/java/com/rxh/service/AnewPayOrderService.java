package com.rxh.service;

import com.rxh.pojo.base.Page;
import com.rxh.vo.ResponseVO;

public interface AnewPayOrderService {

    ResponseVO getList (Page page);

    ResponseVO getCardHolderInfo(String payId);
}
