package com.rxh.service.impl;

import com.rxh.anew.table.agent.AgentMerchantSettingTable;
import com.rxh.service.AnewAgentMerchantSettingService;
import com.rxh.service.anew.agent.ApiAgentMerchantSettingService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnewAgentMerchantSettingServiceImpl implements AnewAgentMerchantSettingService {
    @Autowired
    private ApiAgentMerchantSettingService apiAgentMerchantSettingService;
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
}
