package com.rxh.service.anew.business;

import com.rxh.anew.table.business.MerchantCardTable;

import java.util.List;
import java.util.Set;

public interface ApiMerchantCardService {

    MerchantCardTable getOne(MerchantCardTable mct);

    List<MerchantCardTable>  getList(MerchantCardTable mct);

    List<MerchantCardTable> getListByPlatformOrderId(Set<String> platformOrderIds, MerchantCardTable mct);

    boolean updateById(MerchantCardTable mct);

    boolean save(MerchantCardTable mct);
}
