package com.rxh.service.agent;

import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewAgentMerchantSettingService {

    ResponseVO batchSave(List<AgentMerchantSettingTable> list);

    ResponseVO betchUpdate(List<AgentMerchantSettingTable> list);

    ResponseVO getList(AgentMerchantSettingTable agentMerchantSettingTable);

    ResponseVO delByAgentIds(List<String> agentIds);
}
