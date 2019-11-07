package com.rxh.service;

import com.rxh.anew.table.agent.AgentMerchantWalletTable;
import com.rxh.pojo.base.Page;
import com.rxh.vo.ResponseVO;

public interface AnewAgentWalletService {

    ResponseVO search(AgentMerchantWalletTable agentMerchantInfoTable);

    ResponseVO pageByDetails(Page page);
}
