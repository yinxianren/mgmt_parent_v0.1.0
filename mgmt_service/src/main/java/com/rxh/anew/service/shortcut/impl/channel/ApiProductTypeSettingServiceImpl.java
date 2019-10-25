package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.system.ProductTypeSettingDBService;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.enums.StatusEnum;
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

    public Boolean removeById(List<Long> ids){
        if(isHasNotElement(ids))  return false;
        return productTypeSettingDBService.removeByIds(ids);
    }

    public List<ProductSettingTable> list(ProductSettingTable productSettingTable){
        if(isNull(productSettingTable)) return null;
        LambdaQueryWrapper<ProductSettingTable> lambdaQueryWrapper = new QueryWrapper<ProductSettingTable>().lambda();
        if( !isBlank(productSettingTable.getOrganizationId()) ) lambdaQueryWrapper.eq(ProductSettingTable::getOrganizationId,productSettingTable.getOrganizationId());
        if( !isBlank(productSettingTable.getProductId()) ) lambdaQueryWrapper.eq(ProductSettingTable::getProductId,productSettingTable.getProductId());
        if( !isNull(productSettingTable.getStatus()) ) lambdaQueryWrapper.eq(ProductSettingTable::getStatus,productSettingTable.getStatus());
        return productTypeSettingDBService.list(lambdaQueryWrapper);
    }

    @Override
    public ProductSettingTable getOne(ProductSettingTable productSettingTable) {
        if(isNull(productSettingTable)) return null;
        LambdaQueryWrapper<ProductSettingTable> lambdaQueryWrapper = new QueryWrapper<ProductSettingTable>()
                .lambda().eq(ProductSettingTable::getStatus, StatusEnum._0.getStatus());//默认只取可用的

        if( !isBlank(productSettingTable.getOrganizationId()) ) lambdaQueryWrapper.eq(ProductSettingTable::getOrganizationId,productSettingTable.getOrganizationId());
        if( !isBlank(productSettingTable.getProductName()) ) lambdaQueryWrapper.eq(ProductSettingTable::getProductName,productSettingTable.getProductName());
        return productTypeSettingDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public Boolean batchUpdate(List<ProductSettingTable> productSettingTableList) {
        if(isHasNotElement(productSettingTableList)) return false;
        return productTypeSettingDBService.updateBatchById(productSettingTableList);
    }
}
