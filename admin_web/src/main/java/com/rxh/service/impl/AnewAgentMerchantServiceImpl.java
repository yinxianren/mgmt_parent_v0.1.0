package com.rxh.service.impl;

import com.rxh.anew.table.agent.AgentMerchantInfoTable;
import com.rxh.anew.table.agent.AgentMerchantSettingTable;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.*;
import com.rxh.service.anew.agent.ApiAgentMerchantInfoService;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.management.resources.agent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnewAgentMerchantServiceImpl implements AnewAgentMerchantService {

    @Autowired
    private ApiAgentMerchantInfoService apiAgentMerchantInfoService;
    @Autowired
    private AnewAgentMerchantSettingService anewAgentMerchantSettingService;
    @Autowired
    private ConstantService constantService;

    @Override
    public ResponseVO list(AgentMerchantInfoTable agentMerchantInfoTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(0);
        responseVO.setData(apiAgentMerchantInfoService.list(agentMerchantInfoTable));
        return responseVO;
    }

    @Override
    public ResponseVO save(AgentMerchantInfoTable agentMerchantInfoTable) {
        ResponseVO responseVO = new ResponseVO();
        Boolean b  = apiAgentMerchantInfoService.saveOrUpdate(agentMerchantInfoTable);
        if (b){
            List<SysConstant> constantList = constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PRODUCTTYPE);
            List<AgentMerchantSettingTable> settingTables = new ArrayList<>();
            Date date = new Date();
            //初始化代理商费率
            for (SysConstant sysConstant : constantList){
                AgentMerchantSettingTable agentMerchantSettingTable = new AgentMerchantSettingTable();
                agentMerchantSettingTable.setAgentMerchantId(agentMerchantInfoTable.getAgentMerchantId());
                agentMerchantSettingTable.setCreateTime(date);
                agentMerchantSettingTable.setProductId(sysConstant.getFirstValue());
                agentMerchantSettingTable.setStatus(1);
                agentMerchantSettingTable.setUpdateTime(date);
                settingTables.add(agentMerchantSettingTable);
            }
            anewAgentMerchantSettingService.batchSave(settingTables);
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO delByIds(List<String> ids) {
        ResponseVO responseVO = new ResponseVO();
        Boolean b  = apiAgentMerchantInfoService.delByIds(ids);
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO update(AgentMerchantInfoTable agentMerchantInfoTable) {
        ResponseVO responseVO = new ResponseVO();
        Boolean b  = apiAgentMerchantInfoService.saveOrUpdate(agentMerchantInfoTable);
        if (b){

            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }
}
