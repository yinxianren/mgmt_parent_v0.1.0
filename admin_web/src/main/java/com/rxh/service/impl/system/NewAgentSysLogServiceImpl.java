package com.rxh.service.impl.system;

import com.internal.playment.api.db.system.ApiAgentSysLogService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.AgentSysLogTable;
import com.rxh.pojo.base.Page;
import com.rxh.service.system.NewAgentSysLogService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewAgentSysLogServiceImpl implements NewAgentSysLogService {

    @Autowired
    private ApiAgentSysLogService apiAgentSysLogService;

    @Override
    public ResponseVO page(Page page) {
        AgentSysLogTable agentSysLogTable = new AgentSysLogTable();
        PageDTO pageDTO = new PageDTO(page.getPageNum(),page.getPageSize());
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiAgentSysLogService.page(agentSysLogTable,pageDTO));
        return responseVO;
    }
}
