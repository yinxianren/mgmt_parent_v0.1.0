package com.rxh.service;

import com.internal.playment.common.table.system.ProductSettingTable;
import com.internal.playment.common.page.ResponseVO;

public interface ProductTypeSettingService {

    ResponseVO selectByOrganizationId(ProductSettingTable productSettingTable);

    ResponseVO addProduct(ProductSettingTable productSettingTable);
}
