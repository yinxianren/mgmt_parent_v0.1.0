package com.rxh.service.merchant;

import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.merchant.MerchantUserTable;

import java.util.List;

public interface AnewMerchantUserService {

    ResponseVO saveOrUpdate(MerchantUserTable merchantUserTable);

    ResponseVO delByIds(List<Long> ids);

    ResponseVO getList(MerchantUserTable merchantUserTable);
}
