package com.rxh.anew.service.shortcut.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.agent.AgentMerchantInfoDbService;
import com.rxh.anew.table.agent.AgentMerchantInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.agent.ApiAgentMerchantInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
