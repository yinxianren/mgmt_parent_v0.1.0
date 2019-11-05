package com.rxh.service.impl;

import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.enums.StatusEnum;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.AnewMerchantQuotaRiskService;
import com.rxh.service.anew.merchant.ApiMerchantQuotaRiskService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AnewMerchantQuotaRiskServiceImpl implements AnewMerchantQuotaRiskService, NewPayAssert {

    @Autowired
    private ApiMerchantQuotaRiskService apiMerchantQuotaRiskService;
    @Override
    public ResponseVO search(MerchantQuotaRiskTable merchantId) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setData(apiMerchantQuotaRiskService.getOne(merchantId));
        return responseVO;
    }

    @Override
    public ResponseVO saveOrUpdate(MerchantQuotaRiskTable merchantQuotaRiskTable) {
        ResponseVO responseVO = new ResponseVO();
        if (isNull(merchantQuotaRiskTable)) {
            responseVO.setCode(StatusEnum._1.getStatus());
            return responseVO;
        }
        Boolean b = true;
        if (merchantQuotaRiskTable.getId() == null){
            merchantQuotaRiskTable.setCreateTime(new Date());
            merchantQuotaRiskTable.setUpdateTime(new Date());
            b = apiMerchantQuotaRiskService.save(merchantQuotaRiskTable);
        }else {
            merchantQuotaRiskTable.setUpdateTime(new Date());
            b = apiMerchantQuotaRiskService.updateByPrimaryKey(merchantQuotaRiskTable);
        }
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            return responseVO;
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            return responseVO;
        }
    }
}
