package com.rxh.service.square;

import com.rxh.square.pojo.NotifyOrder;

import java.util.Date;
import java.util.List;

public interface NotifyOrderService {

    int insert (NotifyOrder notifyOrder);

    List<NotifyOrder> getNotifyOrders(String notifyStatus, Integer notifyNum, Date beginDate,Date endDate);

    NotifyOrder findOneByMerOrderId(String merOrderId);

    void update(NotifyOrder notifyOrder);
}
