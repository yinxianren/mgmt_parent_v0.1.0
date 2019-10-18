package com.rxh.wallet;


import com.rxh.exception.PayException;
import com.rxh.square.pojo.AgentMerchantSetting;
import com.rxh.square.pojo.AgentMerchantsDetails;
import com.rxh.square.pojo.AgentWallet;
import com.rxh.square.pojo.MerchantRate;
import com.rxh.utils.PayMap;
import com.rxh.utils.UUID;
import com.rxh.vo.OrderObjectToMQ;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：代理商钱包业务
 * @author  panda
 * @date 20190721
 *
 *
 */
@Component
public class AgentWalletComponet  extends AbstractWalletComponent{




    /**
     *   支付代理商钱包
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public PayMap<String,Object> getAgentWalletObject(OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap) throws PayException {

        String merId=orderObjectToMQ.getMerId();
        BigDecimal amount = orderObjectToMQ.getAmount();
        if (orderObjectToMQ.getPayType().equals("6")){
            amount = new BigDecimal(0).subtract(amount);
        }
        String parentId=orderObjectToMQ.getParentId();
        String payType= orderObjectToMQ.getChannelType().toString();
        BigDecimal hundred = new BigDecimal(100);


        //代理商配置
        AgentMerchantSetting agentMerchantSetting = redisCacheCommonCompoment.agentMerchantSettingCahce.getOne(parentId,payType);
        if(null ==agentMerchantSetting ) agentMerchantSetting= new  AgentMerchantSetting();


        BigDecimal agentRateFee = agentMerchantSetting.getRateFee();
        agentRateFee= ( null == agentRateFee ? new BigDecimal(0) : agentRateFee);
        BigDecimal agenetFee = amount.divide(hundred).multiply(agentRateFee).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal singlefee = agentMerchantSetting.getSingleFee() != null ? agentMerchantSetting.getSingleFee() : new BigDecimal(0);
        if (orderObjectToMQ.getPayType().equals("6")){
            singlefee = new BigDecimal(0).subtract(singlefee);
        }
        agenetFee=agenetFee.add(singlefee);

        //获取代理商钱包
        AgentWallet agentWallet = paymentRecordSquareService.getAgentMerchantWallet(parentId);
        boolean isInsert=false;
        if ( null == agentWallet ) {
            agentWallet = new AgentWallet();
            agentWallet.setAgentMerchantId(parentId);
            isInsert=true;
        }
        //订单总额
        agentWallet.setTotalAmount(agentWallet.getTotalAmount() == null ?
                amount : agentWallet.getTotalAmount().add(amount).setScale(2, BigDecimal.ROUND_UP));
        //入账总额
        agentWallet.setIncomeAmount(agentWallet.getIncomeAmount() == null ?
                agenetFee : agentWallet.getIncomeAmount().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));
        //总余额
        agentWallet.setTotalBalance(agentWallet.getTotalBalance() == null ?
                agenetFee : agentWallet.getTotalBalance().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));

        if (agentMerchantSetting.getSettlecycle().equalsIgnoreCase("D0") || agentMerchantSetting.getSettlecycle().equalsIgnoreCase("T0")) {
            //总可用 原始表总额 + 订单金额*手续费
            agentWallet.setTotalAvailableAmount(agentWallet.getTotalAvailableAmount() == null ?
                    agenetFee : agentWallet.getTotalAvailableAmount().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));
        } else {
            //总不可 原始表总不可用+ 订单金额*手续费
            agentWallet.setTotalUnavailableAmount(agentWallet.getTotalUnavailableAmount() == null ?
                    agenetFee : agentWallet.getTotalUnavailableAmount().add(agenetFee).setScale(2, BigDecimal.ROUND_UP));
        }
        agentWallet.setUpdateTime(new Date());

        if(isInsert) payMap.put("insertAgentWallet",agentWallet);
        else payMap.put("updateAgentWallet",agentWallet);

        return payMap;
    }

    /**
     *   支付代理商明细
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public  PayMap<String,Object> getAgentMerchantsDetailsObject(OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap) {

        //获取交易金额
        BigDecimal amount = orderObjectToMQ.getAmount();
        if (orderObjectToMQ.getPayType().equals("6")){
            amount = new BigDecimal(0).subtract(amount);
        }
        String parentId=orderObjectToMQ.getParentId();
        String payType= orderObjectToMQ.getChannelType().toString();
        //代理商配置
        AgentMerchantSetting agentMerchantSetting = redisCacheCommonCompoment.agentMerchantSettingCahce.getOne(parentId,payType);
        if(null ==agentMerchantSetting ) agentMerchantSetting= new  AgentMerchantSetting();


        //获取代理商钱包
        AgentWallet agentWallet = paymentRecordSquareService.getAgentMerchantWallet(parentId);
        if(null == agentWallet){
            agentWallet =new AgentWallet();
            agentWallet.setAgentMerchantId(parentId);
        }

        BigDecimal agentRateFee = agentMerchantSetting.getRateFee();
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal singlefee = agentMerchantSetting.getSingleFee() != null ? agentMerchantSetting.getSingleFee() : new BigDecimal(0);
        if (orderObjectToMQ.getPayType().equals("6")){
            singlefee = new BigDecimal(0).subtract(singlefee);
        }
        BigDecimal agenetFee = amount.divide(hundred).multiply(agentRateFee).setScale(2, BigDecimal.ROUND_UP);
        agenetFee=agenetFee.add(singlefee);

        AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
        agentMerchantsDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details", ""));
        agentMerchantsDetails.setAgentMerId(agentWallet.getAgentMerchantId());
        agentMerchantsDetails.setMerOrderId(orderObjectToMQ.getMerOrderId());
        agentMerchantsDetails.setOrderId(orderObjectToMQ.getPayId());
        agentMerchantsDetails.setAmount(orderObjectToMQ.getAmount());
        agentMerchantsDetails.setType(orderObjectToMQ.getPayType());
        if (orderObjectToMQ.getPayType().equals("6")){
            agentMerchantsDetails.setOutAmount(new BigDecimal(0).subtract(agenetFee));
            agentMerchantsDetails.setInAmount(new BigDecimal(0));
        }else {
            agentMerchantsDetails.setInAmount(agenetFee);
            agentMerchantsDetails.setOutAmount(new BigDecimal(0));
        }

        agentMerchantsDetails.setUpdateTime(new Date());
        agentMerchantsDetails.setTotalBenifit( ( null ==  agentWallet.getTotalBalance()
                ? new BigDecimal(0).add(agenetFee)
                : agentWallet.getTotalBalance().add(agenetFee) ));
        return payMap.lput("agentMerchantsDetails",agentMerchantsDetails);
    }

    //----------------------------------------代付------------------------------------------------------------
    /**
     *    代付代理商钱包和明细
     * @param orderObjectToMQ
     * @param payMap
     * @return
     */
    public PayMap<String,Object>  handleAgentWalletAadDedails(OrderObjectToMQ orderObjectToMQ, PayMap<java.lang.String, java.lang.Object> payMap) throws PayException {
        BigDecimal amount = orderObjectToMQ.getAmount();
        if (orderObjectToMQ.getPayType().equals("6")){
            amount = new BigDecimal(0).subtract(amount);
        }
        String parentId = orderObjectToMQ.getParentId();
        String payType = orderObjectToMQ.getChannelType().toString();
        String merId=orderObjectToMQ.getMerId();
        //获取商户费率
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);
        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));

        //获取代理商钱包
        AgentWallet agentWallet = paymentRecordSquareService.getAgentMerchantWallet(parentId);

        //获取代理商设置
        AgentMerchantSetting agentMerchantSetting = redisCacheCommonCompoment.agentMerchantSettingCahce.getOne(parentId,payType);
        isNull(agentWallet,format("【MQ队列任务-钱包处理】商户号：%s,代理商配置对象为null",merId));

        BigDecimal agentSingleFee = agentMerchantSetting.getSingleFee();
        if (orderObjectToMQ.getPayType().equals("6")){
            agentSingleFee = new BigDecimal(0).subtract(agentSingleFee);
        }
        BigDecimal agentRateFee = agentMerchantSetting.getRateFee();
        agentRateFee= ( null == agentRateFee ? new BigDecimal(0) : agentRateFee);
        BigDecimal agenetFee = amount.divide(new BigDecimal(100)).multiply(agentRateFee).setScale(2, BigDecimal.ROUND_UP);
        agenetFee=agenetFee.add(agentSingleFee);
        //订单总额
