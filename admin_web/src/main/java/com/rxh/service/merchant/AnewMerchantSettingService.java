package com.rxh.service.merchant;

import com.internal.playment.common.table.system.MerchantSettingTable;
import com.rxh.vo.ResponseVO;

import java.util.List;
import java.util.Map;

public interface AnewMerchantSettingService {

    ResponseVO getList(MerchantSettingTable merchantSettingTable);

    ResponseVO batchSave(List<MerchantSettingTable> settingTables);

    ResponseVO batchUpdate(Map<String,Object> map);

    ResponseVO getChannels(String organizationIds);
}
