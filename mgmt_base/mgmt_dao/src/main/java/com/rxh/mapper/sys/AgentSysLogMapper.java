package com.rxh.mapper.sys;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.sys.SysLog;
import com.rxh.vo.VoSysLog;

import java.util.List;

public interface AgentSysLogMapper extends BaseMapper<SysLog, Long> {

    List<SysLog> selectBySearchInfo(VoSysLog log);
}