package com.rxh.service.sys;

import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.base.BaseService;

public interface SysConstantService extends BaseService<SysConstant,String> {

    SysConstant getOneByFirstValueAndCode(String firstValue,String groupCode);

}