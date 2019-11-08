package com.rxh.service.impl;

import com.internal.playment.api.db.channel.ApiChannelInfoService;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.rxh.service.AnewChannelService;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnewChannelServiceImpl implements AnewChannelService {

    @Autowired
    private ApiChannelInfoService apiChannelInfoService;

    @Override
    public ResponseVO getAll(ChannelInfoTable channelInfoTable) {
        List<ChannelInfoTable> list = apiChannelInfoService.getList(channelInfoTable);
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode((int)SystemConstant.BANK_STATUS_SUCCESS);
        responseVO.setData(list);
        return responseVO;
    }

    @Override
    public ResponseVO saveOrUpdate(ChannelInfoTable channelInfoTable) {
        Boolean b = apiChannelInfoService.saveOrUpdate(channelInfoTable);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode((int)SystemConstant.BANK_STATUS_SUCCESS);
        }else{
            responseVO.setCode((int)SystemConstant.BANK_STATUS_FAIL);
        }
        return responseVO;
    }

    @Override
    public ResponseVO delByIds(List<String> ids) {
        Boolean b = apiChannelInfoService.delByIds(ids);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setCode((int)SystemConstant.BANK_STATUS_SUCCESS);
        }else{
            responseVO.setCode((int)SystemConstant.BANK_STATUS_FAIL);
        }
        return responseVO;
    }
}
