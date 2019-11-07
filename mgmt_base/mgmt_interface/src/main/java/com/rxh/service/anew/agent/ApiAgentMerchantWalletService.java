package com.rxh.service.anew.agent;

import com.rxh.anew.table.agent.AgentMerchantWalletTable;

import java.util.List;

public interface ApiAgentMerchantWalletService {

    AgentMerchantWalletTable getOne(AgentMerchantWalletTable amt);

    boolean updateOrSave(AgentMerchantWalletTable amt);

    List<AgentMerchantWalletTable> getList(AgentMerchantWalletTable agentMerchantWalletTable);
}
