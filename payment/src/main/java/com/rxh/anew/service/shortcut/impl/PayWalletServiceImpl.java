package com.rxh.anew.service.shortcut.impl;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.service.PayWalletService;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.channel.ChannelWalletTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;
import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午6:40
 * Description:
 */
@Service
public class PayWalletServiceImpl extends CommonServiceAbstract implements PayWalletService {


    @Override
    public MerchantInfoTable getMerInfo(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerInfo(InnerPrintLogObject ipo)";
        MerchantInfoTable mit = null;
        try {
            mit = commonRPCComponent.apiMerchantInfoService.getOne(new MerchantInfoTable().setMerchantId(ipo.getMerId()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询商户信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(mit,
                ResponseCodeEnum.RXH00017.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00017.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00017.getMsg()));
        return mit;
    }

    @Override
    public MerchantRateTable getMerRate(PayOrderInfoTable poi, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerRate(PayOrderInfoTable poi, InnerPrintLogObject ipo)";
        MerchantRateTable mrt = null;
        try{
            mrt = commonRPCComponent.apiMerchantRateService.getOne(new MerchantRateTable()
                    .setProductId(poi.getProductId())
                    .setChannelId(poi.getChannelId())
                    .setMerchantId(ipo.getMerId())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询商户产品费率信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(mrt,
                ResponseCodeEnum.RXH00041.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；产品类型：%s,代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00041.getMsg(),poi.getProductId(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00041.getMsg()));
        return null;
    }

    @Override
    public MerchantWalletTable getMerWallet(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerWallet(InnerPrintLogObject ipo)";
        MerchantWalletTable mwt = null;
        try{
            mwt = commonRPCComponent.apiMerchantWalletService.getOne(new MerchantWalletTable()
                    .setMerchantId(ipo.getMerId())
                    .setStatus(StatusEnum._0.getStatus())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询商户钱包信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }
        return  null == mwt ? new MerchantWalletTable() : mwt ;
    }

    @Override
    public Tuple2<MerchantWalletTable, MerchantsDetailsTable> updateMerWallet(MerchantWalletTable mwt, PayOrderInfoTable poi, MerchantRateTable mrt) {
        //订单金额
        BigDecimal amount = poi.getAmount();
        //总订单金额
        BigDecimal totalAmount = ( null == mwt.getTotalAmount() ?  amount :  mwt.getTotalAmount().add(amount) );
        BigDecimal rateFee = (null == mrt.getRateFee() ? new BigDecimal(0) : mrt.getRateFee() );
        BigDecimal singleFee = (null == mrt.getSingleFee() ? new BigDecimal(0) : mrt.getSingleFee() );
        //单笔总费用
        BigDecimal totalSingleFee = ( totalAmount.multiply(rateFee).setScale(2, BigDecimal.ROUND_UP ) .add(singleFee)) ;
        //入账金额
        BigDecimal inAmount = amount.subtract(totalSingleFee);
        //总入账金额
        BigDecimal totalIncomeAmount = (null == mwt.getIncomeAmount() ? inAmount :  mwt.getIncomeAmount().add(inAmount) );
        //总手续费用
        BigDecimal totalFee = ( null == mwt.getTotalFee() ? totalSingleFee : mwt.getTotalFee().add(totalSingleFee) );
        //终端手续费
        BigDecimal terMerFee = amount.multiply(poi.getPayFee()).setScale(2, BigDecimal.ROUND_UP );
        //商户单笔手续费利润 = 终端手续费 - 单笔总费用;
        BigDecimal merSingleFeeProfit = terMerFee.subtract(totalSingleFee);
        //手续费利润总和
        BigDecimal feeProfit = ( null == mwt.getFeeProfit() ? merSingleFeeProfit :  mwt.getFeeProfit().add(merSingleFeeProfit) );
        //保证金尚未考虑
        //................
        //总可用余额
        BigDecimal totalBalance =( null == mwt.getTotalBalance() ? inAmount : mwt.getTotalBalance().add(inAmount) );
       //总不可用余额
        BigDecimal totalUnavailableAmount = ( null == mwt.getTotalUnavailableAmount() ? new BigDecimal(0) : mwt.getTotalUnavailableAmount() );
        //总可用余额
        BigDecimal totalAvailableAmount = ( null== mwt.getTotalAvailableAmount() ?  new BigDecimal(0) : mwt.getTotalAvailableAmount() );
        //判断结算周期
        if( !mrt.getSettleCycle().equalsIgnoreCase("d0")  || !mrt.getSettleCycle().equalsIgnoreCase("t0")){
            totalUnavailableAmount = totalUnavailableAmount.add(inAmount);
        }else{
            totalAvailableAmount = totalAvailableAmount.add(inAmount);
        }
        //商户钱包
        mwt.setId(  null == mwt.getId() ? System.currentTimeMillis() : mrt.getId() )
                .setMerchantId( poi.getMerchantId())
                .setTotalAmount(totalAmount)//总订单金额
                .setIncomeAmount(totalIncomeAmount)  //总入账金额
                .setOutAmount(mwt.getOutAmount())//总出帐金额
                .setTotalFee(totalFee)//总手续费用
                .setFeeProfit(feeProfit) //手续费利润总和
                .setTotalMargin(mwt.getTotalMargin()) //保证金尚未考虑
                .setTotalBalance(totalBalance)
                .setTotalAvailableAmount( totalAvailableAmount )
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalFreezeAmount(mwt.getTotalFreezeAmount())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime( null == mwt.getCreateTime() ? new Date() : mwt.getCreateTime())
                .setUpdateTime(new Date());
        //创建商户钱包明细
        MerchantsDetailsTable mdt = new MerchantsDetailsTable()
                .setId(System.currentTimeMillis())
                .setMerchantId(poi.getMerchantId())
                .setProductId(poi.getProductId())
                .setMerOrderId(poi.getMerOrderId())
                .setPlatformOrderId(poi.getPlatformOrderId())
                .setAmount(poi.getAmount())
                .setInAmount(inAmount)
                .setOutAmount(new BigDecimal(0))
                .setRateFee(poi.getPayFee())
                .setFee(totalSingleFee)
                .setFeeProfit(merSingleFeeProfit)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(poi.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return new Tuple2<>(mwt,mdt);
    }

    @Override
    public TerminalMerchantsWalletTable getTerMerWallet(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getTerMerWallet(InnerPrintLogObject ipo)";
        TerminalMerchantsWalletTable tmw = null;
        try{
            tmw = commonRPCComponent.apiTerminalMerchantsWalletService.getOne(new TerminalMerchantsWalletTable()
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询终端商户钱包信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }

        return null == tmw ? new TerminalMerchantsWalletTable() : tmw ;
    }

    @Override
    public Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> updateTerMerWallet(TerminalMerchantsWalletTable tmw, PayOrderInfoTable poi, MerchantRateTable mrt){
        //订单金额
        BigDecimal amount = poi.getAmount();
        //订单总金额
        BigDecimal totalAmount = (null == tmw.getTotalAmount() ? amount : tmw.getTotalAmount().add(amount) );
        //手续费率
        BigDecimal payFee = poi.getPayFee();
        //手续费
        BigDecimal terMerFee = amount.multiply(payFee).setScale(2, BigDecimal.ROUND_UP );
        //总手续费
        BigDecimal totalTerMerFee = ( null == tmw.getTotalFee() ?  terMerFee : tmw.getTotalFee().add(terMerFee));
        //入账金额
        BigDecimal inAmount = amount.subtract(terMerFee);
        //入账总金额
        BigDecimal TotalIncomeAmount = ( null == tmw.getIncomeAmount() ? inAmount :
                tmw.getIncomeAmount().add(inAmount) );
        //总余额
        BigDecimal totalBalance = (null == tmw.getTotalBalance() ? inAmount : tmw.getTotalBalance().add(inAmount) );
        //总不可用余额
        BigDecimal totalUnavailableAmount = ( null == tmw.getTotalUnavailableAmount() ? new BigDecimal(0) : tmw.getTotalUnavailableAmount() );
        //总可用余额
        BigDecimal totalAvailableAmount = ( null== tmw.getTotalAvailableAmount() ?  new BigDecimal(0) : tmw.getTotalAvailableAmount() );
        //判断结算周期
        if( !mrt.getSettleCycle().equalsIgnoreCase("d0")  || !mrt.getSettleCycle().equalsIgnoreCase("t0")){
            totalUnavailableAmount = totalUnavailableAmount.add(inAmount);
        }else{
            totalAvailableAmount = totalAvailableAmount.add(inAmount);
        }
        //钱包
        tmw.setId( null == tmw.getId() ? System.currentTimeMillis() : tmw.getId())
                .setMerchantId(poi.getMerchantId())
                .setTerminalMerId(poi.getTerminalMerId())
                .setTotalAmount(totalAmount) //总订单金额
                .setIncomeAmount(TotalIncomeAmount)   //入账总金额
                .setOutAmount(tmw.getOutAmount())//总出帐金额
                .setTotalBalance(totalBalance)//总余额
                .setTotalAvailableAmount(totalAvailableAmount)
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalFee(totalTerMerFee)
                .setTotalMargin(tmw.getTotalMargin())//保证金尚未考虑
                .setTotalFreezeAmount(tmw.getTotalFreezeAmount())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime( null == tmw.getCreateTime() ? new Date() : tmw.getCreateTime() )
                .setUpdateTime(new Date());
        //钱包明细
        TerminalMerchantsDetailsTable tmd = new TerminalMerchantsDetailsTable()
                .setId(System.currentTimeMillis())
                .setMerchantId(poi.getMerchantId())
                .setTerminalMerId(poi.getTerminalMerId())
                .setProductId(poi.getProductId())
                .setMerOrderId(poi.getMerOrderId())
                .setPlatformOrderId(poi.getPlatformOrderId())
                .setAmount(poi.getAmount())
                .setInAmount(inAmount)
                .setOutAmount(new BigDecimal(0))
                .setRateFee(payFee)
                .setFee(terMerFee)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(poi.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return new Tuple2<>(tmw,tmd);
    }

    @Override
    public ChannelWalletTable getChanWallet(String channelId,InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChanWallet(String channelId, PayOrderInfoTable poi)";
        ChannelWalletTable cwt = null;
        try{
            cwt = commonRPCComponent.apiChannelWalletService.getOne(new ChannelWalletTable().setChannelId(channelId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询通道钱包信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }

        return null==cwt ? new ChannelWalletTable() : cwt ;
    }

    @Override
    public ChannelInfoTable getChannelInfo(String channelId, InnerPrintLogObject ipo) {
        final String localPoint=" getChannelInfo(String channelId, InnerPrintLogObject ipo)";
        ChannelInfoTable cit = null;
        try{
            cit = commonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable().setChannelId(channelId));
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }


}