//            agentWallet.setTotalAmount(agentWallet.getTotalAmount() == null ? agentWallet.getTotalAmount() : agentWallet.getTotalAmount().add(amount));
        //入账总额
        agentWallet.setIncomeAmount(agentWallet.getIncomeAmount() == null ? agenetFee : agentWallet.getIncomeAmount().add(agenetFee));
        //总余额
        agentWallet.setTotalBalance(agentWallet.getTotalBalance() == null ? agenetFee : agentWallet.getTotalBalance().add(agenetFee));

        if (merchantRate.getSettlecycle().equals("D0")) {
            //总可用 原始表总额 + 订单金额*手续费
            agentWallet.setTotalAvailableAmount(agentWallet.getTotalAvailableAmount() == null ? agenetFee : agentWallet.getTotalAvailableAmount().add(agenetFee));
        } else {
            //总不可 原始表总不可用+ 订单金额*手续费
            agentWallet.setTotalUnavailableAmount(agentWallet.getTotalUnavailableAmount() == null ? agenetFee : agentWallet.getTotalUnavailableAmount().add(agenetFee));
        }

        //组装代理商钱包明细
        AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
        agentMerchantsDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details", ""));
        agentMerchantsDetails.setAgentMerId(agentWallet.getAgentMerchantId());
        agentMerchantsDetails.setMerOrderId(orderObjectToMQ.getMerOrderId());
        agentMerchantsDetails.setOrderId(orderObjectToMQ.getTransId());
        agentMerchantsDetails.setAmount(orderObjectToMQ.getAmount());
        agentMerchantsDetails.setType(orderObjectToMQ.getPayType());
        if (orderObjectToMQ.getPayType().equals("6")){
            agentMerchantsDetails.setOutAmount(new BigDecimal(0).subtract(agenetFee));
        }else {
            agentMerchantsDetails.setInAmount(agenetFee);
        }

        agentMerchantsDetails.setTotalBenifit(agentWallet.getTotalBalance());
        agentMerchantsDetails.setUpdateTime(new Date());

        return payMap
                .lput("agentWallet",agentWallet)
                .lput("agentMerchantsDetails",agentMerchantsDetails);


    }

}
