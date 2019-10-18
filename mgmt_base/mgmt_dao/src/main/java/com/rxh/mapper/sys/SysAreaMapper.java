package com.rxh.mapper.sys;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.sys.SysArea;

import java.util.List;

public interface SysAreaMapper extends BaseMapper<SysArea, Integer> {
    List<SysArea> selectAreaByCountryCode(String countryCode);
}