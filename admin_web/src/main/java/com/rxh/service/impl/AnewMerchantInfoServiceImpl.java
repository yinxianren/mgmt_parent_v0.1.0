package com.rxh.service.impl;

import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.AnewMerchantInfoService;
import com.rxh.service.anew.merchant.ApiMerchantInfoService;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnewMerchantInfoServiceImpl implements AnewMerchantInfoService, NewPayAssert {

    @Autowired
    private ApiMerchantInfoService apiMerchantInfoService;

    @Override
    public ResponseVO getMerchants(MerchantInfoTable merchantInfoTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode((int)SystemConstant.BANK_STATUS_SUCCESS);
        responseVO.setData(apiMerchantInfoService.getMerchants(merchantInfoTable));
        return responseVO;
    }

    @Override
    public ResponseVO delByIds(List<String> ids) {
        ResponseVO responseVO = new ResponseVO();
        Boolean b = apiMerchantInfoService.delByIds(ids);
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO saveOrUpdate(MerchantInfoTable merchantInfoTable) {
        ResponseVO responseVO = new ResponseVO();
        Boolean b = apiMerchantInfoService.saveOrUpdate(merchantInfoTable);
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }
}
