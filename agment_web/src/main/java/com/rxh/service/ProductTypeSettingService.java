package com.rxh.service;

import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.system.ProductSettingTable;

public interface ProductTypeSettingService {

    ResponseVO selectByOrganizationId(ProductSettingTable productSettingTable);

    ResponseVO addProduct(ProductSettingTable productSettingTable);
}
