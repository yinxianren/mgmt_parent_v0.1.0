package com.rxh.mapper.sys;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.sys.SysNews;

public interface SysNewsMapper extends BaseMapper<SysNews, String> {

    int updateByPrimaryKeyWithBLOBs(SysNews record);

}