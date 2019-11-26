package com.rxh.service.impl.merchnat;

import com.internal.playment.api.db.channel.ApiChannelInfoService;
import com.internal.playment.api.db.system.ApiMerchantSettingService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.rxh.service.merchant.AnewMerchantSettingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnewMerchantSettingServiceImpl implements AnewMerchantSettingService, NewPayAssert {

    @Autowired
    private ApiMerchantSettingService apiMerchantSettingService;
    @Autowired
    private ApiChannelInfoService apiChannelInfoService;

    @Override
    public ResponseVO getList(MerchantSettingTable merchantSettingTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setData(apiMerchantSettingService.getList(merchantSettingTable));
        responseVO.setCode(0);
        return responseVO;
    }

    @Override
    public ResponseVO batchSave(List<MerchantSettingTable> settingTables) {
        Boolean b = apiMerchantSettingService.batchSaveOrUpdate(settingTables);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO batchUpdate(Map<String,Object> map) {
        ResponseVO responseVO = new ResponseVO();
        List<Map> channels = isNull(map.get("channelId"))?new ArrayList<>():(List)map.get("channelId");
        String merchantId = isNull(map.get("merchantId"))?"":map.get("merchantId").toString();
        List<MerchantSettingTable> list = new ArrayList<>();
        MerchantSettingTable merchantSettingTable = new MerchantSettingTable();
        merchantSettingTable.setMerchantId(merchantId);
        Boolean rb  = apiMerchantSettingService.remove(merchantSettingTable);
//        if (!rb){
//            responseVO.setCode(1);
//            return responseVO;
//        }
        for (Map map1 : channels){
            merchantSettingTable = new MerchantSettingTable();
            merchantSettingTable.setChannelId(map1.get("channelId").toString());
            merchantSettingTable.setCreateTime(new Date());
            merchantSettingTable.setOrganizationId(map1.get("organizationId").toString());
            merchantSettingTable.setMerchantId(merchantId);
            merchantSettingTable.setStatus(StatusEnum._0.getStatus());
            merchantSettingTable.setProductId(map1.get("productId").toString());
            merchantSettingTable.setUpdateTime(new Date());
            list.add(merchantSettingTable);
        }
        Boolean b = apiMerchantSettingService.batchSaveOrUpdate(list);
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO getChannels(String organizationIds) {
        List<String> organizations = new ArrayList<>();
        if (StringUtils.isNotEmpty(organizationIds)){
            organizations = Arrays.asList(organizationIds.split(","));
        }
        StringJoiner joiner = new StringJoiner("\",\"");
        for (String organizationId : organizations){
            joiner.add(organizationId);
        }
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(0);
        responseVO.setData(apiChannelInfoService.getChannels(organizations));
        return responseVO;
    }
}
