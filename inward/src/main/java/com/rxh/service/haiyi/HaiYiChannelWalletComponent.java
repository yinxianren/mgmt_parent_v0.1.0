package com.rxh.service.haiyi;

import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.square.pojo.*;
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
public class HaiYiChannelWalletComponent implements PayUtil, PayAssert {


    @Autowired
    private PaymentRecordSquareService paymentRecordSquareService;
    @Autowired
    private RedisCacheCommonCompoment  redisCacheCommonCompoment;
    /**
     *   获取通道钱包和钱包明细
     * @param map
     * @param transOrder
     * @return
     */
    public PayMap<String,Object> handleChannelWalletAndDetails(PayMap<String,Object> map, TransOrder transOrder)throws PayException {
        return getChannelWallet(getChannelDetails(map,transOrder),transOrder);
    }

    /**
     *  获取平台钱包
     */
    public PayMap<String,Object>  getChannelWallet(PayMap<String,Object> map,TransOrder transOrder) throws PayException {

        String merId=transOrder.getMerId();
        String payType=transOrder.getPayType();
        //获取通道钱包
        ChannelWallet  channelWallet=paymentRecordSquareService.getChannelWallet(transOrder.getChannelId());
        boolean isInsert=false;
        if (channelWallet == null) {
            channelWallet = new ChannelWallet();
            channelWallet.setId(UUID.createKey("channel_wallet", ""));
            channelWallet.setChannelId(transOrder.getChannelId());
            channelWallet.setChannelTransCode(transOrder.getChannelTransCode());
            channelWallet.setPayType(transOrder.getPayType());
            isInsert=true;
        }

        //商户手续费
//        MerchantRate merchantRate=(MerchantRate)map.get("merchantRate");
//        if(null == merchantRate) merchantRate=paymentRecordSquareService.getMerchantRateByIdAndPayType(merId, payType);
//        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));
//        map.put("merchantRate",merchantRate);//下个环节使用
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);
        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));
        //获取通道信息
//        ChannelInfo channelInfo=(ChannelInfo)map.get("channelInfo");
//        if(null == channelInfo) channelInfo=paymentRecordSquareService.getChannelInfo(transOrder.getChannelId());
//        isNull(channelInfo,format("【MQ队列任务-钱包处理】商户号：%s,获取通道信息对象为null",merId));
//        map.put("channelInfo",channelInfo);//下个环节使用
        ChannelInfo channelInfo=redisCacheCommonCompoment.channelInfoCache.getOne(transOrder.getChannelId());
        isNull(channelInfo,format("【MQ队列任务-钱包处理】商户号：%s,获取通道信息对象为null",merId));

        //获取支付订单
        BigDecimal amount = transOrder.getAmount();
//        BigDecimal orderAmount = transOrder.getAmount();
        BigDecimal hundred = new BigDecimal(100);
        //商户手续费
        BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee().divide(hundred);
        BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
        //商户交易手续费=单笔手续费+手续费*订单金额
        BigDecimal merTransFee = amount.multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);

        //通道钱包
        BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
        BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
        BigDecimal channelFee = amount.divide(hundred).multiply(channelRateFee).add(channelSingleFee).setScale(BigDecimal.ROUND_HALF_UP,2);
        //手续费利润
        BigDecimal channelFeeProfit =merTransFee.subtract(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);
         //冻结余额
        BigDecimal totalFreezeAmount=channelWallet.getTotalFreezeAmount();
        totalFreezeAmount= (totalFreezeAmount==null? new BigDecimal(0) : totalFreezeAmount );
        channelWallet.setTotalFreezeAmount(totalFreezeAmount.subtract(amount));

//        channelWallet.setTotalAmount(channelWallet.getTotalAmount() == null ? amount : channelWallet.getTotalAmount().add(amount));
        channelWallet.setOutAmount(channelWallet.getOutAmount()==null ? amount : channelWallet.getOutAmount().add(amount));
        channelWallet.setTotalFee(channelWallet.getTotalFee() == null ? channelFee : channelWallet.getTotalFee().add(channelFee));
        channelWallet.setTotalBalance( channelWallet.getTotalBalance().subtract(amount) );

