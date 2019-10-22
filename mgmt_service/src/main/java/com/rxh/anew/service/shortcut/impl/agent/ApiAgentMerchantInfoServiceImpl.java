package com.rxh.anew.service.shortcut.impl.agent;

import com.rxh.anew.service.db.agent.AgentMerchantInfoDbService;
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
public class ApiAgentMerchantInfoServiceImpl implements ApiAgentMerchantInfoService {

    private final AgentMerchantInfoDbService agentMerchantInfoDbService;

}
