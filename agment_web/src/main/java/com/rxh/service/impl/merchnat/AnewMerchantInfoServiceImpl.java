package com.rxh.service.impl.merchnat;

import com.internal.playment.api.db.merchant.ApiMerchantInfoService;
import com.internal.playment.api.db.merchant.ApiMerchantPrivilegesService;
import com.internal.playment.api.db.merchant.ApiMerchantRoleService;
import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.merchant.MerchantPrivilegesTable;
import com.internal.playment.common.table.merchant.MerchantRoleTable;
import com.rxh.service.merchant.AnewMerchantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnewMerchantInfoServiceImpl implements AnewMerchantInfoService, NewPayAssert {

    @Autowired
    private ApiMerchantInfoService apiMerchantInfoService;
    @Autowired
    private ApiMerchantPrivilegesService apiMerchantPrivilegesService;
    @Autowired
    private ApiMerchantRoleService apiMerchantRoleService;


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
        if (merchantInfoTable.getId() == null){
            MerchantRoleTable merchantRole = new MerchantRoleTable();
            merchantRole.setBelongto(merchantInfoTable.getMerchantId());
            merchantRole.setRoleName("管理员");
            List<Long> privilegesId = apiMerchantPrivilegesService.getList(null)
                    .stream()
                    .filter(merchantPrivileges -> merchantPrivileges.getParentId() != null)
                    .map(MerchantPrivilegesTable::getId)
                    .collect(Collectors.toList());
            merchantRole.setPrivilegesId(privilegesId.toString().replaceAll("[^,0-9]", ""));
            merchantRole.setRole(SystemConstant.ROLE_MERCHANT_USER);
            merchantRole.setCreateTime(new Date());
            merchantRole.setAvailable(0);
            apiMerchantRoleService.saveOrUpdate(merchantRole);
        }
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }
}
