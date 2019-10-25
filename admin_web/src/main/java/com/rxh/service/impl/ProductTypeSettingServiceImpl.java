package com.rxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.service.ProductTypeSettingService;
import com.rxh.service.anew.channel.ApiProductTypeSettingService;
import com.rxh.service.sys.SysConstantService;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
@Service
public class ProductTypeSettingServiceImpl implements ProductTypeSettingService {

    @Autowired
    private ApiProductTypeSettingService apiProductTypeSettingService;
    @Autowired
    private SysConstantService sysConstantService;

    @Override
    public ResponseVO selectByOrganizationId(ProductSettingTable productSettingTable) {
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
        List<ProductSettingTable> list = apiProductTypeSettingService.list(productSettingTable);
        Date date = new Date();
        if (CollectionUtils.isEmpty(list)){
            productSettingTable.setCreateTime(date);
        }else {
            productSettingTable.setId(list.get(0).getId());
        }
        SysConstant sysConstant = sysConstantService.getOneByFirstValueAndCode(productSettingTable.getProductId(), SystemConstant.PRODUCTTYPE);
        productSettingTable.setProductName(sysConstant == null?"":sysConstant.getName());
        productSettingTable.setUpdateTime(date);
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
