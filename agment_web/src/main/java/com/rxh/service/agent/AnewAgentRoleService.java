package com.rxh.service.agent;

import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.agent.AgentRoleTable;

public interface AnewAgentRoleService {
    ResponseVO getList(AgentRoleTable agentRoleTable);
}
