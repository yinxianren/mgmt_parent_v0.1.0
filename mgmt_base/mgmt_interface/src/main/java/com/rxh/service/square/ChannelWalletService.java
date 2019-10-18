package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.ChannelWallet;

import java.util.List;
import java.util.Map;

public interface ChannelWalletService {


     List<ChannelWallet> search(ChannelWallet channelWallet);


     List<ChannelInfo> getIdsAndName();

     Result deleteByPrimaryKey(List<String> channelIds);


     PageResult findChannelWallteDetails(Page page);

     ChannelWallet getChannelWallet(String channelId);

     int updateByPrimaryKeySelective(ChannelWallet record);
}
