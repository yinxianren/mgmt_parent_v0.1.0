package com.rxh.service.square;


import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.SystemOrderTrack;

import java.util.List;
import java.util.Map;

public interface SystemOrderTrackService {

    PageResult findSystemOrder(Page page);

    List<SystemOrderTrack> getIds();

    SystemOrderTrack findOneByMerOrderId(String orderId);

    List<SystemOrderTrack> selectAllSystemOrder(Map<String, Object> paramMap);

    SystemOrderTrack findOneById(String id);
}
