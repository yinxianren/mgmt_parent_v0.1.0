package com.rxh.wallet;


import com.rxh.exception.PayException;
import com.rxh.square.pojo.MerchantRate;
import com.rxh.square.pojo.MerchantWallet;
import com.rxh.square.pojo.MerchantsDetails;
import com.rxh.utils.PayMap;
import com.rxh.utils.UUID;
import com.rxh.vo.OrderObjectToMQ;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述： 商户钱包相关业务
 * @author  panda
 * @date 20190721
 *
 */
@Component
public class MerchantWalletComponet extends AbstractWalletComponent{



    /**
     *   支付商户钱包
     *
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public PayMap<String,Object> getMerchantWalletObject(OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap) throws PayException {
        String merId=orderObjectToMQ.getMerId();
        String payType=orderObjectToMQ.getChannelType().toString();
        boolean isInsert=false;
        //获取商户费率
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);

        //获取商户钱包信息
        MerchantWallet merchantWallet= paymentRecordSquareService.getMerchantWallet(merId);
        if(null == merchantWallet){
            merchantWallet=new MerchantWallet();
            isInsert=true;
        }

        BigDecimal amount=orderObjectToMQ.getAmount();//订单金额
        if (orderObjectToMQ.getPayType().equals("6")){
            amount = new BigDecimal(0).subtract(amount);
        }
        String settlecycle = merchantRate.getSettlecycle();//结算周期

        BigDecimal hundred = new BigDecimal(100);
        //交易费率
        BigDecimal merRate = (merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee().divide(hundred));
        //单笔手续费
        BigDecimal singlFee = (merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee());
        if (orderObjectToMQ.getPayType().equals("6")){
            singlFee = new BigDecimal(0).subtract(singlFee);
        }
        //保证金比例
        BigDecimal bondRate = (merchantRate.getBondRate() == null ? new BigDecimal(0) : merchantRate.getBondRate().divide(hundred));
        //客户订单上传的交易费率
        BigDecimal payFee = orderObjectToMQ.getPayFee() == null ? new BigDecimal(0) : orderObjectToMQ.getPayFee().divide(hundred);


        // 商户交易手续费 = 订单金额* 商户手续费 + 单笔
        BigDecimal merTransFee = amount.multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
        // 保证金   = 订单金额 *  保证金比例
        BigDecimal merBondAmount = amount.multiply(bondRate).setScale(2, BigDecimal.ROUND_UP);
        //终端商户的手续费 = 订单金额 *  支付费率
        BigDecimal terPayFee = amount.multiply(payFee).setScale(2, BigDecimal.ROUND_UP);
        //商户手续费利润=终端商户的手续费-商户交易手续费
        BigDecimal merFeeProfit = terPayFee.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP);

        //商户号
        merchantWallet.setMerId(merId);
        //累计订单总额
        merchantWallet.setTotalAmount( merchantWallet.getTotalAmount() == null ? amount : merchantWallet.getTotalAmount().add(amount));
        //入账总额  原始表入账总额+(订单金额-手续费)
        merchantWallet.setIncomeAmount(merchantWallet.getIncomeAmount() == null ?
                amount.subtract(merTransFee)
                : merchantWallet.getIncomeAmount().add(amount.subtract(merTransFee)).setScale(2, BigDecimal.ROUND_UP));

        //手续费成本：原始表手续费金额+（单笔手续费+订单金额*商户交易手续费）
        merchantWallet.setTotalFee(merchantWallet.getTotalFee() == null ?
                merTransFee
                : merchantWallet.getTotalFee().add(merTransFee).setScale(2, BigDecimal.ROUND_UP));

        //保证金：原始表保证金金额+ 订单金额* 商户保证金比例
        merchantWallet.setTotalBond(merchantWallet.getTotalBond() == null ?
                merBondAmount : merchantWallet.getTotalBond().add(merBondAmount).setScale(2, BigDecimal.ROUND_UP));

        //总余额 原始表总余额+订单金额-手续费
        merchantWallet.setTotalBalance(merchantWallet.getTotalBalance() == null ?
                amount.subtract(merTransFee)
                : merchantWallet.getTotalBalance().add(amount.subtract(merTransFee)).setScale(2, BigDecimal.ROUND_UP));

        //手续费利润
        merchantWallet.setFeeProfit( merchantWallet.getFeeProfit() == null ?
                merFeeProfit : merchantWallet.getFeeProfit().add(merFeeProfit));


        if (settlecycle.equalsIgnoreCase("D0") || settlecycle.equalsIgnoreCase("T0")) {
            //总可用
            merchantWallet.setTotalAvailableAmount(merchantWallet.getTotalAvailableAmount() == null ?
                    amount.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP) :
                    merchantWallet.getTotalAvailableAmount().add(amount.subtract(merTransFee)).setScale(2, BigDecimal.ROUND_UP));
            payMap.put("settleStatus","1");
        } else {
            //总不可 原始表总不可用+ 订单金额-手续费-保证金
            merchantWallet.setTotalUnavailableAmount(merchantWallet.getTotalUnavailableAmount() == null ?
                    amount.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP).subtract(merBondAmount) :
                    merchantWallet.getTotalUnavailableAmount().add(amount.subtract(merTransFee).subtract(merBondAmount)).setScale(2, BigDecimal.ROUND_UP));
        }
        merchantWallet.setUpdateTime(new Date());
        if(isInsert) payMap.put("insertMerchantWallet",merchantWallet);
        else  payMap.put("updateMerchantWallet",merchantWallet);
        return   payMap;
    }


    /**
     *   支付商户钱包明细
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public  PayMap<String,Object> getMerchantsDetailsObject(OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap ) throws PayException {

        BigDecimal amount=orderObjectToMQ.getAmount();//订单金额
        if (orderObjectToMQ.getPayType().equals("6")){
            amount = new BigDecimal(0).subtract(amount);
        }
        String merId=orderObjectToMQ.getMerId();
        String payType=orderObjectToMQ.getChannelType().toString();

        //获取商户钱包信息
        MerchantWallet merchantWallet= paymentRecordSquareService.getMerchantWallet(merId);
        if(isNull(merchantWallet)){
            merchantWallet=new  MerchantWallet();
        }

        //获取商户费率
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);
        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));

        BigDecimal hundred = new BigDecimal(100);
        //交易费率
        BigDecimal merRate = (merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee().divide(hundred));
        //单笔手续费
        BigDecimal singlFee = (merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee());
        if (orderObjectToMQ.getPayType().equals("6")){
            singlFee = new BigDecimal(0).subtract(singlFee);
        }
        // 商户交易手续费=订单金额* 商户手续费 + 单笔
        BigDecimal merTransFee = amount.multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal payFee = orderObjectToMQ.getPayFee() == null ? new BigDecimal(0) : orderObjectToMQ.getPayFee().divide(hundred);
        BigDecimal terPayFee = amount.multiply(payFee).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal merFeeProfit = terPayFee.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP);

        MerchantsDetails merchantsDetails = new MerchantsDetails();
        merchantsDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        merchantsDetails.setId(UUID.createKey("merchants_details", ""));
        merchantsDetails.setMerId(merId);
        merchantsDetails.setType(orderObjectToMQ.getPayType());
        merchantsDetails.setMerOrderId(orderObjectToMQ.getMerOrderId());
        merchantsDetails.setOrderId(orderObjectToMQ.getPayId());
        merchantsDetails.setAmount(orderObjectToMQ.getAmount());
        if (orderObjectToMQ.getPayType().equals("6")){
            merchantsDetails.setOutAmount(new BigDecimal(0).subtract(amount.subtract(merTransFee)));
            merchantsDetails.setInAmount(new BigDecimal(0));
        }else {
            merchantsDetails.setInAmount(amount.subtract(merTransFee));
            merchantsDetails.setOutAmount(new BigDecimal(0));
        }

        merchantsDetails.setFeeProfit(merFeeProfit);
        merchantsDetails.setFee(merTransFee);
        merchantsDetails.setTotalBalance(
                ( null == merchantWallet.getTotalBalance()  ? new BigDecimal(0).add(merchantsDetails.getInAmount().subtract(merchantsDetails.getOutAmount()))
                        : merchantWallet.getTotalBalance().add(merchantsDetails.getInAmount().subtract(merchantsDetails.getOutAmount())) )
        );
        merchantsDetails.setUpdateTime(new Date());
        return payMap.lput("merchantsDetails",merchantsDetails);
    }




    //----------------------------------------------代付------------------------------------------------------------------------------------------------

    /**
     *   代付商户钱包和明细
     * @param orderObjectToMQ
     * @param payMap
     * @return
     * @throws PayException
     */
    public PayMap<String,Object>  handleMerchantWalletAadDedails(OrderObjectToMQ orderObjectToMQ, PayMap<String,Object> payMap) throws PayException {
        String merId=orderObjectToMQ.getMerId();
        String payType=orderObjectToMQ.getChannelType().toString();

        //获取商户钱包
        MerchantWallet  merchantWallet = paymentRecordSquareService.getMerchantWallet(merId);
        isNull(merchantWallet,format("【MQ队列任务-钱包处理】商户号：%s,获取商户钱包对象为null",merId));

        //获取商户费率
        MerchantRate merchantRate= redisCacheCommonCompoment.merchantRateCache.getOne(merId, payType);
        isNull(merchantRate,format("【MQ队列任务-钱包处理】商户号：%s,获取商户费率对象为null",merId));

        //获取实际交易金额
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal amount = orderObjectToMQ.getRealAmount();
        BigDecimal orderAmount = orderObjectToMQ.getAmount();
        if (orderObjectToMQ.getPayType().equals("6")){
            orderAmount =  new BigDecimal(0).subtract(orderAmount);
        }

        BigDecimal backFee = orderObjectToMQ.getTerminalFee()==null?new BigDecimal(0):orderObjectToMQ.getTerminalFee();

        //商户手续费
        BigDecimal merRate = merchantRate.getRateFee() == null ? new BigDecimal(0) : merchantRate.getRateFee();
        BigDecimal singlFee = merchantRate.getSingleFee() == null ? new BigDecimal(0) : merchantRate.getSingleFee();
        if (orderObjectToMQ.getPayType().equals("6")){
            singlFee = new BigDecimal(0).subtract(singlFee);
        }
        BigDecimal merTransFee = orderAmount.divide(hundred).multiply(merRate).add(singlFee).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal merFeeProfit = backFee.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP);
        //商户出账成本  实际出账金额+商户手续费
//        BigDecimal merOutAmount = amount.add(merTransFee).setScale(2, BigDecimal.ROUND_UP);
        //商户出账金额
        BigDecimal outAmount = orderAmount.subtract(merTransFee).setScale(2, BigDecimal.ROUND_UP);
        //商户交易钱包
//        merchantWallet.setTotalAmount(merchantWallet.getTotalAmount() == null ? amount : merchantWallet.getTotalAmount().add(amount));
        //出账金额
        merchantWallet.setOutAmount(merchantWallet.getOutAmount()==null?outAmount:merchantWallet.getOutAmount().add(outAmount));
        //手续费成本
        merchantWallet.setTotalFee(merchantWallet.getTotalFee() == null ? merTransFee : merchantWallet.getTotalFee().add(merTransFee));
        //总可用
        merchantWallet.setTotalAvailableAmount(merchantWallet.getTotalAvailableAmount().subtract(outAmount));
        //总余额
        merchantWallet.setTotalBalance( merchantWallet.getTotalBalance().subtract(outAmount) );
        //商户利润  下级手续费-商户手续费
        merchantWallet.setFeeProfit(merchantWallet.getFeeProfit()==null?merFeeProfit:merchantWallet.getFeeProfit().add(merFeeProfit));
        merchantWallet.setUpdateTime(new Date());
        //商户钱包明细
        MerchantsDetails merchantsDetails = new MerchantsDetails();
        merchantsDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        merchantsDetails.setId(UUID.createKey("merchants_details",""));
        merchantsDetails.setMerId(merId);
        merchantsDetails.setType(orderObjectToMQ.getPayType());
        merchantsDetails.setOrderId(orderObjectToMQ.getTransId());
        merchantsDetails.setMerOrderId(orderObjectToMQ.getMerOrderId());
        merchantsDetails.setAmount(orderObjectToMQ.getAmount());
        merchantsDetails.setFeeProfit(merFeeProfit);
        if (orderObjectToMQ.getPayType().equals("6")){
            merchantsDetails.setInAmount(new BigDecimal(0).subtract(outAmount));
            merchantsDetails.setOutAmount(new BigDecimal(0));

        }else {
            merchantsDetails.setOutAmount(outAmount);
            merchantsDetails.setInAmount(new BigDecimal(0));
        }
        merchantsDetails.setFee(merTransFee);
        merchantsDetails.setTotalBalance(merchantWallet.getTotalBalance());
        merchantsDetails.setUpdateTime(new Date());
        return payMap
                .lput("merchantsDetails",merchantsDetails)
                .lput("merchantWallet",merchantWallet);
    }

}
