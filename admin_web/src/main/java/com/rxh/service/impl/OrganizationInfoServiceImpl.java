package com.rxh.service.impl;

import com.rxh.anew.table.system.OrganizationInfoTable;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.OrganizationInfoService;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import com.rxh.service.anew.system.ApiOrganizationInfoService;
import com.rxh.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Service
public class OrganizationInfoServiceImpl implements OrganizationInfoService, NewPayAssert {

    @Autowired
    private RedisCacheCommonCompoment redisCacheCommonCompoment;
    @Autowired
    private ApiOrganizationInfoService apiOrganizationInfoService;
    @Autowired
    private ApiProductTypeSettingService apiProductTypeSettingService;


    public ResponseVO getAll(OrganizationInfoTable organizationInfo){

        List<OrganizationInfoTable> organizationInfoTables = apiOrganizationInfoService.getAll(organizationInfo);
//        List<OrganizationInfo> organizationInfos = null;
//        if (organizationInfo == null){
//            organizationInfos = redisCacheCommonCompoment.organizationInfoCache.getAll();
//        }

        /*if(null == organizationInfos || organizationInfos.isEmpty()){
            organizationInfos = organizationService.getAll(organizationInfo);
        }*/
        for (OrganizationInfoTable organizationInfo1 : organizationInfoTables){
            ProductSettingTable productSettingTable = new ProductSettingTable();
            productSettingTable.setOrganizationId(organizationInfo1.getOrganizationId());
            List<ProductSettingTable> productSettingTables = apiProductTypeSettingService.list(productSettingTable);
            StringJoiner sj = new StringJoiner(",");
            for (ProductSettingTable productSettingTable1 : productSettingTables){{
                if (productSettingTable1.getStatus() == 1)
                    sj.add(productSettingTable1.getProductId());
            }}
            organizationInfo1.setProductIds(sj.toString());
            organizationInfo1.setProductTypes(productSettingTables);
        }
        ResponseVO responseVO = new ResponseVO();
        responseVO.setData(organizationInfoTables);
        responseVO.setCode(0);
        responseVO.setMessage("成功");
        return responseVO;
    }

    @Override
    public ResponseVO savaOrUpdate(OrganizationInfoTable organizationInfo) {
        Boolean b = apiOrganizationInfoService.saveOrUpdate(organizationInfo);
        ProductSettingTable table = new ProductSettingTable();
        table.setOrganizationId(organizationInfo.getOrganizationId());
        List<ProductSettingTable> list = apiProductTypeSettingService.list(table);
        List<String> ids = StringUtils.isNotEmpty(organizationInfo.getProductIds())?Arrays.asList(organizationInfo.getProductIds().split(",")):new ArrayList<>();
        for (ProductSettingTable productSettingTable : list){
            if (ids.contains(productSettingTable.getProductId())){
                if (productSettingTable.getStatus() == 1)
                    continue;
                productSettingTable.setStatus(1);
            }else {
                productSettingTable.setStatus(0);
            }
        }
        if (isHasElement(list))apiProductTypeSettingService.batchUpdate(list);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO removeByIds(List<String> idList) {
        List<Long> productIds = new ArrayList<>();
        for (String id : idList){
            ProductSettingTable productSettingTable = new ProductSettingTable();
            OrganizationInfoTable organizationInfoTable = new OrganizationInfoTable();
            organizationInfoTable.setId(Long.valueOf(id));
            List<OrganizationInfoTable> list = apiOrganizationInfoService.getAll(organizationInfoTable);
            organizationInfoTable = list.get(0);
            productSettingTable.setOrganizationId(organizationInfoTable.getOrganizationId());
            List<ProductSettingTable> productSettingTables = apiProductTypeSettingService.list(productSettingTable);
            for (ProductSettingTable productSettingTable1 : productSettingTables){
                productIds.add(productSettingTable1.getId());
            }
        }
        apiProductTypeSettingService.removeById(productIds);
        Boolean b = apiOrganizationInfoService.remove(idList);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

}
