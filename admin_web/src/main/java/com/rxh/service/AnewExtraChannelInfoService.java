package com.rxh.service;

import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewExtraChannelInfoService {

    public ResponseVO getAll(ChannelExtraInfoTable channelExtraInfoTable);

    public ResponseVO saveOrUpdate(ChannelExtraInfoTable channelExtraInfoTable);

    public ResponseVO removeByIds(List<String> idLists);

}