//        channelWallet.setTotalAvailableAmount(channelWallet.getTotalAvailableAmount().subtract(amount) );
        channelWallet.setFeeProfit(channelWallet.getFeeProfit()==null ? channelFeeProfit : channelWallet.getFeeProfit().add(channelFeeProfit));
        channelWallet.setUpdateTime(new Date());


        if(isInsert)  map.put("insertChannelWallet",channelWallet);
        else   map.put("updateChannelWallet",channelWallet);
        return map;
    }

    /**
     *   获取平台明细
     */
    public PayMap<String,Object> getChannelDetails(PayMap<String,Object> map,TransOrder transOrder) throws PayException {

        String merId=transOrder.getMerId();
        String payType=transOrder.getPayType();
        //获取商户费率
//        MerchantRate merchantRate=(MerchantRate)map.get("merchantRate");
//        if(null == merchantRate) merchantRate=paymentRecordSquareService.getMerchantRateByIdAndPayType(merId, payType);
//        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));
//        map.put("merchantRate",merchantRate);//下个环节使用
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);
        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));
        //获取通道信息
//        ChannelInfo channelInfo=(ChannelInfo)map.get("channelInfo");
//        if(null == channelInfo) channelInfo=paymentRecordSquareService.getChannelInfo(transOrder.getChannelId());
//        isNull(channelInfo,format("【MQ队列任务-钱包处理】商户号：%s,获取通道信息对象为null",merId));
//        map.put("channelInfo",channelInfo);//下个环节使用
        ChannelInfo channelInfo=redisCacheCommonCompoment.channelInfoCache.getOne(transOrder.getChannelId());
        isNull(channelInfo,format("【MQ队列任务-钱包处理】商户号：%s,获取通道信息对象为null",merId));
        //获取通道钱包
        ChannelWallet  channelWallet=paymentRecordSquareService.getChannelWallet(transOrder.getChannelId());
        if (channelWallet == null) {
            channelWallet = new ChannelWallet();
            channelWallet.setId(UUID.createKey("channel_wallet", ""));
            channelWallet.setChannelId(transOrder.getChannelId());
            channelWallet.setChannelTransCode(transOrder.getChannelTransCode());
            channelWallet.setPayType(transOrder.getPayType());
        }

        BigDecimal hundred = new BigDecimal(100);
        BigDecimal amount = transOrder.getRealAmount();
        BigDecimal orderAmount = transOrder.getAmount();

        //商户手续费
        BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
        BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
        BigDecimal merTransFee = orderAmount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);

        //通道钱包
        BigDecimal channelRateFee = channelInfo.getChannelRateFee() == null ? new BigDecimal(0) : channelInfo.getChannelRateFee();
        BigDecimal channelSingleFee = channelInfo.getChannelSingleFee() == null ? new BigDecimal(0) : channelInfo.getChannelSingleFee();
        BigDecimal channelFee = orderAmount.divide(hundred).multiply(channelRateFee).add(channelSingleFee).setScale(BigDecimal.ROUND_HALF_UP,2);
        BigDecimal channelFeeProfit =merTransFee.subtract(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

        //通道出账成本 订单金额+通道手续费
        BigDecimal channelOutAmount = amount.add(channelFee).setScale(BigDecimal.ROUND_HALF_UP,2);

        ChannelDetails channelDetails = new ChannelDetails();
        channelDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        channelDetails.setId(UUID.createKey("channel_details",""));
        channelDetails.setChannelId(transOrder.getChannelId());
        channelDetails.setChannelTransCode(transOrder.getChannelTransCode());
        channelDetails.setPayType(payType);
        channelDetails.setOrderId(transOrder.getTransId());
        channelDetails.setMerOrderId(transOrder.getMerOrderId());
        channelDetails.setType(channelInfo.getType().toString());
        channelDetails.setAmount(transOrder.getAmount());
        channelDetails.setFeeProfit(channelFeeProfit);
        channelDetails.setFee(channelFee);
        channelDetails.setOutAmount(channelOutAmount);
        channelDetails.setTotalBalance(channelWallet.getTotalBalance().subtract(amount));
        channelDetails.setUpdateTime(new Date());
        return map.lput("channelDetails",channelDetails);
    }

}
