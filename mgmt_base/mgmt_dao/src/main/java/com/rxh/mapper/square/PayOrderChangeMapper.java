package com.rxh.mapper.square;


import com.rxh.square.pojo.PayOrderChange;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PayOrderChangeMapper {
    int deleteByPrimaryKey(String exceptionId);

    int insert(PayOrderChange record);

    int insertSelective(PayOrderChange record);

    PayOrderChange selectByPrimaryKey(String exceptionId);

    int updateByPrimaryKeySelective(PayOrderChange record);

    int updateByPrimaryKey(PayOrderChange record);

    List<PayOrderChange> search(@Param("merId") String merId);

    List<PayOrderChange> selectAllOrder(@Param("paramMap") Map<String, Object> paramMap);

    int selectSuccessOrderCountByParam(@Param("paramMap") Map<String, Object> paramMap);

    PayOrderChange selectByPayId(String payId);

}