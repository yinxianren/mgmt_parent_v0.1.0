package com.rxh.service.anew.channel;

import com.rxh.anew.table.system.ProductSettingTable;

import java.util.List;

public interface ApiProductTypeSettingService {

     Boolean SaveOrUpdate(ProductSettingTable productSettingTable);

     Boolean removeById(String id);

     List<ProductSettingTable> list(ProductSettingTable productSettingTable);
}
