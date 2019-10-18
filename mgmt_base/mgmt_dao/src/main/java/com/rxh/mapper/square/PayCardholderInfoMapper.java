package com.rxh.mapper.square;

import com.rxh.square.pojo.PayCardholderInfo;
import com.rxh.square.pojo.PayCardholderInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PayCardholderInfoMapper {
    int countByExample(PayCardholderInfoExample example);

    int deleteByExample(PayCardholderInfoExample example);

    int deleteByPrimaryKey(String payId);

    int insert(PayCardholderInfo record);

    int insertSelective(PayCardholderInfo record);

    List<PayCardholderInfo> selectByExample(PayCardholderInfoExample example);

    PayCardholderInfo selectByPrimaryKey(String payId);

    int updateByExampleSelective(@Param("record") PayCardholderInfo record, @Param("example") PayCardholderInfoExample example);

    int updateByExample(@Param("record") PayCardholderInfo record, @Param("example") PayCardholderInfoExample example);

    int updateByPrimaryKeySelective(PayCardholderInfo record);

    int updateByPrimaryKey(PayCardholderInfo record);
}