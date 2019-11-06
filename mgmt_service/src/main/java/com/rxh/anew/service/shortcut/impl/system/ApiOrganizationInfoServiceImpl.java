package com.rxh.anew.service.shortcut.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.system.OrganizationInfoDBService;
import com.rxh.anew.table.system.OrganizationInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.system.ApiOrganizationInfoService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ApiOrganizationInfoServiceImpl implements ApiOrganizationInfoService, NewPayAssert {


    private OrganizationInfoDBService organizationInfoDBService;

    @Override
    public List<OrganizationInfoTable> getAll(OrganizationInfoTable oit) {
        if (isNull(oit)) return organizationInfoDBService.list();
        LambdaQueryWrapper<OrganizationInfoTable> queryWrapper = new QueryWrapper<OrganizationInfoTable>().lambda();
        if ( !isBlank(oit.getOrganizationName()))  queryWrapper.eq(OrganizationInfoTable::getOrganizationName, oit.getOrganizationName());
        if ( !isNull(oit.getId()) )  queryWrapper.eq(OrganizationInfoTable::getId, oit.getId());
        if ( isHasElement(oit.getOrganizationIdColl()) )  queryWrapper.in(OrganizationInfoTable::getOrganizationIdColl, oit.getOrganizationIdColl());
        if ( !isNull(oit.getStatus()))  queryWrapper.eq(OrganizationInfoTable::getStatus, oit.getStatus());
        return organizationInfoDBService.list(queryWrapper);
    }

    @Override
    public OrganizationInfoTable getOne(OrganizationInfoTable oit) {
        if (isNull(oit)) return  null;
        LambdaQueryWrapper<OrganizationInfoTable> queryWrapper = new QueryWrapper<OrganizationInfoTable>().lambda();
        if ( !isBlank(oit.getOrganizationId()))  queryWrapper.eq(OrganizationInfoTable::getOrganizationId, oit.getOrganizationId());
        if ( !isNull(oit.getStatus()) )  queryWrapper.eq(OrganizationInfoTable::getStatus, oit.getStatus());
        return organizationInfoDBService.getOne(queryWrapper);
    }

    @Override
    public Boolean saveOrUpdate(OrganizationInfoTable organizationInfoTable) {
        if (isNull(organizationInfoTable)) return false;
        return organizationInfoDBService.saveOrUpdate(organizationInfoTable);
    }

    @Override
    public Boolean remove(List<String> ids) {
        if (isHasNotElement(ids)) return false;
        return organizationInfoDBService.removeByIds(ids);
    }
}
