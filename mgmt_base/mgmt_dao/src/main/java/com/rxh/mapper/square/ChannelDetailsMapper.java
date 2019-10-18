package com.rxh.mapper.square;

import com.rxh.square.pojo.AgentMerchantsDetails;
import com.rxh.square.pojo.ChannelDetails;
import com.rxh.square.pojo.ChannelDetailsExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ChannelDetailsMapper {
    int countByExample(ChannelDetailsExample example);

    int deleteByExample(ChannelDetailsExample example);

    int deleteByPrimaryKey(String id);

    int insert(ChannelDetails record);

    int insertSelective(ChannelDetails record);

    List<ChannelDetails> selectByExample(ChannelDetailsExample example);

    ChannelDetails selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ChannelDetails record, @Param("example") ChannelDetailsExample example);

    int updateByExample(@Param("record") ChannelDetails record, @Param("example") ChannelDetailsExample example);

    int updateByPrimaryKeySelective(ChannelDetails record);

    int updateByPrimaryKey(ChannelDetails record);
    List<ChannelDetails> selectAllChannelDetails(@Param("paramMap") Map<String, Object> paramMap);
    int selectSuccessCountByParam(@Param("paramMap") Map<String, Object> paramMap);

    ChannelDetails searchByOrderId(String orderId);
}