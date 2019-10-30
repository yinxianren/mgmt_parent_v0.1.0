package com.rxh.wallet;


import com.rxh.exception.PayException;
import com.rxh.square.pojo.*;
import com.rxh.utils.PayMap;
import com.rxh.utils.UUID;
import com.rxh.vo.OrderObjectToMQ;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述： 通道钱包业务
 * @author  panda
 * @date 20190721
 *
 */
@Component
public class ChannelWalletComponet extends AbstractWalletComponent {


    /**
     *   支付平台钱包
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public PayMap<String,Object>  getChannelWalletObject(OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap) throws PayException {

        String merId=orderObjectToMQ.getMerId();
        String payType=orderObjectToMQ.getChannelType().toString();
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal amount = orderObjectToMQ.getAmount();  //获取交易金额
        if (orderObjectToMQ.getPayType().equals("6")){
            amount = new BigDecimal(0).subtract(amount);
        }
        //获取通道钱包
        ChannelWallet  channelWallet=paymentRecordSquareService.getChannelWallet(orderObjectToMQ.getChannelId());
        boolean isInsert=false;
        if (channelWallet == null) {
            channelWallet = new ChannelWallet();
            channelWallet.setId(UUID.createKey("channel_wallet", ""));
            channelWallet.setChannelId(orderObjectToMQ.getChannelId());
            channelWallet.setChannelTransCode(orderObjectToMQ.getChannelTransCode());
            channelWallet.setPayType(orderObjectToMQ.getPayType());
            isInsert=true;
        }
        //获取商户费率
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);
        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));
        //获取通道信息
        ChannelInfo channelInfo=redisCacheCommonCompoment.channelInfoCache.getOne(orderObjectToMQ.getChannelId());

        BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
        BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
        // 订单金额/100 * 商户手续费 + 单笔
        if (orderObjectToMQ.getPayType().equals("6")){
            singlFee = new BigDecimal(0).subtract(singlFee);
        }
        BigDecimal merTransFee = amount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
        BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
        if (orderObjectToMQ.getChannelType() == 6){
            channelSingleFee = new BigDecimal(0).subtract(channelSingleFee);
        }
        BigDecimal channelIncomeAmount = amount.subtract(channelSingleFee.add(amount.divide(hundred).multiply(channelRateFee))).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal channelFee = channelSingleFee.add(amount.divide(hundred).multiply(channelRateFee)).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal channelFeeProfit = merTransFee.subtract(channelFee);

        //  订单总额
        channelWallet.setTotalAmount(channelWallet.getTotalAmount() == null ?
                amount : channelWallet.getTotalAmount().add(amount).setScale(2, BigDecimal.ROUND_UP));
        //入账总额
        channelWallet.setIncomeAmount(channelWallet.getIncomeAmount() == null ?
                channelIncomeAmount : channelWallet.getIncomeAmount().add(channelIncomeAmount).setScale(2, BigDecimal.ROUND_UP));
        // 手续费总额
        channelWallet.setTotalFee(channelWallet.getTotalFee() == null ?
                channelFee : channelWallet.getTotalFee().add(channelFee).setScale(2, BigDecimal.ROUND_UP));
        //  总余额
        channelWallet.setTotalBalance(channelWallet.getTotalBalance() == null ?
                channelIncomeAmount : channelWallet.getTotalBalance().add(channelIncomeAmount).setScale(2, BigDecimal.ROUND_UP));

        //  通道手续费-商户手续费
        channelWallet.setFeeProfit(channelWallet.getFeeProfit() == null ? channelFeeProfit : channelWallet.getFeeProfit().add(channelFeeProfit));

        if (merchantRate.getSettlecycle().equalsIgnoreCase("D0") || merchantRate.getSettlecycle().equalsIgnoreCase("T0")) {
            //总可用
            channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount() == null ?
                    channelIncomeAmount : channelWallet.getTotalAvailableAmount().add(channelIncomeAmount).setScale(2, BigDecimal.ROUND_UP));
        } else {
            //总不可 原始表总不可用+ 订单金额-手续费
            channelWallet.setTotalUnavailableAmount(channelWallet.getTotalUnavailableAmount() == null ?
                    channelIncomeAmount : channelWallet.getTotalUnavailableAmount().add(channelIncomeAmount).setScale(2, BigDecimal.ROUND_UP));
        }
        channelWallet.setUpdateTime(new Date());

        if(isInsert)  payMap.put("insertChannelWallet",channelWallet);
        else   payMap.put("updateChannelWallet",channelWallet);
        return payMap;
    }

    /**
     *    支付平台明细
     * @param orderObjectToMQ
     * @param payMap
     * @return
     */
    public PayMap<String,Object> getChannelDetailsObject (OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap) throws PayException {


        String merId=orderObjectToMQ.getMerId();
        String payType=orderObjectToMQ.getChannelType().toString();
        //获取通道信息
        ChannelInfo channelInfo=redisCacheCommonCompoment.channelInfoCache.getOne(orderObjectToMQ.getChannelId());
        //获取商户费率
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);
        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));
        //获取通道钱包
        ChannelWallet  channelWallet=paymentRecordSquareService.getChannelWallet(orderObjectToMQ.getChannelId());
        if (channelWallet == null) {
            channelWallet = new ChannelWallet();
            channelWallet.setId(UUID.createKey("channel_wallet", ""));
            channelWallet.setChannelId(orderObjectToMQ.getChannelId());
            channelWallet.setChannelTransCode(orderObjectToMQ.getChannelTransCode());
            channelWallet.setPayType(orderObjectToMQ.getPayType());
        }

        BigDecimal amount = orderObjectToMQ.getAmount();
        BigDecimal hundred = new BigDecimal(100);
        if (orderObjectToMQ.getPayType().equals("6")){
            amount = new BigDecimal(0).subtract(amount);
        }
        //商户手续费
        BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
        BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
        if (orderObjectToMQ.getPayType().equals("6")){
            singlFee = new BigDecimal(0).subtract(singlFee);
        }
        // 订单金额/100 * 商户手续费 + 单笔
        BigDecimal merTransFee = amount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
        //通道钱包
        BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
        BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
        if (orderObjectToMQ.getPayType().equals("6")){
            channelSingleFee = new BigDecimal(0).subtract(channelSingleFee);
        }

        BigDecimal channelFee = channelSingleFee.add(amount.divide(hundred).multiply(channelRateFee)).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal channelFeeProfit = merTransFee.subtract(channelFee);
        BigDecimal channelIncomeAmount = amount.subtract(channelFee);

        ChannelDetails channelDetails = new ChannelDetails();
        channelDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        channelDetails.setId(UUID.createKey("channel_details", ""));
        channelDetails.setChannelId(channelInfo.getChannelId());
        channelDetails.setChannelTransCode(channelInfo.getChannelTransCode());
        channelDetails.setPayType(orderObjectToMQ.getPayType());
        channelDetails.setMerOrderId(orderObjectToMQ.getMerOrderId());
        channelDetails.setOrderId(orderObjectToMQ.getPayId());
        channelDetails.setType(orderObjectToMQ.getPayType());
        channelDetails.setAmount(orderObjectToMQ.getAmount());
        channelDetails.setFeeProfit(channelFeeProfit);
        channelDetails.setFee(channelFee);
        if (orderObjectToMQ.getPayType().equals("6")){
            channelDetails.setOutAmount(new BigDecimal(0).subtract(channelIncomeAmount));
            channelDetails.setInAmount(new BigDecimal(0));
        }else {
            channelDetails.setInAmount(channelIncomeAmount);
            channelDetails.setOutAmount(new BigDecimal(0));
        }

        channelDetails.setTotalBalance( (null == channelWallet.getTotalBalance()
                ? new BigDecimal(0).add(channelIncomeAmount).subtract(channelDetails.getOutAmount())
                : channelWallet.getTotalBalance().add(channelIncomeAmount).subtract(channelDetails.getOutAmount()))
        );
        channelDetails.setUpdateTime(new Date());

        return payMap.lput("channelDetails",channelDetails);
    }

    //-------------------------------------------代付-------------------------------------------------------------------------------

    /**
     *   代付平台钱包和明细
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public PayMap<String,Object>  handleChannelWalletAadDedails(OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap) throws PayException {
        String merId=orderObjectToMQ.getMerId();
        String payType=orderObjectToMQ.getChannelType().toString();
        //获取支付订单
        PayOrder payOrder= paymentRecordSquareService.getPayOrderById(orderObjectToMQ.getPayIds()[0]);
        //获取通道钱包
        ChannelWallet channelWallet=paymentRecordSquareService.getChannelWallet(payOrder.getChannelId());
        //获取通道信息
        ChannelInfo channelInfo=redisCacheCommonCompoment.channelInfoCache.getOne(orderObjectToMQ.getChannelId());

        //获取商户费率
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);
        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));


        BigDecimal hundred = new BigDecimal(100);
        BigDecimal amount = orderObjectToMQ.getRealAmount();
        BigDecimal orderAmount = orderObjectToMQ.getAmount();
        if (orderObjectToMQ.getPayType().equals("6")){
            orderAmount = new BigDecimal(0).subtract(orderAmount);
        }
        //商户手续费
        BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
        BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
        if (orderObjectToMQ.getPayType().equals("6")){
            singlFee = new BigDecimal(0).subtract(singlFee);
        }
        BigDecimal merTransFee = orderAmount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);

        //通道钱包
        BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
        BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
        if (orderObjectToMQ.getPayType().equals("6")){
            channelSingleFee = new BigDecimal(0).subtract(channelSingleFee);
        }
        BigDecimal channelFee = orderAmount.divide(hundred).multiply(channelRateFee).add(channelSingleFee).setScale(BigDecimal.ROUND_HALF_UP,2);
        BigDecimal channelFeeProfit =merTransFee.subtract(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

        //通道出账成本 订单金额+通道手续费
//        BigDecimal channelOutAmount = amount.add(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);
        //通道出账金额
        BigDecimal outAmount = orderAmount.subtract(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

//        channelWallet.setTotalAmount(channelWallet.getTotalAmount() == null ? amount : channelWallet.getTotalAmount().add(amount));
        channelWallet.setOutAmount(channelWallet.getOutAmount()==null ? outAmount : channelWallet.getOutAmount().add(outAmount));
        channelWallet.setTotalFee(channelWallet.getTotalFee() == null ? channelFee : channelWallet.getTotalFee().add(channelFee));
        channelWallet.setTotalBalance( channelWallet.getTotalBalance().subtract(outAmount) );
        channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount().subtract(outAmount) );
        channelWallet.setFeeProfit(channelWallet.getFeeProfit()==null ? channelFeeProfit : channelWallet.getFeeProfit().add(channelFeeProfit));
        channelWallet.setUpdateTime(new Date());
        //组装平台钱包明细
        ChannelDetails channelDetails = new ChannelDetails();
        channelDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        channelDetails.setId(UUID.createKey("channel_details",""));
        channelDetails.setChannelId(orderObjectToMQ.getChannelId());
        channelDetails.setChannelTransCode(orderObjectToMQ.getChannelTransCode());
        channelDetails.setPayType(orderObjectToMQ.getPayType());
        channelDetails.setOrderId(orderObjectToMQ.getTransId());
        channelDetails.setMerOrderId(orderObjectToMQ.getMerOrderId());
        channelDetails.setType(orderObjectToMQ.getPayType());
        channelDetails.setAmount(orderObjectToMQ.getAmount());
        channelDetails.setFeeProfit(channelFeeProfit);
        channelDetails.setFee(channelFee);
        if (orderObjectToMQ.getPayType().equals("6")){
            channelDetails.setInAmount(new BigDecimal(0).subtract(outAmount));
        }else {
            channelDetails.setOutAmount(outAmount);
        }

        channelDetails.setTotalBalance(channelWallet.getTotalBalance());
        channelDetails.setUpdateTime(new Date());

        return payMap
                .lput("channelDetails",channelDetails)
                .lput("channelWallet",channelWallet);
    }

}
