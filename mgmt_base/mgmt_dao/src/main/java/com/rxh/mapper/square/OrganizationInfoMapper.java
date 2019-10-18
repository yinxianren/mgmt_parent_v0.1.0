package com.rxh.mapper.square;

import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.square.pojo.OrganizationInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrganizationInfoMapper {
    int countByExample(OrganizationInfoExample example);

    int selectMaxId();

    int deleteByExample(OrganizationInfoExample example);

    int deleteByPrimaryKey(String organizationId);

    int insert(OrganizationInfo record);

    int insertSelective(OrganizationInfo record);

    List<OrganizationInfo> selectByExample(OrganizationInfoExample example);

    OrganizationInfo selectByPrimaryKey(String organizationId);
    List<OrganizationInfo> selectByObj(@Param("record")OrganizationInfo record);

    int updateByExampleSelective(@Param("record") OrganizationInfo record, @Param("example") OrganizationInfoExample example);

    int updateByExample(@Param("record") OrganizationInfo record, @Param("example") OrganizationInfoExample example);

    int updateByPrimaryKeySelective(OrganizationInfo record);

    int updateByPrimaryKey(OrganizationInfo record);

    String selectLastId();

    List<OrganizationInfo> getAll();

    List<OrganizationInfo> getgetIdsAndName();

    String getIdIncre();
}