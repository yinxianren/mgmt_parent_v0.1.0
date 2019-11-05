package com.rxh.service.impl.trading;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.agent.AgentMerchantInfoDbService;
import com.rxh.anew.service.db.business.PayOrderInfoDBService;
import com.rxh.anew.service.db.channel.ChannelInfoDbService;
import com.rxh.anew.table.agent.AgentMerchantInfoTable;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.mapper.square.*;
import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.pojo.base.SearchInfo;
import com.rxh.service.trading.PayOrderService;
import com.rxh.square.pojo.*;
import com.rxh.square.vo.PayOrderDto;
import com.rxh.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : PayOrderServiceImpl
 * @Author : zoe
 * @Date : 2019/5/19 13:38
 */

@Service
public class PayOrderServiceImpl implements PayOrderService {

    @Autowired
    private PayOrderMapper payOrderMapper;
    @Autowired
    private PayProductDetailMapper payProductDetailMapper;
    @Autowired
    private PayCardholderInfoMapper payCardholderInfoMapper;
    @Autowired
    private PayOrderInfoDBService payOrderInfoDBService;
    @Autowired
    private ChannelInfoDbService channelInfoDbService;

    @Override
    public PageResult findPayOrder(Page page) {
        try {
            int startPage = page.getPageNum()*page.getPageSize();
//            int startPage = 0;
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
//            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
            paramMap.put("merId",searchInfo.getMerId());
            List<PayOrder> list;
            int totalCount = 0;
            BigDecimal totalMoney = new BigDecimal(0);
            BigDecimal totalChannelFee = new BigDecimal(0);
            BigDecimal totalFee = new BigDecimal(0);
            //totalCount  sql  方法
            List<PayOrder> payOrderList = payOrderMapper.selectSuccessOrderByParam(paramMap);
            if (!CollectionUtils.isEmpty(payOrderList)){
                totalCount = payOrderList.size();
            }
            for (PayOrder payOrder:payOrderList){
                totalMoney = totalMoney.add(payOrder.getAmount());
                totalFee = totalFee.add(payOrder.getFee());
                totalChannelFee = totalChannelFee.add(payOrder.getChannelFee());
            }
            Map map = new HashMap();
            map.put("totalMoney",totalMoney.toString());
            map.put("totalFee",totalFee.toString());
            map.put("totalChannelFee",totalChannelFee.toString());
//            totalCount = payOrderMapper.selectSuccessOrderCountByParam(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = payOrderMapper.selectAllOrder(paramMap);
            PageResult pageResult = new PageResult();
            pageResult.setRows(list);
            pageResult.setTotal(totalCount);
            pageResult.setAllPage(allPage);
            pageResult.setCustomize(map);
            // 返回结果
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PageResult search(Page page) {
       try {
            int startPage = (page.getPageNum() - 1) * 10;
            int pageSize = page.getPageSize();
            SearchInfo searchInfo = page.getSearchInfo();
            Map<String, Object> paramMap = JsonUtils.objectToMap(searchInfo);
            paramMap.put("startPage",startPage);
            paramMap.put("pageSize", pageSize);
            paramMap.put("merId",searchInfo.getMerId());
            paramMap.put("merOrderId",searchInfo.getMerOrderId());
            paramMap.put("payId",searchInfo.getId());
            paramMap.put("channelId",searchInfo);
            paramMap.put("orderStatus",searchInfo);
            paramMap.put("organizationId",searchInfo);
            paramMap.put("clearStatus",searchInfo);
            paramMap.put("tradeTime",searchInfo);
            List<PayOrderDto> list;
            int totalCount;
            //totalCount  sql  方法
            totalCount = payOrderMapper.selectSuccessOrderCountByParam(paramMap);
            int allPage = (totalCount + pageSize - 1) / pageSize;
            list = payOrderMapper.getPayOrderBySerach(paramMap);
            // PageInfo<PayOrderDto> pageInfo = new PageInfo<>(list);
            PageResult pageResult = new PageResult();
            pageResult.setRows(list);
            pageResult.setTotal(totalCount);
            pageResult.setAllPage(allPage);
            // 返回结果
            return pageResult;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Result getCardHolderInfo(String payId) {
        PayCardholderInfo cardholderInfo = payCardholderInfoMapper.selectByPrimaryKey(payId);
        LambdaQueryWrapper<PayOrderInfoTable> queryWrapper = new QueryWrapper<PayOrderInfoTable>().lambda();
        queryWrapper.eq(PayOrderInfoTable::getPlatformOrderId,payId);
        PayOrderInfoTable payOrder = payOrderInfoDBService.getOne(queryWrapper);
        Result <PayCardholderInfo> result=new Result<>();
        if(cardholderInfo!=null){
            ChannelInfoTable channelInfo = channelInfoDbService.getOne(new QueryWrapper<ChannelInfoTable>().lambda().eq(ChannelInfoTable::getChannelId,payOrder.getChannelId()));
            cardholderInfo.setChannelBankResult(payOrder.getChannelRespResult());
            cardholderInfo.setChannelBankTime(payOrder.getUpdateTime());
            cardholderInfo.setOrgOrderId(payOrder.getChannelOrderId());
            cardholderInfo.setCurrency(payOrder.getCurrency());
            cardholderInfo.setTerminalMerId(payOrder.getTerminalMerId());
            cardholderInfo.setOrderStatus(payOrder.getStatus());
            cardholderInfo.setChannelName(channelInfo == null ? "" : channelInfo.getChannelName());
            result.setCode(Result.SUCCESS);
            result.setMsg("获取持卡人详情成功");
            result.setData(cardholderInfo);
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("获取持卡人详情失败");
        }
        return result;
    }

    @Override
    public Result getProductInfo(String payId) {
        PayProductDetail payProductDetail=payProductDetailMapper.getProductInfo(payId);
        Result <PayProductDetail> result=new Result<>();
        if(payProductDetail!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("获取交易商品详情成功");
            result.setData(payProductDetail);
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("获取交易商品详情失败");
        }
        return result;
    }

    @Override
    public PayOrder selectByPayId(String payId) {
        return payOrderMapper.selectByPrimaryKey(payId);
    }

    @Override
    public List<PayOrder> getPayOrderByWhereCondition(PayOrder record) {
        return payOrderMapper.getPayOrderByWhereCondition(record);
    }

    @Override
    public int updateByPrimaryKey(PayOrder payOrder) {
        return payOrderMapper.updateByPrimaryKey(payOrder);
    }
    @Override
    public int updateByPrimaryKeySelective(PayOrder payOrder){
        return payOrderMapper.updateByPrimaryKeySelective(payOrder);
    }

    @Override
    public int insertBean(PayOrder payOrder) {
        return payOrderMapper.insertSelective(payOrder);
    }

    @Override
    public PayOrder seleteBymerOrderId(String merId, String merOrderId ,String terminalMerId) {
        return payOrderMapper.selectPayOrderByMerOrderIdAndMerIdAndTerMerId(merOrderId,merId,terminalMerId);
    }

    @Override
    public List<PayOrder> selectByOrders(Map<String,Object> paramMap) {
        return payOrderMapper.selectByOrders(paramMap);
    }

    @Override
    public List<PayOrder> selectByOrderStatusAndSettleStatus(String orderStatus,String settleStatus) {
        return payOrderMapper.selectByOrderStatusAndSettleStatus(orderStatus,orderStatus);
    }
}
