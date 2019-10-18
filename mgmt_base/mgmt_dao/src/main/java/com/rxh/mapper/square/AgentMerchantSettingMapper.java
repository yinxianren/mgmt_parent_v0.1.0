package com.rxh.mapper.square;

import com.rxh.square.pojo.AgentMerchantSetting;
import com.rxh.square.pojo.AgentMerchantSettingExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentMerchantSettingMapper {
    int countByExample(AgentMerchantSettingExample example);

    int deleteByExample(AgentMerchantSettingExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgentMerchantSetting record);

    int insertSelective(AgentMerchantSetting record);

    List<AgentMerchantSetting> selectByExample(AgentMerchantSettingExample example);

    AgentMerchantSetting selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgentMerchantSetting record, @Param("example") AgentMerchantSettingExample example);

    int updateByExample(@Param("record") AgentMerchantSetting record, @Param("example") AgentMerchantSettingExample example);

    int updateByPrimaryKeySelective(AgentMerchantSetting record);

    int updateByPrimaryKey(AgentMerchantSetting record);

    List<AgentMerchantSetting> search(AgentMerchantSetting agentMerchantSetting);

    List<AgentMerchantSetting> merSearch(String merId);

    AgentMerchantSetting getAgentMerchantSettingByParentId(String parentId);

    void deleteByAgentMerchantId(String id);

    void updateByMerId(AgentMerchantSetting agentMerchantSetting);

    AgentMerchantSetting getAgentMerchantSettingByParentIdAndPayType(@Param("parentId") String parentId,@Param("payType") String payType);
}
