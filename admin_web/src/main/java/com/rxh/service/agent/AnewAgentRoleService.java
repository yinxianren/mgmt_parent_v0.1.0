package com.rxh.service.agent;

import com.internal.playment.common.table.agent.AgentRoleTable;
import com.rxh.vo.ResponseVO;

public interface AnewAgentRoleService {
    ResponseVO getList(AgentRoleTable agentRoleTable);
}
