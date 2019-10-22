package com.rxh.service.anew.channel;

import com.rxh.anew.table.channel.ChannelExtraInfoTable;

import java.util.List;

public interface ApiChannelExtraInfoService {

    ChannelExtraInfoTable  getOne(ChannelExtraInfoTable cei);

    List<ChannelExtraInfoTable> lsit(ChannelExtraInfoTable cei);
}
