package com.rxh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.api.db.business.ApiPayOrderInfoService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.AnewChannelService;
import com.rxh.service.AnewPayOrderService;
import com.rxh.service.ConstantService;
import com.rxh.square.pojo.PayCardholderInfo;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
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
    private ConstantService constantService;

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
            if (null != (searchInfo.getStartDate())) payOrderInfoTable.setBeginTime(searchInfo.getStartDate());
            if (null != searchInfo.getEndDate()){
                String date = sdf.format(searchInfo.getEndDate());
                date = date + " 23:59:59";
                payOrderInfoTable.setEndTime(sdf2.parse(date));
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
            SysConstant idtype = null;
            SysConstant banktype = null;
            SysConstant productType = null;
            if (idMap.get(payOrder.getIdentityType().toString())!=null){
                idtype = (SysConstant)idMap.get(payOrder.getIdentityType().toString());
            }
            if (bankMap.get(payOrder.getBankCardType().toString())!=null){
                banktype = (SysConstant)bankMap.get(payOrder.getBankCardType().toString());
            }
            if (productMap.get(payOrder.getProductId())!=null){
                productType = (SysConstant)productMap.get(payOrder.getProductId());
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
