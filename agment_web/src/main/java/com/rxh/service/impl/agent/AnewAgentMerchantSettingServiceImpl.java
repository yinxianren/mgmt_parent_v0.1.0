package com.rxh.service.impl.agent;

import com.internal.playment.api.db.agent.ApiAgentMerchantInfoService;
import com.internal.playment.api.db.agent.ApiAgentMerchantSettingService;
import com.internal.playment.api.db.system.ApiSysConstantService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.agent.AgentMerchantInfoTable;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.internal.playment.common.table.system.SysConstantTable;
import com.rxh.service.agent.AnewAgentMerchantSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnewAgentMerchantSettingServiceImpl implements AnewAgentMerchantSettingService {
    @Autowired
    private ApiAgentMerchantSettingService apiAgentMerchantSettingService;
    @Autowired
    private ApiAgentMerchantInfoService apiAgentMerchantInfoService;
    @Autowired
    private ApiSysConstantService apiSysConstantService;
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
        for (AgentMerchantSettingTable agentMerchantSettingTable : list){
            if (agentMerchantSettingTable.getId() == null){
                agentMerchantSettingTable.setCreateTime(new Date());
            }
            agentMerchantSettingTable.setUpdateTime(new Date());

        }
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
        List<AgentMerchantSettingTable> settingTables = apiAgentMerchantSettingService.list(agentMerchantSettingTable);
        List<String> products = new ArrayList<>();
        for (AgentMerchantSettingTable agentMerchantSettingTable1 : settingTables){
            products.add(agentMerchantSettingTable1.getProductId());
        }
        SysConstantTable sysConstantTable = new SysConstantTable();
        sysConstantTable.setGroupCode(SystemConstant.PRODUCTTYPE);
        List<SysConstantTable> constantTables = apiSysConstantService.getList(sysConstantTable);
        for (SysConstantTable constantTable : constantTables){
            if (!products.contains(constantTable.getFirstValue())){
                AgentMerchantSettingTable merchantSettingTable = new AgentMerchantSettingTable();
                merchantSettingTable.setProductId(constantTable.getFirstValue());
                merchantSettingTable.setStatus(StatusEnum._1.getStatus());
                merchantSettingTable.setAgentMerchantId(agentMerchantSettingTable.getAgentMerchantId());
                settingTables.add(merchantSettingTable);
            }
        }
        responseVO.setData(settingTables);
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
