package com.rxh.service.impl.sys;

import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.mapper.sys.AgentSysLogMapper;
import com.rxh.pojo.sys.SysLog;
import com.rxh.service.sys.AgentSysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentSysLogServiceImpl extends AbstractBaseService<SysLog, Long> implements AgentSysLogService {
    private final AgentSysLogMapper agentSysLogMapper;

    @Autowired
    public AgentSysLogServiceImpl(AgentSysLogMapper agentSysLogMapper) {
        this.agentSysLogMapper = agentSysLogMapper;
        setMapper(agentSysLogMapper);
    }
}