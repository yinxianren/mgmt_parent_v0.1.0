package com.rxh.service.anew.agent;

import com.rxh.anew.table.agent.AgentMerchantSettingTable;

public interface ApiAgentMerchantSettingService {

    AgentMerchantSettingTable getOne(AgentMerchantSettingTable ams);

    boolean save(AgentMerchantSettingTable ams);

}
