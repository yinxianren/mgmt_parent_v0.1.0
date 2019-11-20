package com.rxh.service.merchant;

import com.internal.playment.common.table.merchant.MerchantRoleTable;
import com.rxh.vo.ResponseVO;

public interface AnewMerchantRoleService {

    ResponseVO getList(MerchantRoleTable merchantRoleTable);
}
