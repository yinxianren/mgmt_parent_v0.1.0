package com.rxh.service.anew.channel;

import com.rxh.anew.table.channel.ChannelInfoTable;

import java.util.List;
import java.util.Set;

public interface ApiChannelInfoService {

    List<ChannelInfoTable>  batchGetByChannelId(Set<String> channelIdSet);

    ChannelInfoTable getOne(ChannelInfoTable cit);

    List<ChannelInfoTable> getChannel(ChannelInfoTable cit);

    Boolean savaOrUpdate(ChannelInfoTable cit);

    Boolean delByIds(List<String> ids);

    List<ChannelInfoTable> getChannels(List<String> orgIds);
}
