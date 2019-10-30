package com.rxh.anew.service.shortcut.impl.agent;

import com.rxh.anew.service.db.agent.AgentMerchantsDetailsDBService;
import com.rxh.anew.table.agent.AgentMerchantsDetailsTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.agent.ApiAgentMerchantsDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午3:30
 * Description:    com.rxh.service.anew.agent.ApiAgentMerchantsDetailsService com.rxh.service.anew.agent.ApiAgentMerchantWalletService
 */
@AllArgsConstructor
@Service
public class ApiAgentMerchantsDetailsServiceImpl implements ApiAgentMerchantsDetailsService, NewPayAssert {

    private final AgentMerchantsDetailsDBService agentMerchantsDetailsDBService;

    @Override
    public boolean save(AgentMerchantsDetailsTable amd) {
        if(isNull(amd)) return  false;
        return agentMerchantsDetailsDBService.save(amd);
    }
}
