package com.rxh.service.impl;

import com.rxh.mapper.square.SystemOrderTrackMapper;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.square.SystemOrderTrackService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SystemOrderTrackServiceImpl implements SystemOrderTrackService {

    @Resource
    private SystemOrderTrackMapper systemOrderTrackMapper;
    @Override
    public PageResult findSystemOrder(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
            paramMap.put("merId",searchInfo.getMerId());
            List<SystemOrderTrack> list;
            int totalCount;
            totalCount = systemOrderTrackMapper.selectSuccessOrderCountByParam(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = systemOrderTrackMapper.selectAllSystemOrder(paramMap);
            PageResult pageResult = new PageResult();
            pageResult.setRows(list);
            pageResult.setTotal(totalCount);
            pageResult.setAllPage(allPage);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SystemOrderTrack> getIds() {
        return systemOrderTrackMapper.getIds();
    }

    @Override
    public SystemOrderTrack findOneByMerOrderId(String orderId) {
        if (StringUtils.isBlank(orderId)){
            return null;
        }
        return systemOrderTrackMapper.findOneByMerOrderId(orderId);
    }

    @Override
    public List<SystemOrderTrack> selectAllSystemOrder(Map<String, Object> paramMap) {
        return systemOrderTrackMapper.selectAllSystemOrder(paramMap);
    }

    @Override
    public SystemOrderTrack findOneById(String id) {
        return systemOrderTrackMapper.getSystemOrderTrack(id);
    }
}
