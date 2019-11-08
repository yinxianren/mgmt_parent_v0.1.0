package com.rxh.service.impl;

import com.internal.playment.api.db.channel.ApiChannelExtraInfoService;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.rxh.service.AnewExtraChannelInfoService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnewExtraChannelInfoServiceImpl implements AnewExtraChannelInfoService {

    @Autowired
    private ApiChannelExtraInfoService apiChannelExtraInfoService;
    @Override
    public ResponseVO getAll(ChannelExtraInfoTable channelExtraInfoTable) {
        List<ChannelExtraInfoTable> list = apiChannelExtraInfoService.list(channelExtraInfoTable);
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(0);
        responseVO.setData(list);
        return responseVO;
    }

    @Override
    public ResponseVO saveOrUpdate(ChannelExtraInfoTable channelExtraInfoTable) {
        Boolean b = apiChannelExtraInfoService.saveOrUpdate(channelExtraInfoTable);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }

    @Override
    public ResponseVO removeByIds(List<String> idLists) {
        Boolean b = apiChannelExtraInfoService.removeByIds(idLists);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode(0);
        }else {
            responseVO.setCode(1);
        }
        return responseVO;
    }
}
