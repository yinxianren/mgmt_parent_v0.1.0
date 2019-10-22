package com.rxh.service;

import com.rxh.anew.table.channel.ProductSettingTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface ProductTypeSettingService {

    ResponseVO selectByOrganizationId(String id);

    ResponseVO addProduct(ProductSettingTable productSettingTable);
}
