package com.rxh.service.impl;

import com.internal.playment.api.db.system.ApiBankRateService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.BankRateTable;
import com.rxh.service.AnewBankRateService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnewBankRateServiceImpl implements AnewBankRateService {

    @Autowired
    private ApiBankRateService apiBankRateService;

    @Override
    public ResponseVO search(BankRateTable bankRateTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
        responseVO.setData(apiBankRateService.getList(bankRateTable));
        return responseVO;
    }

    @Override
    public ResponseVO saveOrUpdate(BankRateTable bankRateTable) {
        Boolean b = apiBankRateService.saveOrUpdate(bankRateTable);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
        }
        return responseVO;
    }

    @Override
    public ResponseVO removeByIds(List<String> idList) {
        Boolean b = apiBankRateService.batchDelByIds(idList);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
        }
        return responseVO;
    }
}
