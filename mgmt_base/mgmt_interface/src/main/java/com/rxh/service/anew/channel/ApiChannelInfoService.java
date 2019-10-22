package com.rxh.service.anew.channel;

import com.rxh.anew.table.channel.ChannelInfoTable;

import java.util.List;
import java.util.Set;

public interface ApiChannelInfoService {

    List<ChannelInfoTable>  batchGetByChannelId(Set<String> channelIdSet);

}
