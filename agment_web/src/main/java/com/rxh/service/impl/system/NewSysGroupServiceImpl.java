package com.rxh.service.impl.system;

import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.db.system.ApiSysGroupService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.system.SysGroupTable;
import com.rxh.service.system.NewSysGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NewSysGroupServiceImpl implements NewSysGroupService {

    @Autowired
    private ApiSysGroupService apiSysGroupService;

    @Override
    public ResponseVO saveOrUpdate(SysGroupTable sysGroupTable) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiSysGroupService.saveOrUpdate(sysGroupTable);
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }

    @Override
    public ResponseVO getList(SysGroupTable sysGroupTable) {
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiSysGroupService.getList(sysGroupTable));
        return responseVO;
    }

    @Override
    public ResponseVO batchByIds(String ids) {
        ResponseVO responseVO = new ResponseVO();
        List<String> idList = new ArrayList<>();
        List<Long> idLs = new ArrayList<>();
        if (StringUtils.isNotBlank(ids)){
            idList = Arrays.asList(ids.split(","));
            for (String id : idList){
                idLs.add(Long.valueOf(id));
            }
        }
        boolean b = apiSysGroupService.delByIds(idLs);
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
        PageDTO pageDTO = new PageDTO(page.getPageNum(),page.getPageSize());
        SysGroupTable sysGroupTable = new SysGroupTable();
        String json = JSONObject.toJSONString(page.getObject());
        if (StringUtils.isNotBlank(json)) sysGroupTable = JSONObject.parseObject(json,SysGroupTable.class);
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
        responseVO.setData(apiSysGroupService.page(sysGroupTable,pageDTO));
        return responseVO;
    }

    @Override
    public ResponseVO delByCodes(String codes) {
        List<String> codeList = new ArrayList<>();
        if (StringUtils.isNotBlank(codes)) codeList = Arrays.asList(codes.split(","));
        boolean b = apiSysGroupService.delByCodes(codeList);
        if (b) return new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        else return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
    }
}
