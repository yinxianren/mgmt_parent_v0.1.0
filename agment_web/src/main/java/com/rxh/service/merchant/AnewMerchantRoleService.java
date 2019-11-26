package com.rxh.service.merchant;

import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantRoleTable;

public interface AnewMerchantRoleService {

    ResponseVO getList(MerchantRoleTable merchantRoleTable);
}
