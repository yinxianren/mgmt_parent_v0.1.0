package com.rxh.anew.service.impl.agent;

import com.rxh.anew.service.db.agent.AgentMerchantInfoDbService;
import com.rxh.anew.table.agent.AgentMerchantInfoTable;
import com.rxh.service.anew.agent.AnewAgentMerchantInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33
 * Description:
 */

@Service
public class AnewAgentMerchantInfoServiceImpl implements AnewAgentMerchantInfoService {

    @Autowired
    private AgentMerchantInfoDbService agentMerchantInfoDbService;


    @Override
    public String test() {
         List<AgentMerchantInfoTable>  list= agentMerchantInfoDbService.list();
        return "AgentMerchantInfoServiceImpl implements AgentMerchantInfoService";
    }
}
