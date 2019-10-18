package com.rxh.mapper.square;

import com.rxh.square.pojo.MerchantsDetails;
import com.rxh.square.pojo.MerchantsDetailsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MerchantsDetailsMapper {
    int countByExample(MerchantsDetailsExample example);

    int deleteByExample(MerchantsDetailsExample example);

    int deleteByPrimaryKey(String id);

    int insert(MerchantsDetails record);

    int insetByBath(@Param("list")List<MerchantsDetails> list);

    int insertSelective(MerchantsDetails record);

    List<MerchantsDetails> selectByExample(MerchantsDetailsExample example);

    MerchantsDetails selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MerchantsDetails record, @Param("example") MerchantsDetailsExample example);

    int updateByExample(@Param("record") MerchantsDetails record, @Param("example") MerchantsDetailsExample example);

    int updateByPrimaryKeySelective(MerchantsDetails record);

    int updateByPrimaryKey(MerchantsDetails record);

    List<MerchantsDetails> selectMerchantsDetails(@Param("paramMap") Map<String, Object> paramMap);

    int countMerchantsDetails(@Param("paramMap") Map<String, Object> paramMap);
    String getMaxMerchantsDetailsId();
    String getMaxMerchantsDetailsOrderId();

    MerchantsDetails getMerchantDetails(String orderId);
}