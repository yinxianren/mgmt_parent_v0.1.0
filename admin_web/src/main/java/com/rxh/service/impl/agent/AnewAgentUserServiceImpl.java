package com.rxh.service.impl.agent;

import com.internal.playment.api.db.agent.ApiAgentUserService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.agent.AgentUserTable;
import com.rxh.service.agent.AnewAgentUserService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnewAgentUserServiceImpl implements AnewAgentUserService {

    @Autowired
    private ApiAgentUserService apiAgentUserService;

    @Override
    public ResponseVO saveOrUpdate(AgentUserTable agentUserTable) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiAgentUserService.saveOrUpdate(agentUserTable);
        if (b){
            responseVO.setMessage(StatusEnum._0.getRemark());
            responseVO.setCode(StatusEnum._0.getStatus());
        }else {
            responseVO.setMessage(StatusEnum._1.getRemark());
            responseVO.setCode(StatusEnum._1.getStatus());
        }
        return responseVO;
    }

    @Override
    public ResponseVO delByIds(List<Long> ids) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiAgentUserService.delByIds(ids);
        if (b){
            responseVO.setMessage(StatusEnum._0.getRemark());
            responseVO.setCode(StatusEnum._0.getStatus());
        }else {
            responseVO.setMessage(StatusEnum._1.getRemark());
            responseVO.setCode(StatusEnum._1.getStatus());
        }
        return responseVO;
    }

    @Override
    public ResponseVO getList(AgentUserTable agentUserTable) {
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiAgentUserService.getList(agentUserTable));
        return responseVO;
    }
}
