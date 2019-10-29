package com.rxh.service.anew.channel;

import com.rxh.anew.table.channel.ChannelDetailsTable;

public interface ApiChannelDetailsService {

    ChannelDetailsTable  getOne(ChannelDetailsTable cdt);

    boolean updateOrSave(ChannelDetailsTable cdt);
}
