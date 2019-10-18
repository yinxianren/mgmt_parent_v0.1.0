package com.rxh.service.square;

import com.rxh.square.pojo.OrganizationInfo;
import java.util.List;

public interface OrganizationService {
    int insert(OrganizationInfo record);
    int updateByPrimaryKeySelective(OrganizationInfo record);
    int deleteByPrimaryKey( List<String> idList);
    List<OrganizationInfo>  getAll(OrganizationInfo record);
    List<OrganizationInfo>  getIdsAndName();
    List<OrganizationInfo> selectAll();
}
