package com.rxh.service;

import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewMerchantSettingService {

    ResponseVO getList(MerchantSettingTable merchantSettingTable);

    ResponseVO batchSave(List<MerchantSettingTable> settingTables);

    ResponseVO batchUpdate(MerchantSettingTable settingTables);

    ResponseVO getChannels(String organizationIds);
}
