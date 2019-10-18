package com.rxh.strategy;

import com.rxh.exception.PayException;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.service.square.RiskQuotaDataService;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.ChannelWallet;
import com.rxh.square.pojo.RiskQuotaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *   通道选择策略，最优选择策略
 * @author  panda
 * @date 20190727
 *
 */
@Component
public class OptimalChannelStrategy extends MerchantRiskManagement {

    @Autowired
    private RiskQuotaDataService riskQuotaDataService;
    @Autowired
    private PaymentRecordSquareService paymentRecordSquareService;


    /**
     *  执行通道风控并获取一个最优通道
     * @param channelInfoList
     * @param tradeObjectSquare
     */
    public  ChannelInfo  strategy(List<ChannelInfo> channelInfoList, TradeObjectSquare tradeObjectSquare) throws PayException {
        String merId=tradeObjectSquare.getMerId();
        String merOrderId = tradeObjectSquare.getMerOrderId();

        BigDecimal  tranOrderAmount=tradeObjectSquare.getAmount();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdfDay.format(new Date());
        SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
        String month = sdfMonth.format(new Date());

        //01.筛选符合单笔交易金额的通道
        List<ChannelInfo> channelInfoList_minContiditon= channelInfoList.stream()
                .filter(t->t.getSingleMaxAmount().compareTo(tranOrderAmount)>0)
                .distinct()
                .collect(Collectors.toList());
        isNotElement(channelInfoList_minContiditon,format("【通道筛选】商户号：%s,订单号：%s：单笔额度超出所有通道的最大单笔额度！",merId,merOrderId));
        List<String> channelIdList = channelInfoList_minContiditon.stream()
                .map(ChannelInfo::getChannelId)
                .distinct()
                .collect(Collectors.toList());
        //获取符合通道记录额度
        RiskQuotaData riskQuotaData=new RiskQuotaData();
        riskQuotaData.setTradeTimeList(Arrays.asList(day,month));
        riskQuotaData.setRefIdList(channelIdList);
        riskQuotaData.setRefType((short)1);// 1:通道 2:商户
        List<RiskQuotaData> riskQuotaDataList= riskQuotaDataService.getRiskQuotaDataByWhereCondition(riskQuotaData);
        List<ChannelInfo> channelInfoList_secondaryChannel=null;
        //执行刷选策略
        if(null !=riskQuotaDataList && riskQuotaDataList.size() != 0 ) {
            //02.获取单日未超额的通道
            List<ChannelInfo> channelInfoList_day = new ArrayList<>();
            List<RiskQuotaData> dayRiskQuotaDataList = riskQuotaDataList.stream()
                    .filter(riskQuota ->
                            riskQuota.getTradeTime().equalsIgnoreCase(day))
                    .distinct()
                    .collect(Collectors.toList());

            channelInfoList_minContiditon.forEach(channelInfo -> {
                dayRiskQuotaDataList.forEach(dayRiskQuotaData -> {
                    //把当前代付的金额也算进去
                    if ((channelInfo.getDayQuotaAmount()).compareTo((dayRiskQuotaData.getAmount().add(tranOrderAmount))) > 0) {
                        channelInfoList_day.add(channelInfo);
                    }
                });
            });
            isNotElement(channelInfoList_day, format("【通道筛选】商户号：%s,订单号：%s：单笔额度超出所有通道的最大当日额度！", merId, merOrderId));
            //03.获取单月未超额的通道
            List<ChannelInfo> channelInfoList_month = new ArrayList<>();
            List<RiskQuotaData> monthRiskQuotaDataList = riskQuotaDataList.stream()
                    .filter(riskQuota ->
                            riskQuota.getTradeTime().equalsIgnoreCase(month))
                    .distinct()
                    .collect(Collectors.toList());
            channelInfoList_minContiditon.forEach(channelInfo -> {
                monthRiskQuotaDataList.forEach(monthRiskQuotaData -> {
                    //把当前代付的金额也算进去
                    if ((channelInfo.getMonthQuotaAmount()).compareTo((monthRiskQuotaData.getAmount().add(tranOrderAmount))) > 0) {
                        channelInfoList_month.add(channelInfo);
                    }
                });
            });
            isNotElement(channelInfoList_month, format("【通道筛选】商户号：%s,订单号：%s：单笔金额超出所有通道当月限额的额度！", merId, merOrderId));
            //04.同时满足单日和当月都额度的通道
            List<ChannelInfo> channelInfoList_twoCondition=new ArrayList<>();
            channelInfoList_day.forEach(dayChannelInfo->{
                channelInfoList_month.forEach(monthChannelInfo->{
                    if(dayChannelInfo.getChannelId().equals(monthChannelInfo.getChannelId())){
                        channelInfoList_twoCondition.add(dayChannelInfo);
                    }
                });
            });
            isNotElement(channelInfoList_twoCondition, format("【通道筛选】商户号：%s,订单号：%s：没有符合通道可使用！", merId, merOrderId));
            channelInfoList_secondaryChannel=channelInfoList_twoCondition;
        }else {
            channelInfoList_secondaryChannel=channelInfoList;
        }
        isNotElement(channelInfoList_secondaryChannel,format("【通道筛选】商户号：%s,订单号：%s：没有符合通道可使用！", merId, merOrderId));
        //04-1  获取通道钱包
        //获取通道id
        List<String>  channelInfoList_secondaryChannel_channelId=channelInfoList_secondaryChannel.stream()
                .map(ChannelInfo::getChannelId)
                .distinct()
                .collect(Collectors.toList());
        isNotElement(channelInfoList_secondaryChannel_channelId,format("【通道筛选】商户号：%s,订单号：%s：没有可用的通道ID！", merId, merOrderId));
        //获取满足条件的通道钱包
        List<ChannelWallet>  channelWalletList=paymentRecordSquareService.getChannelWalletByIds(channelInfoList_secondaryChannel_channelId);
        isNotElement(channelWalletList,format("【通道筛选】商户号：%s,订单号：%s：通道钱包异常，无法获取到通道钱包信息！", merId, merOrderId));
        //筛选出通道钱包有足够的资金来进行代付的钱包
        List<String> secondaryChannelIds=channelWalletList.stream()
                .filter(t->(t.getTotalAvailableAmount().compareTo(tranOrderAmount))>=0)
                .map(ChannelWallet::getChannelId)
                .collect(Collectors.toList());
        isNotElement(secondaryChannelIds,format("【通道筛选】商户号：%s,订单号：%s：通道剩余金额无法完成本次代付！", merId, merOrderId));
        //对通道进行筛选
        List<ChannelInfo> channelInfoList_firstChannel=new ArrayList<>();
        channelInfoList_secondaryChannel.forEach( secondary->{
            secondaryChannelIds.forEach(ids->{
                if(ids.equals( secondary.getChannelId())){
                    channelInfoList_firstChannel.add(secondary);
                }
            });
        });
        //05.获取手续最低的通道
        BigDecimal minRateFee= channelInfoList_firstChannel.stream()
                .map(ChannelInfo::getChannelRateFee)
                .sorted()
                .findFirst().orElse(null) ;
        ChannelInfo channelInfo_minRateFee=channelInfoList_firstChannel.stream()
                .filter(t->t.getChannelRateFee().compareTo(minRateFee) == 0)
                .distinct()
                .findFirst()
                .orElse(null);
        isNull(channelInfo_minRateFee,format("【通道筛选】商户号：%s,订单号：%s：筛选最低费率通道异常！", merId, merOrderId));
        //06.获取单笔最小费用的通道
        BigDecimal minSingleFee=channelInfoList_firstChannel.stream()
                .map(ChannelInfo::getChannelSingleFee)
                .sorted()
                .findFirst().orElse(null);
        ChannelInfo channelInfo_minSingleFee=channelInfoList_firstChannel.stream()
                .filter(t->t.getChannelSingleFee().compareTo(minSingleFee) == 0)
                .distinct()
                .findFirst()
                .orElse(null);
        isNull(channelInfo_minSingleFee,format("【通道筛选】商户号：%s,订单号：%s：筛选单笔最大费用通道异常！", merId, merOrderId));

        //07.获取一个最优通道
        if(channelInfo_minRateFee.getChannelId().equals(channelInfo_minSingleFee.getChannelId()))
            return channelInfo_minSingleFee; //两个都可以
        else
            return channelInfo_minRateFee;//选择费率最低的通道
    }

}
