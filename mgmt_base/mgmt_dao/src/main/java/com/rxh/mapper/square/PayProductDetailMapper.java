package com.rxh.mapper.square;

import com.rxh.square.pojo.PayProductDetail;
import com.rxh.square.pojo.PayProductDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PayProductDetailMapper {
    int countByExample(PayProductDetailExample example);

    int deleteByExample(PayProductDetailExample example);

    int deleteByPrimaryKey(String id);

    int insert(PayProductDetail record);

    int insertSelective(PayProductDetail record);

    List<PayProductDetail> selectByExample(PayProductDetailExample example);

    PayProductDetail selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") PayProductDetail record, @Param("example") PayProductDetailExample example);

    int updateByExample(@Param("record") PayProductDetail record, @Param("example") PayProductDetailExample example);

    int updateByPrimaryKeySelective(PayProductDetail record);

    int updateByPrimaryKey(PayProductDetail record);

    PayProductDetail getProductInfo(String payId);
}