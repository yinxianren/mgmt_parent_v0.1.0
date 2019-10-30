package com.rxh.service.anew.agent;

import com.rxh.anew.table.agent.AgentMerchantWalletTable;

public interface ApiAgentMerchantWalletService {

    AgentMerchantWalletTable getOne(AgentMerchantWalletTable amt);

    boolean updateOrSave(AgentMerchantWalletTable amt);
}
