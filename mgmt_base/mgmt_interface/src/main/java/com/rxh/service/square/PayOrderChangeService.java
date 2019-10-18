package com.rxh.service.square;

import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.PayOrder;


public interface PayOrderChangeService {
    PageResult findPayOrder(Page page);

    boolean insert(PayOrder payOrder);
}
