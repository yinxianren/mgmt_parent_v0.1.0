package com.rxh.service.impl.merchnat;

import com.internal.playment.api.db.merchant.ApiMerchantAcountService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.merchant.MerchantAcountTable;
import com.rxh.service.merchant.NewMerchantAcountService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewMerchantAcountServiceImpl implements NewMerchantAcountService {

    @Autowired
    private ApiMerchantAcountService apimerchantAcountService;

    @Override
    public ResponseVO getOne(MerchantAcountTable merchantAcountTable) {
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apimerchantAcountService.getOne(merchantAcountTable));
        return responseVO;
    }

    @Override
    public ResponseVO savaOrUpdate(MerchantAcountTable merchantAcountTable) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apimerchantAcountService.saveOrUpdate(merchantAcountTable);
        if (b) {
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }
}
