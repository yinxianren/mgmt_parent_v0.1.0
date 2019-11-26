package com.rxh.service.impl.system;

import com.internal.playment.api.db.system.ApiSysLogService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.page.SearchInfo;
import com.internal.playment.common.table.system.SysLogTable;
import com.rxh.service.system.NewSystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class NewSystemLogServiceImpl implements NewSystemLogService {

    @Autowired
    private ApiSysLogService apiSysLogService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseVO saveOrUpdate(SysLogTable sysLogTable) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiSysLogService.saveOrUpdate(sysLogTable);
        if(b){
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
        SysLogTable sysLogTable = new SysLogTable();
        PageDTO pageDTO = new PageDTO(page.getPageNum(),page.getPageSize());
        SearchInfo searchInfo = page.getSearchInfo();
        if (searchInfo != null){
            if (null != (searchInfo.getStartDate())) pageDTO.setBeginTime(sdf2.format(searchInfo.getStartDate()));
            if (null != searchInfo.getEndDate()){
                String date = sdf.format(searchInfo.getEndDate());
                date = date + " 23:59:59";
                pageDTO.setEndTime(date);
            }
        }
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiSysLogService.page(sysLogTable,pageDTO));
        return responseVO;
    }
}
