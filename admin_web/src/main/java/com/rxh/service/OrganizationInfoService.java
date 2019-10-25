package com.rxh.service;

import com.rxh.anew.table.system.OrganizationInfoTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface OrganizationInfoService {

    public ResponseVO getAll(OrganizationInfoTable organizationInfo);

    public ResponseVO savaOrUpdate(OrganizationInfoTable organizationInfo);

    public ResponseVO removeByIds(List<String> idList);
}
