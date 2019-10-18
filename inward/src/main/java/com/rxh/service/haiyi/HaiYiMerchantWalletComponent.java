package com.rxh.service.haiyi;


import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.square.pojo.MerchantWallet;
import com.rxh.square.pojo.MerchantsDetails;
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
public class HaiYiMerchantWalletComponent implements PayUtil, PayAssert {



    @Autowired
    private PaymentRecordSquareService paymentRecordSquareService;

    /**
     *    获取钱包对象和钱包明细对象
     */
    public PayMap<String,Object> handleMerchantWalletAndDetails(PayMap<String,Object> map, TransOrder transOrder)throws PayException {
        return getMerchantWallet(getMerchantsDetails(map,transOrder),transOrder);
    }
    /**
     *   获取商户钱包
     */
    public PayMap<String,Object>  getMerchantWallet(PayMap<String,Object> map,TransOrder transOrder)throws PayException {
        String merId=transOrder.getMerId();
        BigDecimal amount=transOrder.getAmount();
        String merOrderId=transOrder.getMerOrderId();

        //获取商户钱包
        MerchantWallet merchantWallet = paymentRecordSquareService.getMerchantWallet(merId);
        isNull(merchantWallet, format("【海懿代付】处理队列任务(成功订单)：商户号：%s,代付订单号：%s,处理队列任务(成功订单)：获取商户钱包对象异常！",merId,merOrderId));

        //计算 商户总余额
        BigDecimal merchantTotalBalance = merchantWallet.getTotalBalance();
        isNull(merchantTotalBalance, "【海懿代付】处理队列任务(成功订单)：获取商户总可用余额异常！");
        state(!(merchantTotalBalance.compareTo(amount) == -1), "【海懿代付】处理队列任务(成功订单)：商户总可用余额小于订单金额");

        //设置总手续成本
        BigDecimal merTotalFee = merchantWallet.getTotalFee();
        merTotalFee = (null == merTotalFee ? new BigDecimal(0) : merTotalFee);
        merchantWallet.setTotalFee(merTotalFee.add(transOrder.getFee()));

        //获取冻结资金
        BigDecimal totalFreezeAmount = merchantWallet.getTotalFreezeAmount();
        if (null == totalFreezeAmount|| totalFreezeAmount.compareTo(amount)== -1|| merchantTotalBalance.compareTo(totalFreezeAmount) == -1)
            state(false, "【海懿代付】处理队列任务(成功订单)：冻结金额为空或者冻结金额大于总余额或者订单金额大于冻结金额");

        //冻结金额-总订单金额
        //计算剩余的冻结金额，这个金额需要更新到数据库中
        totalFreezeAmount = totalFreezeAmount.subtract(amount);
        merchantWallet.setTotalFreezeAmount(totalFreezeAmount);

        //总余额
        merchantTotalBalance=merchantTotalBalance.subtract(amount);
        merchantWallet.setTotalBalance(merchantTotalBalance);

        //2-4 设置出账金额  == 出账金额 + 总订单金额
        BigDecimal merOutAmout = merchantWallet.getOutAmount();
        merOutAmout = (null == merOutAmout ? new BigDecimal(0) : merOutAmout);
        merchantWallet.setOutAmount(merOutAmout.add(amount));

        return  map.lput("merchantWallet",merchantWallet);
    }


    /**
     *   获取商户钱包明细
     */
    public PayMap<String,Object>  getMerchantsDetails(PayMap<String,Object> map,TransOrder transOrder) throws PayException {

        String merId=transOrder.getMerId();
        BigDecimal amount=transOrder.getAmount();
        String merOrderId=transOrder.getMerOrderId();
        //获取商户钱包
        MerchantWallet merchantWallet = paymentRecordSquareService.getMerchantWallet(merId);
        isNull(merchantWallet, format("【海懿代付】处理队列任务(成功订单)：商户号：%s,代付订单号：%s,处理队列任务(成功订单)：获取商户钱包对象异常！",merId,merOrderId));

        //计算 商户总余额
        BigDecimal merchantTotalBalance = merchantWallet.getTotalBalance();
        isNull(merchantTotalBalance, format("【海懿代付】处理队列任务(成功订单)：商户号：%s,代付订单号：%s,获取商户总可用余额为null！",merId,merOrderId));
        state(!(merchantTotalBalance.compareTo(amount) == -1),  format("【海懿代付】处理队列任务(成功订单)：商户号：%s,代付订单号：%s,商户总可用余额小于订单金额",merId,merOrderId));
        BigDecimal merchantDetailsTotalBalance = merchantTotalBalance.subtract(amount);
        //创建商户明细对象
        MerchantsDetails merchantsDetails = new MerchantsDetails();
        merchantsDetails.setId(UUID.createKey("merchants_details", ""));
        merchantsDetails.setMerId(merId);
        merchantsDetails.setType("4");//代付
        merchantsDetails.setOrderId(transOrder.getTransId());
        merchantsDetails.setMerOrderId(transOrder.getMerOrderId());
        merchantsDetails.setAmount(amount);
        merchantsDetails.setFeeProfit(null);
        merchantsDetails.setOutAmount(amount);
        merchantsDetails.setFee(transOrder.getFee());
        merchantsDetails.setTotalBalance(merchantDetailsTotalBalance);
        merchantsDetails.setUpdateTime(new Date());
        merchantsDetails.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return map.lput("merchantsDetails",merchantsDetails);
    }


}
