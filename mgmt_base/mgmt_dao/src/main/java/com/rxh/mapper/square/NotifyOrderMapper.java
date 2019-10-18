package com.rxh.mapper.square;

import com.rxh.square.pojo.NotifyOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface NotifyOrderMapper {

    int insert(NotifyOrder notifyOrder);

    List<NotifyOrder> getNotifyOrdersByNotifyStatus(@Param("notifyStatus") String notifyStatus, @Param("notifyNum") Integer notifyNum,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);

    NotifyOrder findOneByMerOrderId(@Param("originalOrderId") String originalOrderId);

    void update(NotifyOrder notifyOrder);
}
