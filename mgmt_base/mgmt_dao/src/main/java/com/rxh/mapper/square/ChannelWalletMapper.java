package com.rxh.mapper.square;

import com.rxh.square.pojo.ChannelWallet;
import com.rxh.square.pojo.ChannelWalletExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChannelWalletMapper {
    int countByExample(ChannelWalletExample example);

    int deleteByExample(ChannelWalletExample example);

    int deleteByPrimaryKey(String id);

    int insert(ChannelWallet record);

    int insertSelective(ChannelWallet record);

    List<ChannelWallet> selectByExample(ChannelWalletExample example);

    ChannelWallet selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ChannelWallet record, @Param("example") ChannelWalletExample example);

    int updateByExample(@Param("record") ChannelWallet record, @Param("example") ChannelWalletExample example);

    int updateByPrimaryKeySelective(ChannelWallet record);

    int updateByPrimaryKey(ChannelWallet record);

    List<ChannelWallet> selectAllChannel();

    List<ChannelWallet> getWalletByParam(ChannelWallet channelWallet);

    ChannelWallet getChannelWallet(String channelId);

    List<ChannelWallet> getChannelWalletByIds(@Param("channelIds")List<String> channelIds);
}