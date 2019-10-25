package com.rxh.service.anew.agent;

import com.rxh.anew.table.agent.AgentMerchantInfoTable;

public interface ApiAgentMerchantInfoService {

    AgentMerchantInfoTable getOne(AgentMerchantInfoTable ami);

    boolean save(AgentMerchantInfoTable ami);

}
