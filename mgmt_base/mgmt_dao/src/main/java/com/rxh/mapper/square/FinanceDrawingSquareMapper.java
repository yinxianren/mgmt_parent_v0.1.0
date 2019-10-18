package com.rxh.mapper.square;


import com.rxh.pojo.base.SearchInfo;
import com.rxh.square.pojo.FinanceDrawing;
import com.rxh.square.pojo.PayOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FinanceDrawingSquareMapper {
    int deleteByPrimaryKey(String id);

    int insert(FinanceDrawing record);

    int insertSelective(FinanceDrawing record);

    FinanceDrawing selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FinanceDrawing record);

    int updateByPrimaryKey(FinanceDrawing record);

    List<FinanceDrawing> search(@Param("merId") String merId);

    int selectSuccessOrderCountByParam(Map<String, Object> paramMap);

    String getMaxDrawingId();

    List<FinanceDrawing> selectBySearchInfo(SearchInfo searchInfo);
}