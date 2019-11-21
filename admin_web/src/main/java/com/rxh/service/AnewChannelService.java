package com.rxh.service;

import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.page.ResponseVO;

import java.util.List;

public interface AnewChannelService {

    ResponseVO getAll(ChannelInfoTable channelInfoTable);

    ResponseVO saveOrUpdate(ChannelInfoTable channelInfoTable);

    ResponseVO delByIds(List<String> ids);

}
