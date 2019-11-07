package com.rxh.service.anew.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rxh.anew.table.agent.AgentMerchantsDetailsTable;

public interface ApiAgentMerchantsDetailsService {

    boolean save(AgentMerchantsDetailsTable amd);

    IPage page(AgentMerchantsDetailsTable amd);

}
