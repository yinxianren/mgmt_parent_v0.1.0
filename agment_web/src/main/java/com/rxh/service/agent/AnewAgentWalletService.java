package com.rxh.service.agent;

import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.agent.AgentMerchantWalletTable;

public interface AnewAgentWalletService {

    ResponseVO search(AgentMerchantWalletTable agentMerchantInfoTable);

    ResponseVO pageByDetails(Page page);
}
