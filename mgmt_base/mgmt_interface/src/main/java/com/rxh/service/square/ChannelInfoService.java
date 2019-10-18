package com.rxh.service.square;

import com.rxh.square.pojo.ChannelInfo;

import java.util.List;

public interface ChannelInfoService {

    boolean deleteByPrimaryKey( String[] arrayStr);

    int insert(ChannelInfo record);

    List<ChannelInfo> selectByExample(ChannelInfo record);


    List<ChannelInfo> getAll();

    int updateByPrimaryKeySelective(ChannelInfo record);

    List<ChannelInfo>  selectByGroupOrganizationId();

    int updateOthersInfo(String Others,String channelId);

    ChannelInfo selectByChannelId(String channelId);

    List<ChannelInfo> selectByMerId(String merId);

    List<ChannelInfo> selectByWhereCondition(ChannelInfo record);
}
