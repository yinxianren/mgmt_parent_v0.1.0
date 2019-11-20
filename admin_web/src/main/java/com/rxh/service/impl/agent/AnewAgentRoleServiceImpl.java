package com.rxh.service.impl.agent;

import com.internal.playment.api.db.agent.ApiAgentRoleService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.agent.AgentRoleTable;
import com.rxh.service.agent.AnewAgentRoleService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnewAgentRoleServiceImpl implements AnewAgentRoleService {

    @Autowired
    private ApiAgentRoleService apiAgentRoleService;

    @Override
    public ResponseVO getList(AgentRoleTable agentRoleTable) {
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiAgentRoleService.getList(agentRoleTable));
        return responseVO;
    }
}
