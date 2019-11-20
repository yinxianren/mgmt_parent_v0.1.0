package com.rxh.service.merchant;

import com.internal.playment.common.table.merchant.MerchantUserTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewMerchantUserService {

    ResponseVO saveOrUpdate(MerchantUserTable merchantUserTable);

    ResponseVO delByIds(List<Long> ids);

    ResponseVO getList(MerchantUserTable merchantUserTable);
}
