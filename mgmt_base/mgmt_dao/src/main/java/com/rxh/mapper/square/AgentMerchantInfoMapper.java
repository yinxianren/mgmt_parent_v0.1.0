package com.rxh.mapper.square;

import com.rxh.square.pojo.AgentMerchantInfo;
import com.rxh.square.pojo.AgentMerchantInfoExample;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.vo.VoAgentMerchantInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentMerchantInfoMapper {
    int countByExample(AgentMerchantInfoExample example);

    int deleteByExample(AgentMerchantInfoExample example);

    int deleteByPrimaryKey(String agentMerchantId);

    int insert(AgentMerchantInfo record);

    int insertSelective(AgentMerchantInfo record);

    List<AgentMerchantInfo> selectByExample(AgentMerchantInfoExample example);

    AgentMerchantInfo selectByPrimaryKey(String agentMerchantId);

    int updateByExampleSelective(@Param("record") AgentMerchantInfo record, @Param("example") AgentMerchantInfoExample example);

    int updateByExample(@Param("record") AgentMerchantInfo record, @Param("example") AgentMerchantInfoExample example);

    int updateByPrimaryKeySelective(AgentMerchantInfo record);

    int updateByPrimaryKey(AgentMerchantInfo record);

    List<VoAgentMerchantInfo> getAllByVoAgentMerchantInfo(VoAgentMerchantInfo voAgentMerchantInfo);


    String getAgentMerchantIdIncre();

    List<AgentMerchantInfo> getAllIdAndName();

    List<MerchantInfo> getAllMerchantIdAndName(String merchantId);

    String getMaxAgentMerchantId();

}