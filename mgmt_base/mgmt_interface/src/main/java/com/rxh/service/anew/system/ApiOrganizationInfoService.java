package com.rxh.service.anew.system;

import com.rxh.anew.table.system.OrganizationInfoTable;

import java.util.List;

public interface ApiOrganizationInfoService {

     List<OrganizationInfoTable> getAll(OrganizationInfoTable organizationInfoTable);

     OrganizationInfoTable getOne(OrganizationInfoTable organizationInfoTable);

     Boolean saveOrUpdate(OrganizationInfoTable organizationInfoTable);

     Boolean remove(List<String> ids);

}
