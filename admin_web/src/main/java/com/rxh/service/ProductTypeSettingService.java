package com.rxh.service;

import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.vo.ResponseVO;

public interface ProductTypeSettingService {

    ResponseVO selectByOrganizationId(ProductSettingTable productSettingTable);

    ResponseVO addProduct(ProductSettingTable productSettingTable);
}
