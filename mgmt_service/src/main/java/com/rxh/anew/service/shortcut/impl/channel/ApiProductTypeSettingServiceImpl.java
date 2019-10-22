package com.rxh.anew.service.shortcut.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.channel.ProductTypeSettingDBService;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiProductTypeSettingServiceImpl implements ApiProductTypeSettingService {

    @Autowired
    private ProductTypeSettingDBService productTypeSettingDBService;

    public Boolean SaveOrUpdate(ProductSettingTable productSettingTable){
        return productTypeSettingDBService.saveOrUpdate(productSettingTable);
    }

    public Boolean removeById(String id){
        return productTypeSettingDBService.removeById(id);
    }

    public List<ProductSettingTable> list(ProductSettingTable productSettingTable){
        LambdaQueryWrapper<ProductSettingTable> lambdaQueryWrapper = new QueryWrapper<ProductSettingTable>().lambda();
        lambdaQueryWrapper.eq(ProductSettingTable::getOrganizationId,productSettingTable.getOrganizationId());
        return productTypeSettingDBService.list(lambdaQueryWrapper);
    }
}
