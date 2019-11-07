package com.rxh.service.impl;

import com.rxh.anew.table.agent.AgentMerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.enums.StatusEnum;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.AnewMerchantWalletService;
import com.rxh.service.anew.agent.ApiAgentMerchantInfoService;
import com.rxh.service.anew.channel.ApiChannelDetailsService;
import com.rxh.service.anew.merchant.ApiMerchantInfoService;
import com.rxh.service.anew.merchant.ApiMerchantWalletService;
import com.rxh.service.anew.merchant.ApiMerchantsDetailsService;
import com.rxh.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnewMerchantWalletServiceImpl implements AnewMerchantWalletService {

    @Autowired
    private ApiMerchantWalletService apiMerchantWalletService;
    @Autowired
    private ApiMerchantsDetailsService apiMerchantsDetailsService;
    @Autowired
    private ApiMerchantInfoService apiMerchantInfoService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseVO search(MerchantWalletTable merchantWalletTable) {
        ResponseVO responseVO = new ResponseVO();

        //代理商查询，获取代理商所属商户
        if (StringUtils.isNotBlank(merchantWalletTable.getAgentMerchantId())){
            MerchantInfoTable merchantInfoTable = new MerchantInfoTable();
            merchantInfoTable.setAgentMerchantId(merchantWalletTable.getAgentMerchantId());
            List<MerchantInfoTable> list = apiMerchantInfoService.getMerchants(merchantInfoTable);
            List<String> lists = new ArrayList<>();
            for (MerchantInfoTable merchantInfoTable1 : list){
                lists.add(merchantInfoTable1.getMerchantId());
            }
            merchantWalletTable.setMerchantIds(lists);
        }

        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setData(apiMerchantWalletService.getList(merchantWalletTable));
        responseVO.setMessage(StatusEnum._0.getRemark());
        return responseVO;
    }

    @Override
    public ResponseVO pageByDetails(Page page) {
        ResponseVO responseVO = new ResponseVO();
        MerchantsDetailsTable merchantsDetailsTable = new MerchantsDetailsTable();
        merchantsDetailsTable.setPageNum(page.getPageNum());
        merchantsDetailsTable.setPageSize(page.getPageSize());
        SearchInfo searchInfo = page.getSearchInfo();
        if (StringUtils.isNotBlank(searchInfo.getProductId())) merchantsDetailsTable.setProductId(searchInfo.getProductId());
        if (StringUtils.isNotBlank(searchInfo.getMerOrderId())) merchantsDetailsTable.setMerOrderId(searchInfo.getMerOrderId());
        if (StringUtils.isNotBlank(searchInfo.getOrderId())) merchantsDetailsTable.setPlatformOrderId(searchInfo.getOrderId());
        if (StringUtils.isNotBlank(searchInfo.getMerId())) merchantsDetailsTable.setMerchantId(searchInfo.getMerId());
        if (null != (searchInfo.getStartDate())) merchantsDetailsTable.setBeginTime(searchInfo.getStartDate());
        if (null != (searchInfo.getEndDate())) merchantsDetailsTable.setEndTime(searchInfo.getEndDate());
        if (null != searchInfo.getEndDate()){
            String date = sdf.format(searchInfo.getEndDate());
            date = date + " 23:59:59";
            try {
                merchantsDetailsTable.setEndTime(sdf2.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setData(apiMerchantsDetailsService.page(merchantsDetailsTable));
        responseVO.setMessage(StatusEnum._0.getRemark());
        return responseVO;
    }
}