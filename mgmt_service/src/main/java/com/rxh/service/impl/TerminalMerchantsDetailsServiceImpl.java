package com.rxh.service.impl;

import com.rxh.mapper.square.TerminalMerchantsDetailsMapper;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.square.TerminalMerchantsDetailsService;
import com.rxh.square.pojo.TerminalMerchantsDetails;
import com.rxh.utils.JsonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
public class TerminalMerchantsDetailsServiceImpl implements TerminalMerchantsDetailsService {

    @Resource
    private TerminalMerchantsDetailsMapper terminalMerchantsDetailsMapper;
    @Override
    public PageResult terminalMerchantsDetails(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
            System.out.println(searchInfo);
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
            paramMap.put("merId",searchInfo.getMerId());
            List<TerminalMerchantsDetails> list;
            int totalCount;
            totalCount = terminalMerchantsDetailsMapper.countTerminalMerchantsDetails(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = terminalMerchantsDetailsMapper.selectTerminalMerchantsDetails(paramMap);
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
    public List<TerminalMerchantsDetails> getIds() {
        return terminalMerchantsDetailsMapper.getIds();
    }
}
