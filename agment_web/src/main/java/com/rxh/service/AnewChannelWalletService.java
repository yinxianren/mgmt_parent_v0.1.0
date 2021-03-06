package com.rxh.service;

import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.channel.ChannelWalletTable;

public interface AnewChannelWalletService {

    ResponseVO search(ChannelWalletTable channelWalletTable);

    ResponseVO pageByDetails(Page page);
}
