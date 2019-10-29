package com.rxh.service.anew.agent;

import com.rxh.anew.table.agent.AgentMerchantSettingTable;

import java.util.List;

public interface ApiAgentMerchantSettingService {

    AgentMerchantSettingTable getOne(AgentMerchantSettingTable ams);

    boolean save(AgentMerchantSettingTable ams);

    Boolean update(AgentMerchantSettingTable ams);

    Boolean batchSaveOrUpdate(List<AgentMerchantSettingTable> list);

    List<AgentMerchantSettingTable> list(AgentMerchantSettingTable agentMerchantSettingTable);

}
