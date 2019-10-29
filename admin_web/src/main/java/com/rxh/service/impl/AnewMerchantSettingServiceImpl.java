package com.rxh.service.impl;

import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.service.AnewMerchantSettingService;
import com.rxh.service.anew.channel.ApiChannelInfoService;
import com.rxh.service.anew.system.ApiMerchantSettingService;
import com.rxh.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Service
public class AnewMerchantSettingServiceImpl implements AnewMerchantSettingService {

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
    public ResponseVO batchUpdate(MerchantSettingTable settingTables) {
        Boolean b = apiMerchantSettingService.batchSaveOrUpdate(null);
        ResponseVO responseVO = new ResponseVO();
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
