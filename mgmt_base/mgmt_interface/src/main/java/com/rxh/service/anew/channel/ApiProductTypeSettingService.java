package com.rxh.service.anew.channel;

import com.rxh.anew.table.system.ProductSettingTable;

import java.util.List;

public interface ApiProductTypeSettingService {

     Boolean SaveOrUpdate(ProductSettingTable productSettingTable);

     Boolean removeById(List<Long> id);

     List<ProductSettingTable> list(ProductSettingTable productSettingTable);

     ProductSettingTable  getOne(ProductSettingTable productSettingTable);

     Boolean batchUpdate(List<ProductSettingTable> productSettingTableList);
}
