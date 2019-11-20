package com.rxh.service.impl.system;

import com.internal.playment.api.db.system.ApiSysLogService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SysLogTable;
import com.rxh.pojo.base.Page;
import com.rxh.service.system.NewSystemLogService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewSystemLogServiceImpl implements NewSystemLogService {

    @Autowired
    private ApiSysLogService apiSysLogService;

    @Override
    public ResponseVO page(Page page) {
        SysLogTable sysLogTable = new SysLogTable();
        PageDTO pageDTO = new PageDTO(page.getPageNum(),page.getPageSize());
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiSysLogService.page(sysLogTable,pageDTO));
        return responseVO;
    }
}
