package com.rxh.service;

import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.page.ResponseVO;

import java.util.List;

public interface AnewExtraChannelInfoService {

    public ResponseVO getAll(ChannelExtraInfoTable channelExtraInfoTable);

    public ResponseVO saveOrUpdate(ChannelExtraInfoTable channelExtraInfoTable);

    public ResponseVO removeByIds(List<String> idLists);

}
