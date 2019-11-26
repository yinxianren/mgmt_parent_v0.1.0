package com.rxh.service.impl.system;

import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.db.system.ApiSysConstantService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SysConstantTable;
import com.internal.playment.common.page.Page;
import com.rxh.service.system.NewConstantService;
import com.internal.playment.common.page.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class NewConstantServiceImpl implements NewConstantService {

    @Autowired
    private ApiSysConstantService apiSysConstantService;

    @Override
    public ResponseVO saveOrUpdate(SysConstantTable sysConstantTable) {
        ResponseVO responseVO = new ResponseVO();
        if (StringUtils.isBlank(sysConstantTable.getId())){
            sysConstantTable.setId(null);
            sysConstantTable.setCreateTime(new Date());
        }
        sysConstantTable.setUpdateTime(new Date());
        boolean b = apiSysConstantService.saveOrUpdate(sysConstantTable);
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setMessage(StatusEnum._1.getRemark());
            responseVO.setCode(StatusEnum._1.getStatus());
        }
        return responseVO;
    }

    @Override
    public ResponseVO delByIds(String ids) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(ids)){
            list = Arrays.asList(ids.split(","));
        }
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiSysConstantService.delByIds(list);
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setMessage(StatusEnum._1.getRemark());
            responseVO.setCode(StatusEnum._1.getStatus());
        }
        return responseVO;
    }

    @Override
    public ResponseVO page(Page page) {
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        SysConstantTable sysConstantTable = new SysConstantTable();
        String json = JSONObject.toJSONString(page.getObject());
        if (StringUtils.isNotBlank(json)) sysConstantTable = JSONObject.parseObject(json,SysConstantTable.class);
        PageDTO pageDTO = new PageDTO(page.getPageNum(),page.getPageSize());
        responseVO.setData(apiSysConstantService.page(sysConstantTable,pageDTO));
        return responseVO;
    }

    @Override
    public ResponseVO getList(SysConstantTable sysConstantTable) {
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiSysConstantService.getList(sysConstantTable));
        return responseVO;
    }
}
