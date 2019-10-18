package com.rxh.service;


import com.rxh.pojo.sys.SysGroup;
import com.rxh.service.base.BaseServiceXZM;

import java.util.List;
import java.util.Map;

public interface GroupService extends BaseServiceXZM<SysGroup,String> {
    List<SysGroup> selectAll();

    Map<String, Object> findSysGroup(Map<String, Object> paramMap);

    Map<String, Object> addSysGroup(SysGroup sysGroup);

    Map<String, Object> updateSysGroup(SysGroup sysGroup);

    Map<String, Object> deleteByIds(Map<String, Object> paramMap);

    SysGroup selectUserByGroupName(String groupName);

    SysGroup selectUserByGroupCode(String groupCode);
}
