package com.rxh.service.trading;

import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.PayOrder;

import java.util.List;
import java.util.Map;


/**
 * @ClassName : PayOrderService
 * @Author : zoe
 * @Date : 2019/5/19 13:32
 */
public interface PayOrderService {

     PageResult findPayOrder(Page page);

     PageResult search(Page page);

     Result getCardHolderInfo(String payId);

     Result getProductInfo(String payId);

     PayOrder selectByPayId(String payId);

     List<PayOrder> getPayOrderByWhereCondition(PayOrder record);

     int updateByPrimaryKey(PayOrder payOrder);

     int insertBean(PayOrder payOrder);

     PayOrder seleteBymerOrderId(String merId,String merOrderId,String terminalMerId);

     List<PayOrder> selectByOrders(Map<String,Object> paramMap);

     List<PayOrder> selectByOrderStatusAndSettleStatus(String orderStatus,String settleStatus);

     int updateByPrimaryKeySelective(PayOrder payOrder);
}
