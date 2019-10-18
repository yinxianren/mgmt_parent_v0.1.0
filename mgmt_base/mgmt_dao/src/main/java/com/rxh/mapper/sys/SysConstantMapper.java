package com.rxh.mapper.sys;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.sys.SysConstant;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysConstantMapper extends BaseMapper<SysConstant,String> {


    List<SysConstant> selectByGroupName(String groupName);

    List<SysConstant> selectByGroupNameAndSortValueIsNotNULL(String groupName);

    List<SysConstant> selectAll();
    List<SysConstant> getProduct();

    SysConstant selectByConstant(SysConstant constant);

    @MapKey("firstValue")
    Map<String, SysConstant> selectByGroupNameForMapKeyFirstValue(String groupName);

	int getConstantAllResultCount(Map<String, Object> paramMap);

    SysConstant getStateByProvince(String province);

    SysConstant getConstantByCurrency(@Param("currency") String currency);

    SysConstant   getOneByFirstValueAndCode(@Param("firstValue")String type, @Param("groupCode")String groupCode);
}