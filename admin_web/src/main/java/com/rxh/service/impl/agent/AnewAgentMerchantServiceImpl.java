package com.rxh.service.impl.agent;

import com.internal.playment.api.db.agent.ApiAgentMerchantInfoService;
import com.internal.playment.api.db.agent.ApiAgentPrivielgesService;
import com.internal.playment.api.db.agent.ApiAgentRoleService;
import com.internal.playment.common.table.agent.AgentMerchantInfoTable;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.internal.playment.common.table.agent.AgentPrivielgesTable;
import com.internal.playment.common.table.agent.AgentRoleTable;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.*;
import com.rxh.service.agent.AnewAgentMerchantService;
import com.rxh.service.agent.AnewAgentMerchantSettingService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnewAgentMerchantServiceImpl implements AnewAgentMerchantService {

    @Autowired
    private ApiAgentMerchantInfoService apiAgentMerchantInfoService;
    @Autowired
    private AnewAgentMerchantSettingService anewAgentMerchantSettingService;
    @Autowired
    private NewSystemConstantService constantService;
    @Autowired
    private ApiAgentPrivielgesService apiAgentPrivielgesService;
    @Autowired
    private ApiAgentRoleService apiAgentRoleService;

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
            List<SysConstant> constantList = (List)constantService.getConstantByGroupName(SystemConstant.PRODUCTTYPE).getData();
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
            AgentRoleTable agentRoleTable = new AgentRoleTable();
            agentRoleTable.setBelongto(agentMerchantInfoTable.getAgentMerchantId());
            agentRoleTable.setRoleName("管理员");
            List<Long> privilegesId = apiAgentPrivielgesService.getList(null)
                    .stream()
                    .filter(merchantPrivileges -> merchantPrivileges.getParentId() != null)
                    .map(AgentPrivielgesTable::getId)
                    .collect(Collectors.toList());
            agentRoleTable.setPrivilegesId(privilegesId.toString().replaceAll("[^,0-9]", ""));
            agentRoleTable.setRole(SystemConstant.ROLE_MERCHANT_USER);
            agentRoleTable.setCreateTime(new Date());
            agentRoleTable.setAvailable(0);
            apiAgentRoleService.saveOrUpdate(agentRoleTable);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO delByIds(List<String> ids) {
        ResponseVO responseVO = new ResponseVO();
        anewAgentMerchantSettingService.delByAgentIds(ids);
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
