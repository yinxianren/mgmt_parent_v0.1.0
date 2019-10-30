package com.rxh.anew.service.shortcut.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.agent.AgentMerchantWalletDBService;
import com.rxh.anew.table.agent.AgentMerchantWalletTable;
import com.rxh.enums.StatusEnum;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.agent.ApiAgentMerchantWalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午3:31
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiAgentMerchantWalletServiceImpl implements ApiAgentMerchantWalletService, NewPayAssert {

   private final AgentMerchantWalletDBService agentMerchantWalletDBService;

    @Override
    public AgentMerchantWalletTable getOne(AgentMerchantWalletTable amw) {
       if(isNull(amw)) return null;
        LambdaQueryWrapper<AgentMerchantWalletTable> lambdaQueryWrapper = new QueryWrapper<AgentMerchantWalletTable>()
                .lambda().eq(AgentMerchantWalletTable::getStatus, StatusEnum._0.getStatus());
        if(!isBlank(amw.getAgentMerchantId())) lambdaQueryWrapper.eq(AgentMerchantWalletTable::getAgentMerchantId,amw.getAgentMerchantId());
        return agentMerchantWalletDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean updateOrSave(AgentMerchantWalletTable amw) {
        if(isNull(amw)) return false;
        return agentMerchantWalletDBService.saveOrUpdate(amw);
    }
}
