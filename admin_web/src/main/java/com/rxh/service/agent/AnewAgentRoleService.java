package com.rxh.service.agent;

import com.internal.playment.common.table.agent.AgentRoleTable;
import com.internal.playment.common.page.ResponseVO;

public interface AnewAgentRoleService {
    ResponseVO getList(AgentRoleTable agentRoleTable);
}
