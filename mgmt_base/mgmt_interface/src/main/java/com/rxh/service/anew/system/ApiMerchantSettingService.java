package com.rxh.service.anew.system;

import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;

import java.util.List;

public interface ApiMerchantSettingService {

    List<MerchantSettingTable> getList(MerchantSettingTable mst);

    Boolean batchSaveOrUpdate(List<MerchantSettingTable> merchantSettingTables);

}
