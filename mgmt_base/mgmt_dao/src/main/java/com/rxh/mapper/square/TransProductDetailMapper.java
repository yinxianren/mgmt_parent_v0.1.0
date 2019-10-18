package com.rxh.mapper.square;

import com.rxh.square.pojo.PayProductDetail;
import com.rxh.square.pojo.TransProductDetail;
import com.rxh.square.pojo.TransProductDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransProductDetailMapper {
    int countByExample(TransProductDetailExample example);

    int deleteByExample(TransProductDetailExample example);

    int deleteByPrimaryKey(String id);

    int insert(TransProductDetail record);

    int insertSelective(TransProductDetail record);

    List<TransProductDetail> selectByExample(TransProductDetailExample example);

    TransProductDetail selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TransProductDetail record, @Param("example") TransProductDetailExample example);

    int updateByExample(@Param("record") TransProductDetail record, @Param("example") TransProductDetailExample example);

    int updateByPrimaryKeySelective(TransProductDetail record);

    int updateByPrimaryKey(TransProductDetail record);

    TransProductDetail getProductInfo(String transId);
}