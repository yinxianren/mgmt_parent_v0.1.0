package com.rxh.service.nonVerifyKuaijie;

import com.rxh.square.pojo.ChannelInfo;

import java.util.List;

public interface INonVerifyKuaiJieService {

    /**
     *  获取最低费率通道策略
      * @param list
     * @return
     */
  default ChannelInfo  lowestReteChannelStrategy(List<ChannelInfo> list){
      return list.stream()
      .distinct()
      .sorted((t1,t2)->
              t1.getChannelRateFee().compareTo(t2.getChannelRateFee())
      )
      .findFirst()
      .orElse(null);
  }



}
