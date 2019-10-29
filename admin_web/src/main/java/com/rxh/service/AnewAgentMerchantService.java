package com.rxh.service;

import com.rxh.anew.table.agent.AgentMerchantInfoTable;
import com.rxh.square.pojo.AgentMerchantInfo;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewAgentMerchantService {

    ResponseVO list(AgentMerchantInfoTable agentMerchantInfoTable);
    ResponseVO save(AgentMerchantInfoTable agentMerchantInfoTable);
    ResponseVO delByIds(List<String> ids);
    ResponseVO update(AgentMerchantInfoTable agentMerchantInfoTable);
}
