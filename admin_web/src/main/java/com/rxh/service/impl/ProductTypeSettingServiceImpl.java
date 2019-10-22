package com.rxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.table.channel.ProductSettingTable;
import com.rxh.service.ProductTypeSettingService;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import com.rxh.utils.SystemConstant;
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
        LambdaQueryWrapper<ProductSettingTable> queryWrapper = new QueryWrapper().lambda();
        queryWrapper.eq(ProductSettingTable::getOrganizationId,id);
        List<ProductSettingTable> list = apiProductTypeSettingService.list(queryWrapper);
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
