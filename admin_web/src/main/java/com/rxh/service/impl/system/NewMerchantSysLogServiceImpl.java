package com.rxh.service.impl.system;

import com.internal.playment.api.db.system.ApiMerchantSysLogService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.MerchantSysLogTable;
import com.rxh.pojo.base.Page;
import com.rxh.service.system.NewMerchantSysLogService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewMerchantSysLogServiceImpl implements NewMerchantSysLogService {

    @Autowired
    private ApiMerchantSysLogService apiMerchantSysLogService;

    @Override
    public ResponseVO page(Page page) {
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        MerchantSysLogTable merchantSysLogTable = new MerchantSysLogTable();
        PageDTO pageDTO = new PageDTO(page.getPageNum(),page.getPageSize());
        responseVO.setData(apiMerchantSysLogService.page(merchantSysLogTable,pageDTO));
        return responseVO;
    }
}
