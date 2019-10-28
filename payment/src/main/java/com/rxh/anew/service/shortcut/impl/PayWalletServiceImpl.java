package com.rxh.anew.service.shortcut.impl;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.service.PayWalletService;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.anew.table.system.MerchantSettingTable;
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
    public MerchantRateTable getMerRate(String productId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerRate(String productId, InnerPrintLogObject ipo)";
        MerchantRateTable mrt = null;
        try{
            mrt = commonRPCComponent.apiMerchantRateService.getOne(new MerchantRateTable()
                    .setProductId(productId)
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
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00041.getMsg(),productId,localPoint),
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
//        BigDecimal totalFee

        mwt.setId(  null == mwt.getId() ? System.currentTimeMillis() : mrt.getId() )
                .setMerchantId( poi.getMerchantId())
                .setTotalAmount(totalAmount)//总订单金额
                .setIncomeAmount(totalIncomeAmount)  //总入账金额
                .setOutAmount(mwt.getOutAmount())//总出帐金额
//                .setTotalFee()//总手续费用
//                .setFeeProfit()
//                .setTotalMargin()
//                .setTotalBalance()
//                .setTotalAvailableAmount()
//                .setTotalUnavailableAmount()
//                .setTotalFreezeAmount()
//                .setStatus()
//                .setCreateTime()
                .setUpdateTime(new Date());

        return null;
    }


}
