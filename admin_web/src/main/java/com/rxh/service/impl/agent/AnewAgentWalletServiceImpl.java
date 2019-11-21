package com.rxh.service.impl.agent;

import com.internal.playment.api.db.agent.ApiAgentMerchantWalletService;
import com.internal.playment.api.db.agent.ApiAgentMerchantsDetailsService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.agent.AgentMerchantWalletTable;
import com.internal.playment.common.table.agent.AgentMerchantsDetailsTable;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.SearchInfo;
import com.rxh.service.agent.AnewAgentWalletService;
import com.internal.playment.common.page.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;


@Service
public class AnewAgentWalletServiceImpl implements AnewAgentWalletService {

    @Autowired
    private ApiAgentMerchantWalletService apiAgentMerchantWalletService;
    @Autowired
    private ApiAgentMerchantsDetailsService apiAgentMerchantsDetailsService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseVO search(AgentMerchantWalletTable agentMerchantInfoTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setData(apiAgentMerchantWalletService.getList(agentMerchantInfoTable));
        return responseVO;
    }

    @Override
    public ResponseVO pageByDetails(Page page) {
        ResponseVO responseVO = new ResponseVO();
        AgentMerchantsDetailsTable agentMerchantsDetailsTable = new AgentMerchantsDetailsTable();
        agentMerchantsDetailsTable.setPageNum(page.getPageNum());
        agentMerchantsDetailsTable.setPageSize(page.getPageSize());
        SearchInfo searchInfo = page.getSearchInfo();
        if (StringUtils.isNotBlank(searchInfo.getProductId())) agentMerchantsDetailsTable.setProductId(searchInfo.getProductId());
        if (StringUtils.isNotBlank(searchInfo.getMerOrderId())) agentMerchantsDetailsTable.setMerOrderId(searchInfo.getMerOrderId());
        if (StringUtils.isNotBlank(searchInfo.getOrderId())) agentMerchantsDetailsTable.setPlatformOrderId(searchInfo.getOrderId());
        if (StringUtils.isNotBlank(searchInfo.getAgentMerId())) agentMerchantsDetailsTable.setAgentMerchantId(searchInfo.getAgentMerId());
        if (null != (searchInfo.getStartDate())) agentMerchantsDetailsTable.setBeginTime(searchInfo.getStartDate());
        if (null != (searchInfo.getEndDate())) agentMerchantsDetailsTable.setEndTime(searchInfo.getEndDate());
        if (null != searchInfo.getEndDate()){
            String date = sdf.format(searchInfo.getEndDate());
            date = date + " 23:59:59";
            try {
                agentMerchantsDetailsTable.setEndTime(sdf2.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        responseVO.setData(apiAgentMerchantsDetailsService.page(agentMerchantsDetailsTable));
        return responseVO;
    }
}
