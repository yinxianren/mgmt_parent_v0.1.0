package com.rxh.anew.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.agent.AgentWalletDBService;
import com.rxh.anew.table.agent.AgentWalletTable;
import com.rxh.mapper.anew.agent.AnewAgentWalletMapper;
import org.springframework.stereotype.Service;

@Service
public class AgentWalletDBServiceImmpl extends ServiceImpl<AnewAgentWalletMapper, AgentWalletTable> implements AgentWalletDBService {
}
