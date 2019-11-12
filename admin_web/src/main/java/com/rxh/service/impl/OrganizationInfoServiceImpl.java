package com.rxh.service.impl;

import com.internal.playment.api.db.channel.ApiProductTypeSettingService;
import com.internal.playment.api.db.system.ApiOrganizationInfoService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.common.table.system.ProductSettingTable;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.service.OrganizationInfoService;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrganizationInfoServiceImpl implements OrganizationInfoService, NewPayAssert {

    @Autowired
    private ApiOrganizationInfoService apiOrganizationInfoService;
    @Autowired
    private ApiProductTypeSettingService apiProductTypeSettingService;
    @Autowired
    private ConstantService constantService;


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
                if (productSettingTable1.getStatus() == 0)
                    sj.add(productSettingTable1.getProductId());
            }}
            organizationInfo1.setProductIds(sj.toString());
            organizationInfo1.setProductTypes(productSettingTables);
        }
        ResponseVO responseVO = new ResponseVO();
        responseVO.setData(organizationInfoTables);
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage("成功");
        return responseVO;
    }

    @Override
    public ResponseVO savaOrUpdate(OrganizationInfoTable organizationInfo) {
        Boolean b = apiOrganizationInfoService.saveOrUpdate(organizationInfo);
        Map<String, SysConstant> proMap = constantService.getConstantsMapByGroupName(SystemConstant.PRODUCTTYPE);
        ProductSettingTable table = new ProductSettingTable();
        table.setOrganizationId(organizationInfo.getOrganizationId());
        List<ProductSettingTable> listed = apiProductTypeSettingService.list(table);
        List<ProductSettingTable> list = new ArrayList<>();
        List<String> idsed = new ArrayList<>();//已存在的产品id
        List<String> ids = StringUtils.isNotEmpty(organizationInfo.getProductIds())?Arrays.asList(organizationInfo.getProductIds().split(",")):new ArrayList<>();
        for (ProductSettingTable productSettingTable : listed){
            if (ids.contains(productSettingTable.getProductId())){
                productSettingTable.setStatus(StatusEnum._0.getStatus());
            }else {
                productSettingTable.setStatus(StatusEnum._1.getStatus());
            }
            productSettingTable.setUpdateTime(new Date());
            list.add(productSettingTable);
            idsed.add(productSettingTable.getProductId());
        }
        //删除已存在的产品id，剩余的做新增
        ids = new ArrayList<>(ids);
        if (idsed.containsAll(ids)){
            ids = new ArrayList<>();
        }else {
            ids.removeAll(idsed);
        }

        for (String productId : ids){
            ProductSettingTable productSettingTable = new ProductSettingTable();
            productSettingTable.setStatus(StatusEnum._0.getStatus());
            productSettingTable.setCreateTime(new Date());
            productSettingTable.setOrganizationId(organizationInfo.getOrganizationId());
            productSettingTable.setProductId(productId);
            productSettingTable.setUpdateTime(new Date());
            productSettingTable.setProductName(proMap.get(productId).getName());
            list.add(productSettingTable);
        }
        if (isHasElement(list))apiProductTypeSettingService.saveOrUpdateBatch(list);
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
