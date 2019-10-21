package com.rxh.anew.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.agent.AgentMerchantInfoDbService;
import com.rxh.anew.table.agent.AgentMerchantInfoTable;
import com.rxh.mapper.anew.agent.AnewAgentMerchantInfoMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午5:36
 * Description:
 */

@Service
public class AgentMerchantInfoDbServiceImpl extends ServiceImpl<AnewAgentMerchantInfoMapper, AgentMerchantInfoTable> implements AgentMerchantInfoDbService {
}
