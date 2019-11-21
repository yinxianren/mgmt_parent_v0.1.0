package com.rxh.service.impl.agent;

import com.internal.playment.api.db.agent.ApiAgentMerchantInfoService;
import com.internal.playment.api.db.agent.ApiAgentMerchantSettingService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.agent.AgentMerchantInfoTable;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.rxh.service.agent.AnewAgentMerchantSettingService;
import com.internal.playment.common.page.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnewAgentMerchantSettingServiceImpl implements AnewAgentMerchantSettingService {
    @Autowired
    private ApiAgentMerchantSettingService apiAgentMerchantSettingService;
    @Autowired
    private ApiAgentMerchantInfoService apiAgentMerchantInfoService;
    @Override
    public ResponseVO batchSave(List<AgentMerchantSettingTable> list) {
        Boolean b = apiAgentMerchantSettingService.batchSaveOrUpdate(list);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO betchUpdate(List<AgentMerchantSettingTable> list) {
        Boolean b = apiAgentMerchantSettingService.batchSaveOrUpdate(list);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO getList(AgentMerchantSettingTable agentMerchantSettingTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(0);
        responseVO.setData(apiAgentMerchantSettingService.list(agentMerchantSettingTable));
        return responseVO;
    }

    @Override
    public ResponseVO delByAgentIds(List<String> agentIds) {
        ResponseVO responseVO = new ResponseVO();
        List<Long> list = new ArrayList<>();
        for (String id : agentIds){
            list.add(Long.valueOf(id));
        }
        List<AgentMerchantInfoTable> infoTables = apiAgentMerchantInfoService.listByIds(list);
        List<String> agentMerchantIds = new ArrayList<>();
        for (AgentMerchantInfoTable agentMerchantInfoTable : infoTables){
            agentMerchantIds.add(agentMerchantInfoTable.getAgentMerchantId());
        }
        boolean b = apiAgentMerchantSettingService.delByAgentIds(agentMerchantIds);
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }
}
