package com.rxh.mapper.square;

import com.rxh.square.pojo.PayOrder;
import com.rxh.square.pojo.PayOrderExample;
import com.rxh.square.vo.PayOrderDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PayOrderMapper {
    int countByExample(PayOrderExample example);

    int deleteByExample(PayOrderExample example);

    int deleteByPrimaryKey(String payId);

    int insert(PayOrder record);

    int insertSelective(PayOrder record);

    List<PayOrder> selectByExample(PayOrderExample example);

    PayOrder selectByPrimaryKey(String payId);

    int updateByExampleSelective(@Param("record") PayOrder record, @Param("example") PayOrderExample example);

    int updateByExample(@Param("record") PayOrder record, @Param("example") PayOrderExample example);

    int updateByPrimaryKeySelective(PayOrder payOrder);

    int updateByPrimaryKey(PayOrder payOrder);

    List<PayOrder> selectAllOrder(@Param("paramMap") Map<String, Object> paramMap);

    int selectSuccessOrderCountByParam(@Param("paramMap") Map<String, Object> paramMap);

    List<PayOrderDto> getPayOrderBySerach(@Param("paramMap") Map<String, Object> paramMap);

    List<PayOrder> getPayOrderByMerOrderId(@Param("merId")String merId, @Param("merOrderId")String merOrderId);

    PayOrder getProcessingPayOrderByMerOrderId(@Param("merId")String merId, @Param("merOrderId")String merOrderId);

    PayOrder getPayOrderByMerOrderIdAndMerIdAndTerMerId(@Param("merOrderId")String merOrderId, @Param("merId")String merId, @Param("terminalMerId")String terminalMerId);

    PayOrder selectPayOrderByMerOrderIdAndMerIdAndTerMerId(@Param("merOrderId")String merOrderId, @Param("merId")String merId, @Param("terminalMerId")String terminalMerId);

    List<PayOrder> countByChannelId(String id);

    Integer countByMerId(String merId);

    /**
     *  根据对象进行查询
     * @param record
     * @return
     */
    List<PayOrder> getPayOrderByWhereCondition(PayOrder record);

    List<PayOrder> selectSuccessOrderByParam(@Param("paramMap") Map<String, Object> paramMap);

    List<PayOrder> selectByOrders(Map<String,Object> paramMap);
    List<PayOrder> selectByOrders(@Param("merId") String merId,@Param("terminalMerId") String terminalMerId,@Param("merOrderIds") String merOrderIds,@Param("status") String status);

    List<PayOrder> selectByOrderStatusAndSettleStatus(@Param("orderStatus") String orderStatus,@Param("settleStatus")String settleStatus);


}