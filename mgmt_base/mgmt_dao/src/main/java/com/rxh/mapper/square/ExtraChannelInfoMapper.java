package com.rxh.mapper.square;

import com.rxh.square.pojo.ExtraChannelInfo;
import com.rxh.square.pojo.ExtraChannelInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtraChannelInfoMapper {

    int countByExample(ExtraChannelInfoExample example);

    int deleteByExample(ExtraChannelInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(ExtraChannelInfo record);

    int insertSelective(ExtraChannelInfo record);

    List<ExtraChannelInfo> selectByExample(ExtraChannelInfoExample example);

    ExtraChannelInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ExtraChannelInfo record, @Param("example") ExtraChannelInfoExample example);

    int updateByExample(@Param("record") ExtraChannelInfo record, @Param("example") ExtraChannelInfoExample example);

    int updateByPrimaryKeySelective(ExtraChannelInfo record);

    int updateByPrimaryKey(ExtraChannelInfo record);


    void delete(String id);

    int update(@Param("record") ExtraChannelInfo record);

    List<ExtraChannelInfo> getChannelInfosByPayType(@Param("type")Integer type);

    ExtraChannelInfo searchExtraChannelInfo(@Param("channelId") String channelId,@Param("type")String type);

    ExtraChannelInfo search(ExtraChannelInfo record);

    List<ExtraChannelInfo> select(ExtraChannelInfo record);

    String getMaxExtraChannelId();
}
