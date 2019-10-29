package com.rxh.anew.service.shortcut.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.agent.AgentMerchantInfoDbService;
import com.rxh.anew.table.agent.AgentMerchantInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.agent.ApiAgentMerchantInfoService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiAgentMerchantInfoServiceImpl implements ApiAgentMerchantInfoService, NewPayAssert {

    private final AgentMerchantInfoDbService agentMerchantInfoDbService;

    @Override
    public AgentMerchantInfoTable getOne(AgentMerchantInfoTable ami) {
        if(isNull(ami)) return null;
        LambdaQueryWrapper<AgentMerchantInfoTable> lambdaQueryWrapper =new QueryWrapper<AgentMerchantInfoTable>().lambda();
        if( !isBlank(ami.getAgentMerchantId()) ) lambdaQueryWrapper.eq(AgentMerchantInfoTable::getAgentMerchantId,ami.getAgentMerchantId());

        return agentMerchantInfoDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(AgentMerchantInfoTable ami) {
        if(isNull(ami)) return false;
        return agentMerchantInfoDbService.save(ami);
    }

    @Override
    public Boolean saveOrUpdate(AgentMerchantInfoTable agentMerchantInfoTable) {
        if (isNull(agentMerchantInfoTable)) return false;
        return agentMerchantInfoDbService.saveOrUpdate(agentMerchantInfoTable);
    }

    @Override
    public List<AgentMerchantInfoTable> list(AgentMerchantInfoTable agentMerchantInfoTable) {
        if (isNull(agentMerchantInfoTable)) return agentMerchantInfoDbService.list();
        LambdaQueryWrapper<AgentMerchantInfoTable> queryWrapper = new QueryWrapper<AgentMerchantInfoTable>().lambda();
        if (!isNull(agentMerchantInfoTable.getStatus())) queryWrapper.eq(AgentMerchantInfoTable::getStatus,agentMerchantInfoTable.getStatus());
        if (StringUtils.isNotEmpty(agentMerchantInfoTable.getAgentMerchantId())) queryWrapper.eq(AgentMerchantInfoTable::getAgentMerchantId,agentMerchantInfoTable.getAgentMerchantId());
        if (StringUtils.isNotEmpty(agentMerchantInfoTable.getAgentMerchantName())) queryWrapper.eq(AgentMerchantInfoTable::getAgentMerchantName,agentMerchantInfoTable.getAgentMerchantName());
        return agentMerchantInfoDbService.list(queryWrapper);
    }

    @Override
    public Boolean delByIds(List<String> ids) {
        if (isHasNotElement(ids)) return false;
        return agentMerchantInfoDbService.removeByIds(ids);
    }
}
