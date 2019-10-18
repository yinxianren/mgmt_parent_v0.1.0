package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.square.pojo.ExtraChannelInfo;

import java.util.List;

public interface ExtraChannelInfoService {


    Result insert(ExtraChannelInfo record);

    int insertSelective(ExtraChannelInfo record);

    Result update(ExtraChannelInfo record);

    int delete(List<String> ids);

    ExtraChannelInfo search(ExtraChannelInfo record);

    List<ExtraChannelInfo> select(ExtraChannelInfo record);

    List<ExtraChannelInfo> getAll();

    boolean deleteByPrimaryKey(List<String> ids);

    List<ExtraChannelInfo> getChannelInfosByPayType(Integer bizType);

}
