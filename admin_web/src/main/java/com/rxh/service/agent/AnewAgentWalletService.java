package com.rxh.service.agent;

import com.internal.playment.common.table.agent.AgentMerchantWalletTable;
import com.internal.playment.common.page.Page;
import com.rxh.vo.ResponseVO;

public interface AnewAgentWalletService {

    ResponseVO search(AgentMerchantWalletTable agentMerchantInfoTable);

    ResponseVO pageByDetails(Page page);
}
