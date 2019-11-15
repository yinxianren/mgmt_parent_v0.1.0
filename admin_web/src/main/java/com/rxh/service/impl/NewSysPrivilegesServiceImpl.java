package com.rxh.service.impl;

import com.internal.playment.api.db.system.ApiSysPrivilegesService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SysPrivilegesTable;
import com.rxh.service.NewSysPrivilegesService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewSysPrivilegesServiceImpl implements NewSysPrivilegesService {

    @Autowired
    private ApiSysPrivilegesService apiSysPrivilegesService;

    @Override
    public ResponseVO getList(SysPrivilegesTable sysPrivilegesTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
        responseVO.setData(apiSysPrivilegesService.getList(sysPrivilegesTable));
        return responseVO;
    }
}
