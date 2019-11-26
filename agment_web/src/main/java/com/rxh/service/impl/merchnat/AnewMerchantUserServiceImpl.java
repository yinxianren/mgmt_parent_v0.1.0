package com.rxh.service.impl.merchnat;

import com.internal.playment.api.db.merchant.ApiMerchantUserService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantUserTable;
import com.rxh.service.merchant.AnewMerchantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnewMerchantUserServiceImpl implements AnewMerchantUserService {

    @Autowired
    private ApiMerchantUserService apiMerchantUserService;

    @Override
    public ResponseVO saveOrUpdate(MerchantUserTable merchantUserTable) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiMerchantUserService.saveOrUpdate(merchantUserTable);
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }

    @Override
    public ResponseVO delByIds(List<Long> ids) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiMerchantUserService.delByIds(ids);
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }

    @Override
    public ResponseVO getList(MerchantUserTable merchantUserTable) {
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiMerchantUserService.getList(merchantUserTable));
        return responseVO;
    }
}
