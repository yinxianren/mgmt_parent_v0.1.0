package com.rxh.anew.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.agent.AgentMerchantsDetailsDBService;
import com.rxh.anew.table.agent.AgentMerchantsDetailsTable;
import com.rxh.mapper.anew.agent.AnewAgentMerchantDetailsMapper;
import org.springframework.stereotype.Service;

@Service
public class AgentMerchantsDetailsDBServiceImpl extends ServiceImpl<AnewAgentMerchantDetailsMapper, AgentMerchantsDetailsTable> implements AgentMerchantsDetailsDBService {
}
