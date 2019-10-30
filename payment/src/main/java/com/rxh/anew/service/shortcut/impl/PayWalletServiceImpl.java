package com.rxh.anew.service.shortcut.impl;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.service.PayWalletService;
import com.rxh.anew.table.agent.AgentMerchantSettingTable;
import com.rxh.anew.table.agent.AgentMerchantWalletTable;
import com.rxh.anew.table.agent.AgentMerchantsDetailsTable;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.channel.ChannelDetailsTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.channel.ChannelWalletTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
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
    public ChannelInfoTable getChannelInfo(String channelId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint=" getChannelInfo(String channelId, InnerPrintLogObject ipo)";
        ChannelInfoTable cit = null;
        try{
            cit = commonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable().setChannelId(channelId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询通道信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }
        isNull(cit,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：通道id（%s）不存在，或者通道被禁用",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint,channelId),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg()));
        return cit;
    }


    @Override
    public AgentMerchantSettingTable getAgentMerSet(String agentMerchantId, String  productId,InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getAgentMerSet(String agentMerchantId, InnerPrintLogObject ipo)";
        AgentMerchantSettingTable ams = null;
        try{
            ams = commonRPCComponent.apiAgentMerchantSettingService.getOne(new AgentMerchantSettingTable()
                    .setAgentMerchantId(agentMerchantId)
                    .setProductId(productId)
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询代理设置信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }
        return ams;
    }

    @Override
    public AgentMerchantWalletTable getAgentMerWallet(String agentMerchantId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getAgentMerWallet(String agentMerchantId, InnerPrintLogObject ipo)";
        AgentMerchantWalletTable amw = null ;
        try{
            amw = commonRPCComponent.apiAgentMerchantWalletService.getOne(new AgentMerchantWalletTable().setAgentMerchantId(agentMerchantId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询代理钱包信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }
        return null == amw ? new AgentMerchantWalletTable() : amw ;
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
    public Tuple2<MerchantWalletTable, MerchantsDetailsTable> updateMerWallet(MerchantWalletTable mwt, PayOrderInfoTable poi, MerchantRateTable mrt) {
        //订单金额
        BigDecimal amount = poi.getAmount();
        //总订单金额
        BigDecimal totalAmount = ( null == mwt.getTotalAmount() ?  amount :  mwt.getTotalAmount().add(amount) );
        BigDecimal rateFee = (null == mrt.getRateFee() ? new BigDecimal(0) : mrt.getRateFee().divide(new BigDecimal(100)) );
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
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return new Tuple2<>(mwt,mdt);
    }



    @Override
    public Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> updateTerMerWallet(TerminalMerchantsWalletTable tmw, PayOrderInfoTable poi, MerchantRateTable mrt){
        //订单金额
        BigDecimal amount = poi.getAmount();
        //订单总金额
        BigDecimal totalAmount = (null == tmw.getTotalAmount() ? amount : tmw.getTotalAmount().add(amount) );
        //手续费率
        BigDecimal payFee = poi.getPayFee().divide(new BigDecimal(100));
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
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return new Tuple2<>(tmw,tmd);
    }


    @Override
    public Tuple2<ChannelWalletTable, ChannelDetailsTable> updateChannelWallet(ChannelWalletTable cwt, ChannelInfoTable cit, PayOrderInfoTable poi,MerchantRateTable mrt) {
        //订单金额
        BigDecimal amount = poi.getAmount();
        //通道费率
        BigDecimal chanRateFee = cit.getChannelRateFee().divide(new BigDecimal(100));
        BigDecimal singleFee = cit.getChannelSingleFee();
        //通道费用
        BigDecimal chanFee = amount.multiply(chanRateFee).setScale(2,BigDecimal.ROUND_UP);
        chanFee = chanFee.add(singleFee);
        //入账金额
        BigDecimal inAmount = amount.subtract(amount);
        //总入帐金额
        BigDecimal totalInAmount = ( null == cwt.getIncomeAmount() ? inAmount : cwt.getIncomeAmount().add(inAmount) );
        //总订单金额
        BigDecimal totalAmount = ( null == cwt.getTotalAmount() ? amount : cwt.getTotalAmount().add(amount) );
        //总手续费
        BigDecimal totalFee = ( null == cwt.getTotalFee() ? chanFee : cwt.getTotalFee().add(chanFee) );
        //计算商户费用
        BigDecimal merRateFee = (null == mrt.getRateFee() ? new BigDecimal(0) : mrt.getRateFee().divide(new BigDecimal(100)) );
        BigDecimal merSingleFee = (null == mrt.getSingleFee() ? new BigDecimal(0) : mrt.getSingleFee() );
        BigDecimal merFee = ( totalAmount.multiply(merRateFee).setScale(2, BigDecimal.ROUND_UP ) .add(merSingleFee)) ;
        //通道利润 = 商户的费用 - 通道费用
        BigDecimal chanProfit = merFee.subtract(chanFee);
        //总余额
        BigDecimal totalBalance = (null == cwt.getTotalBalance() ? inAmount : cwt.getTotalBalance().add(inAmount));
        //总不可用余额
        BigDecimal totalUnavailableAmount = ( null == cwt.getTotalUnavailableAmount() ? new BigDecimal(0) : cwt.getTotalUnavailableAmount() );
        //总可用余额
        BigDecimal totalAvailableAmount = ( null== cwt.getTotalAvailableAmount() ?  new BigDecimal(0) : cwt.getTotalAvailableAmount() );
        //判断结算周期
        if( !cit.getSettleCycle().equalsIgnoreCase("d0")  || !cit.getSettleCycle().equalsIgnoreCase("t0")){
            totalUnavailableAmount = totalUnavailableAmount.add(inAmount);
        }else{
            totalAvailableAmount = totalAvailableAmount.add(inAmount);
        }
        //通道钱包
        cwt.setId( null == cwt.getId() ? System.currentTimeMillis() : cwt.getId())
                .setChannelId( null == cwt.getChannelId() ? poi.getChannelId() : cwt.getChannelId())
                .setOrganizationId( null == cwt.getOrganizationId() ? cit.getOrganizationId() : cwt.getOrganizationId())
                .setProductId( null == cwt.getProductId() ? poi.getProductId() : cwt.getProductId())
                .setTotalAmount(totalAmount)
                .setIncomeAmount(totalInAmount)
                .setOutAmount(cwt.getOutAmount())
                .setTotalFee(totalFee)
                .setFeeProfit(chanProfit)
                .setTotalBalance(totalBalance)
                .setTotalAvailableAmount(totalAvailableAmount)
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalMargin(cwt.getTotalMargin())
                .setTotalFreezeAmount(cwt.getTotalFreezeAmount())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(  null == cwt.getCreateTime() ? new Date() : cwt.getCreateTime())
                .setUpdateTime(new Date());
        //通道钱包明细
        ChannelDetailsTable cdt = new ChannelDetailsTable()
                .setId(System.currentTimeMillis())
                .setChannelId(poi.getChannelId())
                .setOrganizationId(cit.getOrganizationId())
                .setProductId(poi.getProductId())
                .setMerOrderId(poi.getMerOrderId())
                .setPlatformOrderId(poi.getPlatformOrderId())
                .setAmount(poi.getAmount())
                .setInAmount(inAmount)
                .setOutAmount(new BigDecimal(0))
                .setRateFee(cit.getChannelRateFee())
                .setFee(chanFee)
                .setFeeProfit(chanProfit)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return  new Tuple2<>(cwt,cdt);
    }


    @Override
    public Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> updateAgentMerWallet(AgentMerchantWalletTable amw, AgentMerchantSettingTable ams, PayOrderInfoTable poi) {
        //订单金额
        BigDecimal amount = poi.getAmount();
        //总订单金额
        BigDecimal totalAmount = ( null == amw.getTotalAmount() ? amount : amw.getTotalAmount().add(amount) );
        BigDecimal payFee = poi.getPayFee().divide(new BigDecimal(100));
        //订单入账总金额
        BigDecimal inAmount = amount.multiply(payFee).setScale(2,BigDecimal.ROUND_UP);
        BigDecimal incomeAmount = ( null == amw.getIncomeAmount() ? inAmount : amw.getIncomeAmount().add(inAmount) );
        //代理费率
        BigDecimal rateFee = ( null == ams.getRateFee() ? new BigDecimal(0) :  ams.getRateFee().divide(new BigDecimal(100)) );
        BigDecimal singleFee = ams.getSingleFee();
        //代理商手续费
        BigDecimal agentMerFee = amount.multiply(rateFee).setScale(2,BigDecimal.ROUND_UP);
        agentMerFee = agentMerFee.add(singleFee);
        //代理商总手续费入账
        BigDecimal totalFee = ( null == amw.getTotalFee() ? agentMerFee : amw.getTotalFee().add(agentMerFee) );
        //代理商总余额
        BigDecimal totalBalance = ( null == amw.getTotalAmount() ? agentMerFee : amw.getTotalAmount().add(agentMerFee) );
        //总不可用余额
        BigDecimal totalUnavailableAmount = ( null == amw.getTotalUnavailableAmount() ? new BigDecimal(0) : amw.getTotalUnavailableAmount() );
        //总可用余额
        BigDecimal totalAvailableAmount = ( null== amw.getTotalAvailableAmount() ?  new BigDecimal(0) : amw.getTotalAvailableAmount() );
        //判断结算周期
        if( !ams.getSettleCycle().equalsIgnoreCase("d0")  || !ams.getSettleCycle().equalsIgnoreCase("t0")){
            totalUnavailableAmount = totalUnavailableAmount.add(agentMerFee);
        }else{
            totalAvailableAmount = totalAvailableAmount.add(agentMerFee);
        }

        amw.setId( null == amw.getId() ? System.currentTimeMillis() : amw.getId())
        .setAgentMerchantId( null ==  amw.getAgentMerchantId() ?  ams.getAgentMerchantId() : amw.getAgentMerchantId() )
        .setTotalAmount(totalAmount)
        .setIncomeAmount(incomeAmount)//总入账金额,这里存放的是终端商户入账的金额
        .setOutAmount(amw.getOutAmount())//手续费出帐金额
        .setTotalBalance(totalBalance) //代理商总余额
        .setTotalAvailableAmount(totalAvailableAmount)
        .setTotalUnavailableAmount(totalUnavailableAmount)
        .setTotalFee(totalFee)
        .setTotalFreezeAmount(amw.getTotalFreezeAmount())
        .setStatus(StatusEnum._0.getStatus())
        .setCreateTime( null == amw.getCreateTime() ? new Date() : amw.getCreateTime() )
        .setUpdateTime(new Date());

        AgentMerchantsDetailsTable amd = new AgentMerchantsDetailsTable()
        .setId(System.currentTimeMillis())
        .setAgentMerchantId(ams.getAgentMerchantId())
        .setMerOrderId(poi.getMerOrderId())
        .setPlatformOrderId(poi.getPlatformOrderId())
        .setProductId(poi.getProductId())
        .setAmount(poi.getAmount())
        .setInAmount(incomeAmount)
        .setOutAmount(new BigDecimal(0))
        .setRateFee(ams.getRateFee())
        .setFee(agentMerFee)
        .setTotalBalance(totalBalance)
        .setTimestamp(System.currentTimeMillis())
        .setStatus(StatusEnum._0.getStatus())
        .setCreateTime(new Date())
        .setUpdateTime(new Date());
        return new Tuple2<>(amw,amd);
    }

}
