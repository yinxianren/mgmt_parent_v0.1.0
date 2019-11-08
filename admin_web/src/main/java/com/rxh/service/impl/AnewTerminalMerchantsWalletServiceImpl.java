package com.rxh.service.impl;

import com.internal.playment.api.db.terminal.ApiTerminalMerchantsDetailsService;
import com.internal.playment.api.db.terminal.ApiTerminalMerchantsWalletService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.terminal.TerminalMerchantsDetailsTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.AnewTerminalMerchantsWalletService;
import com.rxh.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class AnewTerminalMerchantsWalletServiceImpl implements AnewTerminalMerchantsWalletService {

    @Autowired
    private ApiTerminalMerchantsWalletService apiTerminalMerchantsWalletService;
    @Autowired
    private ApiTerminalMerchantsDetailsService apiTerminalMerchantsDetailsService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseVO search(TerminalMerchantsWalletTable terminalMerchantsWalletTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
        responseVO.setData(apiTerminalMerchantsWalletService.getList(terminalMerchantsWalletTable));
        return responseVO;
    }

    @Override
    public ResponseVO pageByDetail(Page page) {
        ResponseVO responseVO = new ResponseVO();
        TerminalMerchantsDetailsTable terminalMerchantsDetailsTable = new TerminalMerchantsDetailsTable();
        SearchInfo searchInfo = page.getSearchInfo();
        if (StringUtils.isNotBlank(searchInfo.getProductId())) terminalMerchantsDetailsTable.setProductId(searchInfo.getProductId());
        if (StringUtils.isNotBlank(searchInfo.getMerOrderId())) terminalMerchantsDetailsTable.setMerOrderId(searchInfo.getMerOrderId());
        if (StringUtils.isNotBlank(searchInfo.getOrderId())) terminalMerchantsDetailsTable.setPlatformOrderId(searchInfo.getOrderId());
        if (StringUtils.isNotBlank(searchInfo.getMerId())) terminalMerchantsDetailsTable.setMerchantId(searchInfo.getMerId());
        if (StringUtils.isNotBlank(searchInfo.getTerminalMerId())) terminalMerchantsDetailsTable.setTerminalMerId(searchInfo.getTerminalMerId());
        if (null != (searchInfo.getStartDate())) terminalMerchantsDetailsTable.setBeginTime(searchInfo.getStartDate());
        if (null != (searchInfo.getEndDate())) terminalMerchantsDetailsTable.setEndTime(searchInfo.getEndDate());
        if (null != searchInfo.getEndDate()){
            String date = sdf.format(searchInfo.getEndDate());
            date = date + " 23:59:59";
            try {
                terminalMerchantsDetailsTable.setEndTime(sdf2.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        terminalMerchantsDetailsTable.setPageNum(page.getPageNum());
        terminalMerchantsDetailsTable.setPageSize(page.getPageSize());
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
        responseVO.setData(apiTerminalMerchantsDetailsService.page(terminalMerchantsDetailsTable));
        return responseVO;
    }

    @Override
    public List<TerminalMerchantsDetailsTable> listByTerminalId() {
        return apiTerminalMerchantsDetailsService.listGroupByTerminalId();
    }
}
