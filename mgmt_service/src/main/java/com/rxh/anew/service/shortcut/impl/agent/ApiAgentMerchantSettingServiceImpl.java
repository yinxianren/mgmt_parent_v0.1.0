package com.rxh.anew.service.shortcut.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.agent.AgentMerchantSettingDBService;
import com.rxh.anew.table.agent.AgentMerchantSettingTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.agent.ApiAgentMerchantSettingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/25
 * Time: 下午7:22
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiAgentMerchantSettingServiceImpl implements ApiAgentMerchantSettingService, NewPayAssert {

    private final AgentMerchantSettingDBService  agentMerchantSettingDBService;

    @Override
    public AgentMerchantSettingTable getOne(AgentMerchantSettingTable ams) {
        if(isNull(ams)) return  null;
        LambdaQueryWrapper<AgentMerchantSettingTable> lambdaQueryWrapper = new QueryWrapper<AgentMerchantSettingTable>().lambda();
        if( !isBlank(ams.getAgentMerchantId())) lambdaQueryWrapper.eq(AgentMerchantSettingTable::getAgentMerchantId,ams.getAgentMerchantId());
        if( !isNull(ams.getStatus()) ) lambdaQueryWrapper.eq(AgentMerchantSettingTable::getStatus,ams.getStatus());
        return agentMerchantSettingDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(AgentMerchantSettingTable ams) {
        if(isNull(ams))  return false;
         return agentMerchantSettingDBService.save(ams);
    }
}
