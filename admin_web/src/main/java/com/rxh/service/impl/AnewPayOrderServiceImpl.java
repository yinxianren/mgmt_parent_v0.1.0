package com.rxh.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.api.db.business.ApiPayOrderInfoService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.PayCardholderInfo;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.system.SysConstantTable;
import com.internal.playment.common.page.Page;
import com.internal.playment.common.page.SearchInfo;
import com.rxh.service.AnewChannelService;
import com.rxh.service.AnewPayOrderService;
import com.rxh.service.system.NewSystemConstantService;
import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnewPayOrderServiceImpl implements AnewPayOrderService {

    @Autowired
    private ApiPayOrderInfoService apiPayOrderInfoService;
    @Autowired
    private AnewChannelService anewChannelService;
    @Autowired
    private NewSystemConstantService constantService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public ResponseVO getList(Page page) {
        ResponseVO responseVO = new ResponseVO();
        try {
            PayOrderInfoTable payOrderInfoTable = new PayOrderInfoTable();
            payOrderInfoTable.setPageNum(page.getPageNum());
            payOrderInfoTable.setPageSize(page.getPageSize());
            SearchInfo searchInfo = page.getSearchInfo();
            if (StringUtils.isNotBlank(searchInfo.getMerId())) payOrderInfoTable.setMerchantId(searchInfo.getMerId());
            if (StringUtils.isNotBlank(searchInfo.getPayId())) payOrderInfoTable.setPlatformOrderId(searchInfo.getPayId());
//            if (StringUtils.isNotEmpty(searchInfo.getOrganizationId())) payOrderInfoTable.setChannelId(searchInfo.getExpressName());
            if (null != (searchInfo.getOrderStatus())) payOrderInfoTable.setStatus(searchInfo.getOrderStatus());
            if (null != (searchInfo.getSettleStatus())) payOrderInfoTable.setSettleStatus(searchInfo.getSettleStatus());
            if (StringUtils.isNotBlank(searchInfo.getProductId())) payOrderInfoTable.setProductId(searchInfo.getProductId());
            if (null != (searchInfo.getStartDate())) payOrderInfoTable.setBeginTime(sdf2.format(searchInfo.getStartDate()));
            if (null != searchInfo.getEndDate()){
                String date = sdf.format(searchInfo.getEndDate());
                date = date + " 23:59:59";
                payOrderInfoTable.setEndTime(date);
            }
            IPage<PayOrderInfoTable> ipage = apiPayOrderInfoService.page(payOrderInfoTable);
            List<ChannelInfoTable> channelInfoTables = (List<ChannelInfoTable>)anewChannelService.getAll(null).getData();
            Map<String,Object> organMap = new HashMap<>();
            for (ChannelInfoTable channelInfoTable : channelInfoTables ){
                organMap.put(channelInfoTable.getChannelId(),channelInfoTable.getOrganizationId());
            }
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalChannelFee = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            for (PayOrderInfoTable payOrder:ipage.getRecords()){
                totalMoney = totalMoney.add(payOrder.getAmount());
                totalFee = totalFee.add(payOrder.getAmount().subtract(payOrder.getInAmount()));
                totalChannelFee = totalChannelFee.add(payOrder.getChannelFee());
                payOrder.setOrganizationId(organMap.get(payOrder.getChannelId()) != null ? organMap.get(payOrder.getChannelId()).toString():null);
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

    @Override
    public ResponseVO getCardHolderInfo(String payId) {
        ResponseVO responseVO = new ResponseVO();
        PayOrderInfoTable payOrderInfoTable = new PayOrderInfoTable();
        payOrderInfoTable.setPlatformOrderId(payId);
        PayOrderInfoTable payOrder = apiPayOrderInfoService.getOne(payOrderInfoTable);
        if(payOrder!=null){
            ChannelInfoTable channelInfo = new ChannelInfoTable();
            channelInfo.setChannelId(payOrder.getChannelId());
            List<ChannelInfoTable> channelInfos = (List<ChannelInfoTable>)anewChannelService.getAll(channelInfo).getData();
            if (!CollectionUtils.isEmpty(channelInfos)){
                channelInfo = channelInfos.get(0);
            }
            PayCardholderInfo cardholderInfo = new PayCardholderInfo();
            Map idMap = constantService.getConstantsMapByGroupName(SystemConstant.IDENTITYTYPE);
            Map bankMap = constantService.getConstantsMapByGroupName(SystemConstant.BANKCARDTYPE);
            Map productMap = constantService.getConstantsMapByGroupName(SystemConstant.PRODUCTTYPE);
            SysConstantTable idtype = null;
            SysConstantTable banktype = null;
            SysConstantTable productType = null;
            if (idMap.get(payOrder.getIdentityType().toString())!=null){
                idtype = (SysConstantTable)idMap.get(payOrder.getIdentityType().toString());
            }
            if (bankMap.get(payOrder.getBankCardType().toString())!=null){
                banktype = (SysConstantTable)bankMap.get(payOrder.getBankCardType().toString());
            }
            if (productMap.get(payOrder.getProductId())!=null){
                productType = (SysConstantTable)productMap.get(payOrder.getProductId());
            }
            //证件类型
            cardholderInfo.setIdentityNum(idtype!=null?idtype.getName():"");
            //卡类型
            cardholderInfo.setBankcardNum(banktype!=null?banktype.getName():"");
            //产品名称
            cardholderInfo.setProductName(productType!=null?productType.getName():"");
            cardholderInfo.setChannelName(channelInfo == null ? "" : channelInfo.getChannelName());
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage("获取持卡人详情成功");
            responseVO.setData(cardholderInfo);
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("获取持卡人详情失败");
        }
        return responseVO;
    }
}
