package com.rxh.mapper.sys;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.sys.SysGroup;

import java.util.List;
import java.util.Map;

public interface SysGroupMapper extends BaseMapper<SysGroup,String> {
    List<SysGroup> selectAll();



    List<SysGroup> selectOrderBySearchInfo();


    int getConstantAllResultCount(Map<String, Object> paramMap);

    SysGroup selectUserByGroupName(String groupName);
    SysGroup selectUserByGroupCode(String groupCode);
}