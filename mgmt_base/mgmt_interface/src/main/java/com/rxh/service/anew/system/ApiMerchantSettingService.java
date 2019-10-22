package com.rxh.service.anew.system;

import com.rxh.anew.table.system.MerchantSettingTable;

import java.util.List;

public interface ApiMerchantSettingService {

    List<MerchantSettingTable> getList(MerchantSettingTable mst);
}
