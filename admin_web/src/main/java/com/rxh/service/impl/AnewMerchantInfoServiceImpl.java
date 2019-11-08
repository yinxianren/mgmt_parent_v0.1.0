package com.rxh.service.impl;

import com.internal.playment.api.db.merchant.ApiMerchantInfoService;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.pojo.merchant.MerchantPrivileges;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.service.AnewMerchantInfoService;
import com.rxh.service.merchant.MerchantPrivilegesService;
import com.rxh.service.merchant.MerchantRoleService;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import com.rxh.vo.ResponseVO;
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
    private MerchantPrivilegesService merchantPrivilegesService;
    @Autowired
    private MerchantRoleService merchantRoleService;

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
            MerchantRole merchantRole = new MerchantRole();
            merchantRole.setBelongto(merchantInfoTable.getMerchantId());
            merchantRole.setRoleName("管理员");
            List<Long> privilegesId = merchantPrivilegesService.getAll()
                    .stream()
                    .filter(merchantPrivileges -> merchantPrivileges.getParentId() != null)
                    .map(MerchantPrivileges::getId)
                    .collect(Collectors.toList());
            merchantRole.setPrivilegesId(privilegesId.toString().replaceAll("[^,0-9]", ""));
            Long roleid = UUID.createKey("merchant_role");
            merchantRole.setId(roleid);
            merchantRole.setRole(SystemConstant.ROLE_MERCHANT_USER);
            merchantRole.setCreateTime(new Date());
            merchantRole.setAvailable(true);
            merchantRoleService.save(merchantRole);
        }
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }
}
