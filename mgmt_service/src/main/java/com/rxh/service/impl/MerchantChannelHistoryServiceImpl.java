package com.rxh.service.impl;

import com.rxh.mapper.square.MerchantChannelHistoryMapper;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.square.MerchantChannelHistoryService;
import com.rxh.square.pojo.MerchantChannelHistory;
import com.rxh.utils.JsonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : MerchantChannelHistoryServiceImpl
 * @Author : zoe
 * @Date : 2019/6/17 19:53
 */
@Service
public class MerchantChannelHistoryServiceImpl implements MerchantChannelHistoryService {

    @Resource
    private MerchantChannelHistoryMapper merchantChannelHistoryMapper;
    @Override
    public PageResult merchantChannel(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
            paramMap.put("merId",searchInfo.getMerId());
            List<MerchantChannelHistory> list;
            int totalCount;
            totalCount = merchantChannelHistoryMapper.merchantChannelHistoryCount(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = merchantChannelHistoryMapper.merchantChannelHistory(paramMap);
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
}
