package com.rxh.service;


import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.base.BaseServiceXZM;

import java.util.List;
import java.util.Map;


public interface ConstantService extends BaseServiceXZM<SysConstant,String> {


	SysConstant selectByConstant(SysConstant constant);

    Map<String, Object> deleteByIds(Map<String, Object> paramMap);

    List<SysConstant> getConstantByGroupName(String groupName);

    List<SysConstant> getConstantByGroupNameAndSortValueIsNotNULL(String groupName);

    List<SysConstant> getConstantByGroupNameAndSortValueIsNULL(String groupName);

    List<SysConstant> selectAll();

    Map<String, Map<String, SysConstant>> getConstantsMapByGroupNames(List<String> groupNames);
    
    public Map<String, Object> getConstantInfo(Map<String, Object> paramMap); 

    Map<String, SysConstant> getConstantsMapByGroupName(String groupName);
     SysConstant getStateByProvince(String province);

}
