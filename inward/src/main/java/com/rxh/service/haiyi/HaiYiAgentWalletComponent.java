package com.rxh.service.haiyi;


import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.square.pojo.AgentMerchantsDetails;
import com.rxh.square.pojo.AgentWallet;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.PayMap;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author  panda
 * @date 20190724
 */
@Component
public class HaiYiAgentWalletComponent implements PayUtil, PayAssert {


    @Autowired
    private PaymentRecordSquareService paymentRecordSquareService;
    @Autowired
    private RedisCacheCommonCompoment redisCacheCommonCompoment;

    /**
     *   获取代理商钱包和明细
     */
    public PayMap<String,Object> handleAgentWalletAndDetails(PayMap<String,Object> map, TransOrder transOrder)throws PayException {
        return getAgentWallet(getAgentMerchantsDetails(map,transOrder),transOrder);
    }

    /**
     *   获取代理商钱包
     */
    public PayMap<String,Object> getAgentWallet(PayMap<String,Object> map,TransOrder transOrder) throws PayException {
        //获取商户信息
//        MerchantInfo merchantInfo = (MerchantInfo) map.get("merchantInfo");
//        if(null == merchantInfo) merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(transOrder.getMerId());
//        isNull(merchantInfo, "【海懿代付】处理队列任务(成功订单)：获取商户信息对象异常！");
//        map.put("merchantInfo",merchantInfo);
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(transOrder.getMerId());
        isNull(merchantInfo, "【海懿代付】处理队列任务(成功订单)：获取商户信息对象异常！");

        //获取代理商的钱包
        AgentWallet agentWallet = paymentRecordSquareService.getAgentMerchantWallet(merchantInfo.getParentId());
        boolean insert=false;
        if (null == agentWallet) {
            insert=true;
            agentWallet = new AgentWallet();
            agentWallet.setAgentMerchantId(merchantInfo.getParentId());
        }
        //计数代理商钱包
        BigDecimal agentTotalBalance = agentWallet.getTotalBalance();
        BigDecimal agentTotalAvailableAmount = agentWallet.getTotalAvailableAmount();
        //代理商总余额
        agentTotalBalance = null == agentTotalBalance ? new BigDecimal(0) : agentTotalBalance;
        agentTotalBalance = agentTotalBalance.add(transOrder.getAgentFee());
        //代理商总可用余额
        agentTotalAvailableAmount = null == agentTotalAvailableAmount ? new BigDecimal(0) : agentTotalAvailableAmount;
        agentTotalAvailableAmount = agentTotalAvailableAmount.add(transOrder.getAgentFee());
        agentWallet.setTotalBalance(agentTotalBalance);
        agentWallet.setTotalAvailableAmount(agentTotalAvailableAmount);
        if(insert) map.put("insertAgentWallet",agentWallet);
        else map.put("updateAgentWallet",agentWallet);
        return map;
    }

    /**
     *   获取代理钱包明细
     */
    public PayMap<String,Object> getAgentMerchantsDetails(PayMap<String,Object> map,TransOrder transOrder) throws PayException {
        BigDecimal amount = transOrder.getAmount();
        //获取商户信息
//        MerchantInfo merchantInfo = (MerchantInfo) map.get("merchantInfo");
//        if(null == merchantInfo) merchantInfo = paymentRecordSquareService.getMerchantInfoByMerId(transOrder.getMerId());
//        isNull(merchantInfo, "【海懿代付】处理队列任务(成功订单)：获取商户信息对象异常！");
//        map.put("merchantInfo",merchantInfo);
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(transOrder.getMerId());
        isNull(merchantInfo, "【海懿代付】处理队列任务(成功订单)：获取商户信息对象异常！");

        AgentWallet  agentWallet = paymentRecordSquareService.getAgentMerchantWallet(merchantInfo.getParentId());
        if (null == agentWallet) {
            agentWallet = new AgentWallet();
            agentWallet.setAgentMerchantId(merchantInfo.getParentId());
        }
        //计算代理商总可用余额
        BigDecimal agentTotalBalanceDetails = agentWallet.getTotalBalance();
        agentTotalBalanceDetails = (null == agentTotalBalanceDetails ? new BigDecimal(0) : agentTotalBalanceDetails);
        agentTotalBalanceDetails = agentTotalBalanceDetails.add(transOrder.getAgentFee());
        // 代理商明细
        AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
        agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details", ""));
        agentMerchantsDetails.setAgentMerId(agentWallet.getAgentMerchantId());
        agentMerchantsDetails.setMerOrderId(transOrder.getMerOrderId());
        agentMerchantsDetails.setOrderId(transOrder.getTransId());
        agentMerchantsDetails.setAmount(amount);
        agentMerchantsDetails.setType("4");//4表示是代付
        agentMerchantsDetails.setInAmount(transOrder.getAgentFee());
        agentMerchantsDetails.setTotalBenifit(agentTotalBalanceDetails);
        agentMerchantsDetails.setUpdateTime(new Date());
        agentMerchantsDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return map.lput("agentMerchantsDetails",agentMerchantsDetails);
    }

}
