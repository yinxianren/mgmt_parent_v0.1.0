package com.rxh.service;

import com.rxh.anew.table.channel.ChannelWalletTable;
import com.rxh.pojo.base.Page;
import com.rxh.vo.ResponseVO;

public interface AnewChannelWalletService {

    ResponseVO search(ChannelWalletTable channelWalletTable);

    ResponseVO pageByDetails(Page page);
}
