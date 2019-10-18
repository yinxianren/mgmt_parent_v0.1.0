package com.rxh.service.impl;

import com.rxh.mapper.square.PayOrderChangeMapper;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.square.PayOrderChangeService;
import com.rxh.square.pojo.PayOrder;
import com.rxh.square.pojo.PayOrderChange;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.UUID;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class PayOrderChangeServiceImpl implements PayOrderChangeService {

    @Resource
    private PayOrderChangeMapper payOrderChangeMapper;
    @Override
    public PageResult findPayOrder(Page page) {
        try {
            int startPage = page.getPageNum()*10;
//            int startPage = 0;
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
//            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
            paramMap.put("customerId",searchInfo.getMerId());
            List<PayOrderChange> list;
            int totalCount;
            //totalCount  sql  方法
            totalCount = payOrderChangeMapper.selectSuccessOrderCountByParam(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = payOrderChangeMapper.selectAllOrder(paramMap);
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
    public boolean insert(PayOrder payOrder) {
            PayOrderChange orderChange = payOrderChangeMapper.selectByPayId(payOrder.getPayId());
            if (orderChange!=null){
                return false;
            }
            PayOrderChange payOrderChange = new PayOrderChange();
            payOrderChange.setExceptionId("C"+UUID.createId());
            payOrderChange.setPayId(payOrder.getPayId());
            payOrderChange.setType(0);
            payOrderChange.setCustomerId(payOrder.getMerId());
            payOrderChange.setRemark(payOrder.getRemark());
            payOrderChange.setChangeStatus(0);
            payOrderChange.setChangeTime(new Date());
            payOrderChange.setChangeAmount(payOrder.getChangeAmount());
            int change = payOrderChangeMapper.insertSelective(payOrderChange);
            if (change==0){
                return false;
            }
            return true;
    }
}
