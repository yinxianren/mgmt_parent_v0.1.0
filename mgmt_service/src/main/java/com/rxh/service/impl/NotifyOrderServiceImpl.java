package com.rxh.service.impl;

import com.rxh.mapper.square.NotifyOrderMapper;
import com.rxh.service.square.NotifyOrderService;
import com.rxh.square.pojo.NotifyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class NotifyOrderServiceImpl implements NotifyOrderService {
    @Autowired
    private NotifyOrderMapper notifyOrderMapper;
    @Override
    public int insert(NotifyOrder notifyOrder) {

        return notifyOrderMapper.insert(notifyOrder);

    }

    @Override
    public List<NotifyOrder> getNotifyOrders(String notifyStatus, Integer notifyNum, Date beginDate, Date endDate) {

        return notifyOrderMapper.getNotifyOrdersByNotifyStatus(notifyStatus,notifyNum,beginDate,endDate);

    }

    @Override
    public NotifyOrder findOneByMerOrderId(String merOrderId) {

        return notifyOrderMapper.findOneByMerOrderId(merOrderId);

    }

    @Override
    public void update(NotifyOrder notifyOrder) {
         notifyOrderMapper.update(notifyOrder);
    }
}
