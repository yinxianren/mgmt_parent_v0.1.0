package com.rxh.service.agent;

import com.internal.playment.common.table.agent.AgentUserTable;
import com.internal.playment.common.page.ResponseVO;

import java.util.List;

public interface AnewAgentUserService {

    ResponseVO saveOrUpdate(AgentUserTable agentUserTable);

    ResponseVO delByIds(List<Long> ids);

    ResponseVO getList(AgentUserTable agentUserTable);
}
