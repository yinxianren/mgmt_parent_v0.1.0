package com.rxh.anew.service.shortcut.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.system.OrganizationInfoDBService;
import com.rxh.anew.table.system.OrganizationInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.system.ApiOrganizationInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiOrganizationInfoServiceImpl implements ApiOrganizationInfoService, NewPayAssert {

    @Autowired
    private OrganizationInfoDBService organizationInfoDBService;

    @Override
    public List<OrganizationInfoTable> getAll(OrganizationInfoTable organizationInfoTable) {
        if (isNull(organizationInfoTable)) return organizationInfoDBService.list();
        LambdaQueryWrapper<OrganizationInfoTable> queryWrapper = new QueryWrapper<OrganizationInfoTable>().lambda();
        if (StringUtils.isNotEmpty(organizationInfoTable.getOrganizationName()))  queryWrapper.eq(OrganizationInfoTable::getOrganizationName, organizationInfoTable.getOrganizationName());
        if ((organizationInfoTable.getId()!= null) && organizationInfoTable.getId()>0)  queryWrapper.eq(OrganizationInfoTable::getId, organizationInfoTable.getId());
        return organizationInfoDBService.list(queryWrapper);
    }

    @Override
    public Boolean savaOrUpdate(OrganizationInfoTable organizationInfoTable) {
        if (isNull(organizationInfoTable)) return false;
        return organizationInfoDBService.saveOrUpdate(organizationInfoTable);
    }

    @Override
    public Boolean remove(List<String> ids) {
        if (isHasNotElement(ids)) return false;
        return organizationInfoDBService.removeByIds(ids);
    }
}
