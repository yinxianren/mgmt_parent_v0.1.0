package com.rxh.service.impl;

import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.AnewMerchantService;
import com.rxh.service.anew.merchant.ApiMerchantInfoService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnewMerchantServiceImpl implements AnewMerchantService, NewPayAssert {

    @Autowired
    private ApiMerchantInfoService apiMerchantInfoService;

    @Override
    public ResponseVO getMerchant(MerchantInfoTable merchantInfoTable) {
        return null;
    }

    @Override
    public ResponseVO delByIds(List<String> ids) {
        return null;
    }

    @Override
    public ResponseVO saveOrUpdate(MerchantInfoTable merchantInfoTable) {
        return null;
    }
}
