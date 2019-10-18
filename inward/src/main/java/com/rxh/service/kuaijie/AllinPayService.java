package com.rxh.service.kuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.service.AbstractPayService;
import com.rxh.square.pojo.*;
import com.rxh.utils.UUID;
import com.rxh.vo.QueryOrderObjectToMQ;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AllinPayService extends AbstractPayService {



    /**
     * 更新钱包和订单
     * @param transOrder
     * @return
     */
    public Boolean updateWallet(TransOrder transOrder,PayOrder payOrder) throws PayException {

        Boolean b = false;
        //代理商钱包
        AgentWallet agentWallet = null;
        //商户钱包
        MerchantWallet merchantWallet = null;
        //平台钱包
        ChannelWallet channelWallet = null;
        //子商户钱包
        TerminalMerchantsWallet terminalMerchantsWallet= null;
        //商户钱包明细
        MerchantsDetails merchantsDetails = new MerchantsDetails();
        //子商户钱包明细
        TerminalMerchantsDetails terminalMerchantsDetails = new TerminalMerchantsDetails();
        //代理商钱包明细
        AgentMerchantsDetails agentMerchantsDetails = new AgentMerchantsDetails();
        //平台钱包明细
        ChannelDetails channelDetails = new ChannelDetails();

//        MerchantInfo merchantInfo = merchantInfoService.selectByMerId(transOrder.getMerId());
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(transOrder.getMerId());
//        ChannelInfo payOrderChannel = channelInfoService.selectByChannelId(transOrder.getChannelId());
        ChannelInfo payOrderChannel =  redisCacheCommonCompoment.channelInfoCache.getOne(transOrder.getChannelId());
        //获取商户钱包
        merchantWallet = paymentRecordSquareService.getMerchantWallet(merchantInfo.getMerId());
//        MerchantRate merchantRate = paymentRecordSquareService.getMerchantRateByIdAndPayType(merchantInfo.getMerId(), transOrder.getPayType());
        MerchantRate merchantRate = redisCacheCommonCompoment.merchantRateCache.getOne(merchantInfo.getMerId(), transOrder.getPayType());
        String settlecycle = merchantRate.getSettlecycle();
        //获取平台钱包
        channelWallet = paymentRecordSquareService.getChannelWallet(payOrder.getChannelId());
//        ChannelInfo channelInfo = paymentRecordSquareService.getChannelInfo(transOrder.getChannelId().toString());
        ChannelInfo channelInfo = redisCacheCommonCompoment.channelInfoCache.getOne(transOrder.getChannelId());
        //获取实际交易金额
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal amount = transOrder.getRealAmount();
        BigDecimal orderAmount = transOrder.getAmount();

        BigDecimal backFee = transOrder.getTerminalFee()==null?new BigDecimal(0):transOrder.getTerminalFee();

        //商户手续费
        BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
        BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
        BigDecimal merTransFee = orderAmount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal merFeeProfit = backFee.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP);


        //商户出账成本  实际出账金额+商户手续费
        BigDecimal merOutAmount = amount.add(merTransFee).setScale(2, BigDecimal.ROUND_UP);


        //商户交易钱包
//        merchantWallet.setTotalAmount(merchantWallet.getTotalAmount() == null ? amount : merchantWallet.getTotalAmount().add(amount));
        //出账金额
        merchantWallet.setOutAmount(merchantWallet.getOutAmount()==null?merOutAmount:merchantWallet.getOutAmount().add(merOutAmount));
        //手续费成本
        merchantWallet.setTotalFee(merchantWallet.getTotalFee() == null ? merTransFee : merchantWallet.getTotalFee().add(merTransFee));
        //总可用
        merchantWallet.setTotalAvailableAmount(merchantWallet.getTotalAvailableAmount().subtract(merOutAmount));
        //总余额
        merchantWallet.setTotalBalance( merchantWallet.getTotalBalance().subtract(merOutAmount) );
        //商户利润  下级手续费-商户手续费
        merchantWallet.setFeeProfit(merchantWallet.getFeeProfit()==null?merFeeProfit:merchantWallet.getFeeProfit().add(merFeeProfit));
        merchantWallet.setUpdateTime(new Date());

        //组装商户钱包明细
        merchantsDetails.setId(UUID.createKey("merchants_details",""));
        merchantsDetails.setMerId(merchantWallet.getMerId());
        merchantsDetails.setType(channelInfo.getType().toString());
        merchantsDetails.setOrderId(transOrder.getTransId());
        merchantsDetails.setMerOrderId(transOrder.getMerOrderId());
        merchantsDetails.setAmount(orderAmount);
        merchantsDetails.setFeeProfit(merFeeProfit);
        merchantsDetails.setOutAmount(merOutAmount);
        merchantsDetails.setFee(merTransFee);
        merchantsDetails.setTotalBalance(merchantWallet.getTotalBalance());
        merchantsDetails.setUpdateTime(new Date());


        //下级商户
        if (transOrder.getTerminalMerId()!=null&&!transOrder.getTerminalMerId().equals("")){
            //获取子商户钱包
            terminalMerchantsWallet = paymentRecordSquareService.getTerminalMerchantsWallet(transOrder.getMerId(),transOrder.getTerminalMerId());
            if (terminalMerchantsWallet==null){
                terminalMerchantsWallet=new TerminalMerchantsWallet();
                terminalMerchantsWallet.setId(UUID.createKey("",""));
                terminalMerchantsWallet.setMerId(transOrder.getMerId());
                terminalMerchantsWallet.setTerminalMerId(transOrder.getTerminalMerId());
            }

            //出账
            terminalMerchantsWallet.setOutAmount(terminalMerchantsWallet.getOutAmount()==null?orderAmount:terminalMerchantsWallet.getOutAmount().add(orderAmount));
            //总余额 1-a-fee
            terminalMerchantsWallet.setTotalBalance(terminalMerchantsWallet.getTotalBalance().subtract(orderAmount) );
            //总手续费
            terminalMerchantsWallet.setTotalFee(terminalMerchantsWallet.getTotalFee()==null?backFee:terminalMerchantsWallet.getTotalFee().add(backFee));
            //总可用 1-a-fee
            terminalMerchantsWallet.setTotalAvailableAmount(terminalMerchantsWallet.getTotalAvailableAmount().subtract(orderAmount) );

            terminalMerchantsWallet.setUpdateTime(new Date());
            //组装子商户钱包明细
            terminalMerchantsDetails.setId(UUID.createKey("terminal_merchants_details",""));
            terminalMerchantsDetails.setMerId(transOrder.getMerId());
            terminalMerchantsDetails.setTerminalMerId(transOrder.getTerminalMerId());
            terminalMerchantsDetails.setType(channelInfo.getType().toString());
            terminalMerchantsDetails.setMerOrderId(transOrder.getMerOrderId());
            terminalMerchantsDetails.setOrderId(transOrder.getTransId());
            terminalMerchantsDetails.setAmount(orderAmount);
            terminalMerchantsDetails.setOutAmount(orderAmount);
            terminalMerchantsDetails.setFee(backFee);
            terminalMerchantsDetails.setTotalBalance(terminalMerchantsWallet.getTotalBalance());
            terminalMerchantsDetails.setUpdateTime(new Date());

        }

        if( null !=channelWallet){
            //通道钱包
            BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
            BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
            BigDecimal channelFee = orderAmount.divide(hundred).multiply(channelRateFee).add(channelSingleFee).setScale(BigDecimal.ROUND_HALF_UP,2);
            BigDecimal channelFeeProfit =merTransFee.subtract(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

            //通道出账成本 订单金额+通道手续费
            BigDecimal channelOutAmount = amount.add(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

//        channelWallet.setTotalAmount(channelWallet.getTotalAmount() == null ? amount : channelWallet.getTotalAmount().add(amount));
            channelWallet.setOutAmount(channelWallet.getOutAmount()==null?channelOutAmount:channelWallet.getOutAmount().add(channelOutAmount));
            channelWallet.setTotalFee(channelWallet.getTotalFee() == null ? channelFee : channelWallet.getTotalFee().add(channelFee));
            channelWallet.setTotalBalance( channelWallet.getTotalBalance().subtract(channelOutAmount) );
            channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount().subtract(channelOutAmount) );
            channelWallet.setFeeProfit(channelWallet.getFeeProfit()==null?channelFeeProfit:channelWallet.getFeeProfit().add(channelFeeProfit));
            channelWallet.setUpdateTime(new Date());
            //组装平台钱包明细
            channelDetails.setId(UUID.createKey("channel_details",""));
            channelDetails.setChannelId(channelInfo.getChannelId());
            channelDetails.setChannelTransCode(channelInfo.getChannelTransCode());
            channelDetails.setPayType(transOrder.getPayType());
            channelDetails.setOrderId(transOrder.getTransId());
            channelDetails.setMerOrderId(transOrder.getMerOrderId());
            channelDetails.setType(channelInfo.getType().toString());
            channelDetails.setAmount(orderAmount);
            channelDetails.setFeeProfit(channelFeeProfit);
            channelDetails.setFee(channelFee);
            channelDetails.setOutAmount(channelOutAmount);
            channelDetails.setTotalBalance(channelWallet.getTotalBalance());
            channelDetails.setUpdateTime(new Date());
        }


        if ( merchantInfo.getParentId() != null && !merchantInfo.getParentId().equals("")) {
            //获取代理商钱包
            agentWallet = paymentRecordSquareService.getAgentMerchantWallet(merchantInfo.getParentId());
            if(null != agentWallet){
//            AgentMerchantSetting agentMerchantSetting = this.getAgentMerchantSetting(merchantInfo.getParentId());
//                AgentMerchantSetting agentMerchantSetting=paymentRecordSquareService.getAgentMerchantSettingByParentIdAndPayType(merchantInfo.getParentId(),transOrder.getPayType());
                AgentMerchantSetting agentMerchantSetting = redisCacheCommonCompoment.agentMerchantSettingCahce.getOne(merchantInfo.getParentId(),transOrder.getPayType());
                BigDecimal agentSingleFee = agentMerchantSetting.getSingleFee();
                //订单总额
//            agentWallet.setTotalAmount(agentWallet.getTotalAmount() == null ? agentWallet.getTotalAmount() : agentWallet.getTotalAmount().add(amount));
                //入账总额
                agentWallet.setIncomeAmount(agentWallet.getIncomeAmount()==null?agentSingleFee:agentWallet.getIncomeAmount().add(agentSingleFee));
                //总余额
                agentWallet.setTotalBalance(agentWallet.getTotalBalance()==null?agentSingleFee:agentWallet.getTotalBalance().add(agentSingleFee));

                if (settlecycle.equals("D0")){
                    //总可用 原始表总额 + 订单金额*手续费
                    agentWallet.setTotalAvailableAmount(agentWallet.getTotalAvailableAmount()==null?agentSingleFee:agentWallet.getTotalAvailableAmount().add(agentSingleFee));
                }else {
                    //总不可 原始表总不可用+ 订单金额*手续费
                    agentWallet.setTotalUnavailableAmount(agentWallet.getTotalUnavailableAmount()==null?agentSingleFee:agentWallet.getTotalUnavailableAmount().add(agentSingleFee));
                }
                //组装代理商钱包明细
                agentMerchantsDetails.setId(UUID.createKey("agent_merchants_details",""));
                agentMerchantsDetails.setAgentMerId(agentWallet.getAgentMerchantId());
                agentMerchantsDetails.setMerOrderId(transOrder.getMerOrderId());
                agentMerchantsDetails.setOrderId(transOrder.getTransId());
                agentMerchantsDetails.setAmount(orderAmount);
                agentMerchantsDetails.setType(channelInfo.getType().toString());
                agentMerchantsDetails.setInAmount(agentSingleFee);
                agentMerchantsDetails.setTotalBenifit(agentWallet.getTotalBalance());
                agentMerchantsDetails.setUpdateTime(new Date());
            }
        }
        try {
            //统一更新所有钱包信息
            allinPayWalletService.updateAllinPayWallet(agentWallet,merchantWallet,channelWallet,terminalMerchantsWallet,transOrder,merchantsDetails,terminalMerchantsDetails,agentMerchantsDetails,channelDetails);
            b =true;
        }catch (Exception e){
            logger.warn("通联代付更新钱包失败!");
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 收单和代付查询订单 封装队列对象
     * @param trade
     * @return
     */
    public QueryOrderObjectToMQ getQueryOrderObjectToMQ(SquareTrade trade){
        QueryOrderObjectToMQ queryOrderObjectToMQ = new QueryOrderObjectToMQ();
        queryOrderObjectToMQ.setChannelInfo(trade.getChannelInfo());
        MerchantRegisterCollect merchantRegisterCollect = new MerchantRegisterCollect();
        merchantRegisterCollect.setBackData(trade.getMerchantRegisterCollect().getBackData());
        queryOrderObjectToMQ.setMerchantRegisterCollect(merchantRegisterCollect);
        queryOrderObjectToMQ.setPayOrder(trade.getPayOrder());
        queryOrderObjectToMQ.setTransOrder(trade.getTransOrder());
        return queryOrderObjectToMQ;
    }
}
