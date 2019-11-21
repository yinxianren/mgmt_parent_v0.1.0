package com.rxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.internal.playment.api.db.channel.ApiProductTypeSettingService;
import com.internal.playment.api.db.system.ApiSysConstantService;
import com.internal.playment.common.table.system.ProductSettingTable;
import com.internal.playment.common.table.system.SysConstantTable;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.service.ProductTypeSettingService;
import com.rxh.service.sys.SysConstantService;
import com.rxh.service.system.NewSystemConstantService;
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
    private ApiSysConstantService apiSysConstantService;

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
        SysConstantTable sysConstantTable = new SysConstantTable();
        sysConstantTable.setFirstValue(productSettingTable.getProductId());
        sysConstantTable.setGroupCode(SystemConstant.PRODUCTTYPE);
        List<SysConstantTable> sysConstants = apiSysConstantService.getList(sysConstantTable);
        productSettingTable.setProductName(CollectionUtils.isEmpty(sysConstants)?"":sysConstants.get(0).getName());
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
