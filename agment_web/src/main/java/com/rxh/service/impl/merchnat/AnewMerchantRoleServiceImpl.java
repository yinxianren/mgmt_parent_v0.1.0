package com.rxh.service.impl.merchnat;

import com.internal.playment.api.db.merchant.ApiMerchantRoleService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantRoleTable;
import com.rxh.service.merchant.AnewMerchantRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnewMerchantRoleServiceImpl implements AnewMerchantRoleService {

    @Autowired
    private ApiMerchantRoleService apiMerchantRoleService;

    @Override
    public ResponseVO getList(MerchantRoleTable merchantRoleTable) {
        ResponseVO responseVO = new ResponseVO(StatusEnum._0.getStatus(),StatusEnum._0.getRemark());
        responseVO.setData(apiMerchantRoleService.getList(merchantRoleTable));
        return responseVO;
    }
}
