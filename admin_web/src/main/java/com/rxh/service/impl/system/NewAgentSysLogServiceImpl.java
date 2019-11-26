package com.rxh.service.impl.system;

import com.internal.playment.api.db.system.ApiAgentSysLogService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.SearchInfo;
import com.internal.playment.common.table.system.AgentSysLogTable;
import com.internal.playment.common.page.Page;
import com.rxh.service.system.NewAgentSysLogService;
import com.internal.playment.common.page.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class NewAgentSysLogServiceImpl implements NewAgentSysLogService {

    @Autowired
    private ApiAgentSysLogService apiAgentSysLogService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseVO page(Page page) {
        AgentSysLogTable agentSysLogTable = new AgentSysLogTable();
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
        responseVO.setData(apiAgentSysLogService.page(agentSysLogTable,pageDTO));
        return responseVO;
    }
}
