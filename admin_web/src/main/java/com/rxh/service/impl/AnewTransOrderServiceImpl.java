package com.rxh.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.api.db.business.ApiTransOrderInfoService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.AnewChannelService;
import com.rxh.service.AnewTransOrderService;
import com.rxh.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnewTransOrderServiceImpl implements AnewTransOrderService {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ApiTransOrderInfoService apiTransOrderInfoService;
    @Autowired
    private AnewChannelService anewChannelService;

    @Override
    public ResponseVO page(Page page) {
        ResponseVO responseVO = new ResponseVO();
        try {
            TransOrderInfoTable transOrderInfoTable = new TransOrderInfoTable();
            transOrderInfoTable.setPageNum(page.getPageNum());
            transOrderInfoTable.setPageSize(page.getPageSize());
            SearchInfo searchInfo = page.getSearchInfo();
            if (StringUtils.isNotBlank(searchInfo.getMerId())) transOrderInfoTable.setMerchantId(searchInfo.getMerId());
            if (StringUtils.isNotBlank(searchInfo.getPayId())) transOrderInfoTable.setPlatformOrderId(searchInfo.getPayId());
//            if (StringUtils.isNotEmpty(searchInfo.getOrganizationId())) payOrderInfoTable.setChannelId(searchInfo.getExpressName());
            if (null != (searchInfo.getOrderStatus())) transOrderInfoTable.setStatus(searchInfo.getOrderStatus());
            if (null != (searchInfo.getSettleStatus())) transOrderInfoTable.setSettleStatus(searchInfo.getSettleStatus());
            if (StringUtils.isNotBlank(searchInfo.getProductId())) transOrderInfoTable.setProductId(searchInfo.getProductId());
            if (null != (searchInfo.getStartDate())) transOrderInfoTable.setBeginTime(sdf2.format(searchInfo.getStartDate()));
            if (null != searchInfo.getEndDate()){
                String date = sdf.format(searchInfo.getEndDate());
                date = date + " 23:59:59";
                transOrderInfoTable.setEndTime(date);
            }
            IPage<TransOrderInfoTable> ipage = apiTransOrderInfoService.page(transOrderInfoTable);
            List<ChannelInfoTable> channelInfoTables = (List<ChannelInfoTable>)anewChannelService.getAll(null).getData();
            Map<String,Object> organMap = new HashMap<>();
            for (ChannelInfoTable channelInfoTable : channelInfoTables ){
                organMap.put(channelInfoTable.getChannelId(),channelInfoTable.getOrganizationId());
            }
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalChannelFee = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            for (TransOrderInfoTable transOrderInfoTable1:ipage.getRecords()){
                totalMoney = totalMoney.add(transOrderInfoTable1.getAmount());
                totalFee = totalFee.add(transOrderInfoTable1.getAmount().subtract(transOrderInfoTable1.getOutAmount()));
                totalChannelFee = totalChannelFee.add(transOrderInfoTable1.getChannelFee());
                transOrderInfoTable1.setOrganizationId(organMap.get(transOrderInfoTable1.getChannelId()) != null ? organMap.get(transOrderInfoTable1.getChannelId()).toString():null);
            }
            Map map = new HashMap();
            map.put("totalMoney",totalMoney.toString());
            map.put("totalFee",totalFee.toString());
            map.put("totalChannelFee",totalChannelFee.toString());
            // 返回结果
            responseVO.setData(ipage);
            responseVO.setCustomData(map);
            return responseVO;
        } catch (Exception e) {
            e.printStackTrace();
            responseVO.setCode(StatusEnum._1.getStatus());
            return responseVO;
        }
    }
}
