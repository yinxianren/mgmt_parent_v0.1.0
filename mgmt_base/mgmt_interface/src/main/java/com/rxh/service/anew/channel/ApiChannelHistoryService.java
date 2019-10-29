package com.rxh.service.anew.channel;

import com.rxh.anew.table.channel.ChannelHistoryTable;

import java.util.Collection;

public interface ApiChannelHistoryService {

    ChannelHistoryTable getOne(ChannelHistoryTable ch);

    boolean save(ChannelHistoryTable ch);

    boolean saveOrUpdateBatch(Collection<ChannelHistoryTable> entityList);
}
