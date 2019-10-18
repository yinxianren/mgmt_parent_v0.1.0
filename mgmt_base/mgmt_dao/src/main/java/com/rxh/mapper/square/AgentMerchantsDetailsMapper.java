package com.rxh.mapper.square;

import com.rxh.square.pojo.AgentMerchantsDetails;
import com.rxh.square.pojo.AgentMerchantsDetailsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AgentMerchantsDetailsMapper {
    int countByExample(AgentMerchantsDetailsExample example);

    int deleteByExample(AgentMerchantsDetailsExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgentMerchantsDetails record);

    int insertSelective(AgentMerchantsDetails record);

    List<AgentMerchantsDetails> selectByExample(AgentMerchantsDetailsExample example);

    AgentMerchantsDetails selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgentMerchantsDetails record, @Param("example") AgentMerchantsDetailsExample example);

    int updateByExample(@Param("record") AgentMerchantsDetails record, @Param("example") AgentMerchantsDetailsExample example);

    int updateByPrimaryKeySelective(AgentMerchantsDetails record);

    int updateByPrimaryKey(AgentMerchantsDetails record);
    List<AgentMerchantsDetails> selectAllAgentMerchantsDetails(@Param("paramMap") Map<String, Object> paramMap);
    int selectSuccessCountByParam(@Param("paramMap") Map<String, Object> paramMap);

    List<AgentMerchantsDetails> selectAllId();

    AgentMerchantsDetails searchByOrderId(String orderId);

    int insetByBath(@Param("agentMerchantsDetailsList") List<AgentMerchantsDetails> agentMerchantsDetailsList);
}