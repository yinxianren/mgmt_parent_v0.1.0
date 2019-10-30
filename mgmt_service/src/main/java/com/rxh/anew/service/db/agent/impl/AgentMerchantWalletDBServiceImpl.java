package com.rxh.anew.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.agent.AgentMerchantWalletDBService;
import com.rxh.anew.table.agent.AgentMerchantWalletTable;
import com.rxh.mapper.anew.agent.AnewAgentMerchantWalletMapper;
import org.springframework.stereotype.Service;

@Service
public class AgentMerchantWalletDBServiceImpl extends ServiceImpl<AnewAgentMerchantWalletMapper, AgentMerchantWalletTable> implements AgentMerchantWalletDBService {
}
