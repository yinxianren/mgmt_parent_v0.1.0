package com.rxh.mapper.square;

import com.rxh.square.pojo.TransOrder;
import com.rxh.square.pojo.TransOrderExample;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TransOrderMapper {
    int countByExample(TransOrderExample example);

    int deleteByExample(TransOrderExample example);

    int deleteByPrimaryKey(String transId);

    int insert(TransOrder record);

    int insertSelective(TransOrder record);

    List<TransOrder> selectByExample(TransOrderExample example);

    TransOrder selectByPrimaryKey(String transId);

    int updateByExampleSelective(@Param("record") TransOrder record, @Param("example") TransOrderExample example);

    int updateByExample(@Param("record") TransOrder record, @Param("example") TransOrderExample example);

    int updateByPrimaryKeySelective(TransOrder record);

    int updateByPrimaryKey(TransOrder record);

    int batchUpdateSuccessOrderStatus(@Param("merOrderIds") List<String> merOrderIds);

    int batchUpdateFailOrderStatus(@Param("merOrderIds") List<String> merOrderIds);

    int updateOrderStatus(@Param("transId")String transId ,@Param("orderStatus")Integer orderStatus);

    int selectSuccessOrderCountByParam(@Param("paramMap") Map<String, Object> paramMap);

    List<TransOrder> selectOrderByParamMap(@Param("paramMap") Map<String, Object> paramMap);

    TransOrder getTransOrderByMerOrderId(@Param("merId")String merId,@Param("merOrderId") String merOrderId);

    TransOrder getTransOrderByMerOrderIdAndMerId(@Param("merId")String merId,@Param("merOrderId") String merOrderId);

    TransOrder getTransOrderByMerOrderIdAndMerIdAndTerMerId(@Param("merOrderId")String merOrderId, @Param("merId")String merId, @Param("terminalMerId")String terminalMerId);

    TransOrder checkTransOrderMul(@Param("merOrderId")String merOrderId, @Param("merId")String merId, @Param("terminalMerId")String terminalMerId);

    BigDecimal getTransOrderAmount(@Param("merId")String merId,@Param("terminalMerId") String terminalMerId, @Param("originalMerOrderId")String originalMerOrderId);

    List<TransOrder> countByChannelId(String id);

    Integer countByMerId(String merId);

    List<TransOrder> getTransOrder(@Param("channelId") String channelId, @Param("orderStatus") Integer orderStatus, @Param("bigenDate") String bigenDate, @Param("endDate") String endDate);

    List<TransOrder> getTransOrderByWhereCondition(TransOrder  transOrder);

    List<TransOrder> selectSuccessOrderByParam(@Param("paramMap") Map<String, Object> paramMap);

    List<TransOrder> selectByParam(Map<String, Object> paramMap);
}
