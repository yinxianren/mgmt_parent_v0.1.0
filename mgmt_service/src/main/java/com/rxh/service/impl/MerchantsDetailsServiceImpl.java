package com.rxh.service.impl;

import com.rxh.mapper.square.MerchantsDetailsMapper;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.square.MerchantsDetailsService;
import com.rxh.square.pojo.MerchantsDetails;
import com.rxh.square.pojo.TerminalMerchantsDetails;
import com.rxh.utils.JsonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
public class MerchantsDetailsServiceImpl implements MerchantsDetailsService {

    @Resource
    private MerchantsDetailsMapper merchantsDetailsMapper;

    @Override
    public PageResult merchantsDetails(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
            paramMap.put("merId",searchInfo.getMerId());
            List<MerchantsDetails> list;
            int totalCount;
            totalCount = merchantsDetailsMapper.countMerchantsDetails(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = merchantsDetailsMapper.selectMerchantsDetails(paramMap);
            System.out.println("==================="+list);
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
