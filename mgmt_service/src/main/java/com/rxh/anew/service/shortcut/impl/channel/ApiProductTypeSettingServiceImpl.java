package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.channel.ProductTypeSettingDBService;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiProductTypeSettingServiceImpl implements ApiProductTypeSettingService, NewPayAssert {

    @Autowired
    private ProductTypeSettingDBService productTypeSettingDBService;

    public Boolean SaveOrUpdate(ProductSettingTable productSettingTable){
        if(isNull(productSettingTable)) return false;
        return productTypeSettingDBService.saveOrUpdate(productSettingTable);
    }

    public Boolean removeById(String id){
        if(isBlank(id))  return false;
        return productTypeSettingDBService.removeById(id);
    }

    public List<ProductSettingTable> list(ProductSettingTable productSettingTable){
        if(isNull(productSettingTable)) return null;
        LambdaQueryWrapper<ProductSettingTable> lambdaQueryWrapper = new QueryWrapper<ProductSettingTable>().lambda();
        if( !isBlank(productSettingTable.getOrganizationId()) ) lambdaQueryWrapper.eq(ProductSettingTable::getOrganizationId,productSettingTable.getOrganizationId());
        if( !isBlank(productSettingTable.getProductName()) ) lambdaQueryWrapper.eq(ProductSettingTable::getProductName,productSettingTable.getProductName());
        if( !isNull(productSettingTable.getStatus()) ) lambdaQueryWrapper.eq(ProductSettingTable::getStatus,productSettingTable.getStatus());
        return productTypeSettingDBService.list(lambdaQueryWrapper);
    }

    @Override
    public ProductSettingTable getOne(ProductSettingTable productSettingTable) {
        if(isNull(productSettingTable)) return null;
        LambdaQueryWrapper<ProductSettingTable> lambdaQueryWrapper = new QueryWrapper<ProductSettingTable>()
                .lambda().eq(ProductSettingTable::getStatus,productSettingTable.getStatus());//默认只取可用的

        if( !isBlank(productSettingTable.getOrganizationId()) ) lambdaQueryWrapper.eq(ProductSettingTable::getOrganizationId,productSettingTable.getOrganizationId());
        if( !isBlank(productSettingTable.getProductName()) ) lambdaQueryWrapper.eq(ProductSettingTable::getProductName,productSettingTable.getProductName());
        return productTypeSettingDBService.getOne(lambdaQueryWrapper);
    }
}
