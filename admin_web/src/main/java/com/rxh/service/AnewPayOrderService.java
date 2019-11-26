package com.rxh.service;

import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AnewPayOrderService {

    ResponseVO getList (Page page);

    ResponseVO getCardHolderInfo(String payId);

    void findPayOrderExcel(Page page, HttpServletRequest request, HttpServletResponse response);
}
