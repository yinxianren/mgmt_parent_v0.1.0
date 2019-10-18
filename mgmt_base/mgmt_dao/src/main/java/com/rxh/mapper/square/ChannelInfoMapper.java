package com.rxh.mapper.square;

import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.ChannelInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ChannelInfoMapper {

    int countByExample(ChannelInfoExample example);

    int deleteByExample(ChannelInfoExample example);

    int deleteByPrimaryKey(String channelId);

    int insert(ChannelInfo record);

    int insertSelective(ChannelInfo record);

    List<ChannelInfo> selectByExample(ChannelInfoExample example);

    ChannelInfo selectByPrimaryKey(String channelId);

    int updateByPrimaryKeySelective(ChannelInfo record);

    int updateByPrimaryKey(ChannelInfo record);

    List<ChannelInfo> selectAllIdAndName();

    String getIdIncre();

    String getMaxId();

    List<ChannelInfo> getChannelByPayType(@Param("paramMap") Map<String,Object> map);

    List<ChannelInfo> getChannelById(@Param("ids") List<String> ids);

    List<ChannelInfo> getType(@Param("ids") List<String> ids);

    List<ChannelInfo>  selectByGroupOrganizationId();

    Integer countByOrgId(String id);

    int updateOthersInfo(@Param("others")String others, @Param("channelId")String channelId);

    ChannelInfo selectByChannelId(@Param("channelId") String channelId);

    List<ChannelInfo> selectByWhereCondition(ChannelInfo record);
}