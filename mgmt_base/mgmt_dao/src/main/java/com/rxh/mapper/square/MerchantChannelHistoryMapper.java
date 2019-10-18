package com.rxh.mapper.square;

import com.rxh.square.pojo.MerchantChannelHistory;
import com.rxh.square.pojo.MerchantChannelHistoryExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MerchantChannelHistoryMapper {

    int countByExample(MerchantChannelHistoryExample example);

    int deleteByExample(MerchantChannelHistoryExample example);

    int deleteByPrimaryKey(String id);

    int insert(MerchantChannelHistory record);

    int insertSelective(MerchantChannelHistory record);

    List<MerchantChannelHistory> selectByExample(MerchantChannelHistoryExample example);

    MerchantChannelHistory selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MerchantChannelHistory record, @Param("example") MerchantChannelHistoryExample example);

    int updateByExample(@Param("record") MerchantChannelHistory record, @Param("example") MerchantChannelHistoryExample example);

    int updateByPrimaryKeySelective(MerchantChannelHistory record);

    int updateByPrimaryKey(MerchantChannelHistory record);

    MerchantChannelHistory getLastUseChannel(@Param("merId") String merId,@Param("status") String status);

    List<MerchantChannelHistory> merchantChannelHistory(@Param("paramMap") Map<String, Object> paramMap);

    int merchantChannelHistoryCount(@Param("paramMap") Map<String, Object> paramMap);

}
