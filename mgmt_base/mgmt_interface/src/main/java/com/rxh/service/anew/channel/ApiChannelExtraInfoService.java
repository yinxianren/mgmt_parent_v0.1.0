package com.rxh.service.anew.channel;

import com.rxh.anew.table.channel.ChannelExtraInfoTable;

import java.util.List;

public interface ApiChannelExtraInfoService {

    ChannelExtraInfoTable  getOne(ChannelExtraInfoTable cei);

    List<ChannelExtraInfoTable> list(ChannelExtraInfoTable cei);

    Boolean saveOrUpdate(ChannelExtraInfoTable channelExtraInfoTable);

    Boolean removeByIds(List<ChannelExtraInfoTable> extraInfoTables);
}
