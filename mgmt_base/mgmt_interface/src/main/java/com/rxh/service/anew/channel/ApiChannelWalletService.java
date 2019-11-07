package com.rxh.service.anew.channel;

import com.rxh.anew.table.channel.ChannelWalletTable;

import java.util.List;

public interface ApiChannelWalletService {

    ChannelWalletTable getOne(ChannelWalletTable cwt);

    boolean updateOrSave(ChannelWalletTable cwt);

    List<ChannelWalletTable> getList(ChannelWalletTable cwt);
}
