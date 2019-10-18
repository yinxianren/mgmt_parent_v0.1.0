package com.rxh.service.trading;

import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.BatchData;
import com.rxh.square.pojo.TransOrder;

import java.util.List;
import java.util.Map;


public interface TransOrderService {

    PageResult findTransOrder(Page page);

    Result getTransBankInfo(String transId);

    Result getProductInfo(String transId);

    TransOrder getTransOrderByPrimaryId(String transId);

    TransOrder selectById(String orderId);

    List<TransOrder> getTransOrder(String channelId, Integer orderStatus, String beginDate,String endDate);

    TransOrder getTransOrderByMerOrderIdAndMerId(String merId,String merOrderId);

    Map<String, Object> getBatchRepayInit(String merId);

    PageResult getBatchRepayList(Page page);

    int batchRepay(List<BatchData> orderChanges, String type, String merId);

    List<TransOrder> getTransOrderByWhereCondition(TransOrder  transOrder);

    int updateByPrimaryKey(TransOrder record);

    int insertBean(TransOrder  transOrder);

    List<TransOrder> selectByMap(Map<String,Object> paramMap);
}
