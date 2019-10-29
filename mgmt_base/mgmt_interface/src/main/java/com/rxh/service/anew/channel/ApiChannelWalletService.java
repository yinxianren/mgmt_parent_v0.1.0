package com.rxh.service.anew.channel;

import com.rxh.anew.table.channel.ChannelWalletTable;

public interface ApiChannelWalletService {

    ChannelWalletTable getOne(ChannelWalletTable cwt);

    boolean updateOrSave(ChannelWalletTable cwt);
}
