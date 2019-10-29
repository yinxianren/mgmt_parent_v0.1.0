package com.rxh.service.anew.agent;

import com.rxh.anew.table.agent.AgentMerchantInfoTable;

import java.util.List;

public interface ApiAgentMerchantInfoService {

    AgentMerchantInfoTable getOne(AgentMerchantInfoTable ami);

    boolean save(AgentMerchantInfoTable ami);

    Boolean saveOrUpdate(AgentMerchantInfoTable agentMerchantInfoTable);

    List<AgentMerchantInfoTable> list(AgentMerchantInfoTable agentMerchantInfoTable);

    Boolean delByIds(List<String> ids);

}
