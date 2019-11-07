package com.rxh.service.impl;

import com.rxh.anew.table.channel.ChannelDetailsTable;
import com.rxh.anew.table.channel.ChannelWalletTable;
import com.rxh.enums.StatusEnum;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.AnewChannelWalletService;
import com.rxh.service.anew.channel.ApiChannelDetailsService;
import com.rxh.service.anew.channel.ApiChannelWalletService;
import com.rxh.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class AnewChannelWalletServiceImpl implements AnewChannelWalletService {

    @Autowired
    private ApiChannelWalletService apiChannelWalletService;
    @Autowired
    private ApiChannelDetailsService apiChannelDetailsService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseVO search(ChannelWalletTable channelWalletTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setData(apiChannelWalletService.getList(channelWalletTable));
        responseVO.setCode(StatusEnum._0.getStatus());
        return responseVO;
    }

    @Override
    public ResponseVO pageByDetails(Page page) {
        ResponseVO responseVO = new ResponseVO();
        ChannelDetailsTable channelDetailsTable = new ChannelDetailsTable();
        channelDetailsTable.setPageNum(page.getPageNum());
        channelDetailsTable.setPageSize(page.getPageSize());
        SearchInfo searchInfo = page.getSearchInfo();
        if (StringUtils.isNotBlank(searchInfo.getProductId())) channelDetailsTable.setProductId(searchInfo.getProductId());
        if (StringUtils.isNotBlank(searchInfo.getMerOrderId())) channelDetailsTable.setMerOrderId(searchInfo.getMerOrderId());
        if (StringUtils.isNotBlank(searchInfo.getOrderId())) channelDetailsTable.setPlatformOrderId(searchInfo.getOrderId());
        if (StringUtils.isNotBlank(searchInfo.getChannelId())) channelDetailsTable.setChannelId(searchInfo.getChannelId());
        if (null != (searchInfo.getStartDate())) channelDetailsTable.setBeginTime(searchInfo.getStartDate());
        if (null != (searchInfo.getEndDate())) channelDetailsTable.setEndTime(searchInfo.getEndDate());
        if (null != searchInfo.getEndDate()){
            String date = sdf.format(searchInfo.getEndDate());
            date = date + " 23:59:59";
            try {
                channelDetailsTable.setEndTime(sdf2.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setData(apiChannelDetailsService.page(channelDetailsTable));
        return responseVO;
    }
}
