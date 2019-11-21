package com.rxh.service;

import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;

public interface AnewPayOrderService {

    ResponseVO getList (Page page);

    ResponseVO getCardHolderInfo(String payId);
}
