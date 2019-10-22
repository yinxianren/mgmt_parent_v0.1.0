package com.rxh.service.anew.channel;

import com.rxh.anew.table.system.ProductSettingTable;

import java.util.List;

public interface ApiProductTypeSettingService {

    public Boolean SaveOrUpdate(ProductSettingTable productSettingTable);

    public Boolean removeById(String id);

    public List<ProductSettingTable> list(ProductSettingTable productSettingTable);
}
