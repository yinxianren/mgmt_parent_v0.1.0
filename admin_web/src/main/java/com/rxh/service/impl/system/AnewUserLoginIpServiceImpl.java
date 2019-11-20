package com.rxh.service.impl.system;

import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.db.system.ApiUserLoginIpService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.UserLoginIpTable;
import com.rxh.pojo.base.Page;
import com.rxh.service.AnewUserLoginIpService;
import com.rxh.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnewUserLoginIpServiceImpl implements AnewUserLoginIpService {

    @Autowired
    private ApiUserLoginIpService apiUserLoginIpService;

    @Override
    public ResponseVO saveOrUpdate(UserLoginIpTable userLoginIpTable) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiUserLoginIpService.saveOrUpdate(userLoginIpTable);
        if (b) {
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }

    @Override
    public ResponseVO delByIds(List<Long> ids) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiUserLoginIpService.delByIds(ids);
        if (b) {
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }

    @Override
    public ResponseVO page(Page page) {
        PageDTO pageDTO = new PageDTO(page.getPageNum(),page.getPageSize());
        String json = JSONObject.toJSONString(page.getObject());
        UserLoginIpTable userLoginIpTable = new UserLoginIpTable();
        if (StringUtils.isNotBlank(json) && !json.equals("null")) userLoginIpTable = JSONObject.parseObject(json,UserLoginIpTable.class);
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiUserLoginIpService.page(userLoginIpTable,pageDTO));
        return responseVO;
    }
}
