package com.rxh.service.impl.trading;

import com.rxh.mapper.square.TransAuditMapper;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.trading.TransAuditService;
import com.rxh.square.pojo.TransAudit;
import com.rxh.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName : TransOrderServiceImpl
 * @Author : zoe
 * @Date : 2019/5/19 16:01
 */
@Service
public class TransAuditServiceImpl implements TransAuditService {

    @Autowired
    private TransAuditMapper transAuditMapper;

    @Override
    public PageResult findTransAudit(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
    //        int startPage =0;
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
    //        Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("startPage", startPage);
            paramMap.put("pageSize", pageSize);
            List<TransAudit> list;
            //totalCount  sql  方法
            int totalCount = transAuditMapper.selectSuccessOrderCountByParam(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = transAuditMapper.selectOrderByParamMap(paramMap);
           // PageInfo<TransOrderDto> pageInfo = new PageInfo<>(list);
            PageResult pageResult = new PageResult();
            pageResult.setRows(list);
            pageResult.setTotal(totalCount);
            pageResult.setAllPage(allPage);
            // 返回结果
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TransAudit findTransAuditByTransId(String transId) {
        return transAuditMapper.findTransAuditByTransId(transId);
    }

}

