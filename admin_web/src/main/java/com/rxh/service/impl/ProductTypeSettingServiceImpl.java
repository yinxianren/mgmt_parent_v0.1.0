package com.rxh.service.impl;

import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.service.ProductTypeSettingService;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductTypeSettingServiceImpl implements ProductTypeSettingService {

    @Autowired
    private ApiProductTypeSettingService apiProductTypeSettingService;

    @Override
    public ResponseVO selectByOrganizationId(String id) {
        ProductSettingTable productSettingTable = new ProductSettingTable();
        productSettingTable.setOrganizationId(id);
        List<ProductSettingTable> list = apiProductTypeSettingService.list(productSettingTable);
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(0);
        responseVO.setMessage("成功");
        responseVO.setData(list);
        return responseVO;
    }

    @Override
    public ResponseVO addProduct(ProductSettingTable productSettingTable) {
        ResponseVO responseVO = new ResponseVO();
        Boolean b = apiProductTypeSettingService.SaveOrUpdate(productSettingTable);
        if (b){
            responseVO.setCode(0);
            responseVO.setMessage("成功");
        }else {
            responseVO.setCode(1);
            responseVO.setMessage("失败");
        }
        return responseVO;
    }
}
