package com.rxh.service.anew.system;

import com.rxh.anew.table.system.OrganizationInfoTable;

import java.util.List;

public interface ApiOrganizationInfoService {

    public List<OrganizationInfoTable> getAll(OrganizationInfoTable organizationInfoTable);

    public Boolean savaOrUpdate(OrganizationInfoTable organizationInfoTable);

    public Boolean remove(List<OrganizationInfoTable> organizationInfoTables);

}
