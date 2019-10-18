package com.rxh.mapper.square;

import com.rxh.square.pojo.BatchData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BatchRepayMapper {
    List<BatchData> selectAllBatchData(String merId);
    int selectBatchDataCountByParam(@Param("paramMap") Map<String, Object> paramMap);
    List<BatchData> selectAllBatchDataByParam(@Param("paramMap") Map<String, Object> paramMap);
    int insertSelective(BatchData record);
    List<BatchData> selectMerOrder(String merId);

}
