package com.rxh.service;

import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewChannelService {

    ResponseVO getAll(ChannelInfoTable channelInfoTable);

    ResponseVO saveOrUpdate(ChannelInfoTable channelInfoTable);

    ResponseVO delByIds(List<String> ids);

}
