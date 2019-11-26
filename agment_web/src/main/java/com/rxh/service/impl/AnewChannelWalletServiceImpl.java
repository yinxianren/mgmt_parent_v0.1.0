package com.rxh.service.impl;

import com.internal.playment.api.db.channel.ApiChannelDetailsService;
import com.internal.playment.api.db.channel.ApiChannelInfoService;
import com.internal.playment.api.db.channel.ApiChannelWalletService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.page.SearchInfo;
import com.internal.playment.common.table.channel.ChannelDetailsTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.channel.ChannelWalletTable;
import com.rxh.service.AnewChannelWalletService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class AnewChannelWalletServiceImpl implements AnewChannelWalletService {

    @Autowired
    private ApiChannelWalletService apiChannelWalletService;
    @Autowired
    private ApiChannelDetailsService apiChannelDetailsService;
    @Autowired
    private ApiChannelInfoService apiChannelInfoService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseVO search(ChannelWalletTable channelWalletTable) {
        ResponseVO responseVO = new ResponseVO();
        List<ChannelWalletTable> list = apiChannelWalletService.getList(channelWalletTable);
        for (ChannelWalletTable channelWalletTable1 : list){
            ChannelInfoTable channelInfoTable = new ChannelInfoTable();
            channelInfoTable.setChannelId(channelWalletTable1.getChannelId());
            channelInfoTable = apiChannelInfoService.getOne(channelInfoTable);
            channelWalletTable1.setProductId(channelInfoTable.getProductId());
        }
        responseVO.setData(list);
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
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
