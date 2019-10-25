package com.rxh.service.anew.channel;

import com.rxh.anew.table.channel.ChannelHistoryTable;

public interface ApiChannelHistoryService {

    ChannelHistoryTable getOne(ChannelHistoryTable ch);

    boolean save(ChannelHistoryTable ch);
}
