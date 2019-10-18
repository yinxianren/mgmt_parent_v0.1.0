package com.rxh.strategy;

import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.service.square.RiskQuotaDataService;
import com.rxh.square.pojo.MerchantQuotaRisk;
import com.rxh.square.pojo.RiskQuotaData;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class AbstractMerchantRiskManagement implements PayUtil, PayAssert {

    @Autowired
    protected RiskQuotaDataService riskQuotaDataService;
    @Autowired
    protected PaymentRecordSquareService paymentRecordSquareService;
    /**
     *  执行商户风控
     * @param merchantQuotaRisk
     * @param tradeObjectSquare
     *
     */
    public void riskManagement(MerchantQuotaRisk merchantQuotaRisk,
                                         TradeObjectSquare tradeObjectSquare) throws PayException {
        String merId=tradeObjectSquare.getMerId();
        String merOrderId = tradeObjectSquare.getMerOrderId();
        //1.判断单笔风控
        BigDecimal singleQuotaAmount=merchantQuotaRisk.getSingleQuotaAmount();
        BigDecimal trnasOrderAmount=tradeObjectSquare.getAmount();
        state( !(trnasOrderAmount.compareTo(singleQuotaAmount)>0),format("【商户风控】商户号：%s,订单号：%s：该商户单笔金额超过单笔最大额度！",merId,merOrderId),"E");
        //获取该商户累计交易额
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdfDay.format(new Date());
        SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
        String month = sdfMonth.format(new Date());
        RiskQuotaData riskQuotaData=new RiskQuotaData();
        riskQuotaData.setTradeTimeList(Arrays.asList(day,month));
        riskQuotaData.setRefId(merId);
        riskQuotaData.setRefType((short)2);// 1:通道 2:商户
        List<RiskQuotaData> riskQuotaDataList= riskQuotaDataService.getRiskQuotaDataByWhereCondition(riskQuotaData);
        //没有数据则认为是初始化阶段
        if( null !=riskQuotaDataList && riskQuotaDataList.size()==0) return;
        //2.判断该商户当日额度是否超额
        BigDecimal dayQuotaAmount= merchantQuotaRisk.getDayQuotaAmount();
        //获取历史交易额度
        BigDecimal riskDayQuotaAmount=riskQuotaDataList.stream()
                .filter(t->t.getTradeTime().equalsIgnoreCase(day))
                .map(RiskQuotaData::getAmount)
                .findFirst().orElse(new BigDecimal(0));
        //加上当前金额
        BigDecimal totalRiskQuotaAmount=riskDayQuotaAmount.add(trnasOrderAmount);
        state(!(totalRiskQuotaAmount.compareTo(dayQuotaAmount)>0),format("【商户风控】商户号：%s,订单号：%s：该商户交易金额超过单日金额最大额度！",merId,merOrderId));
        //3.判断该商户当月额度是否超额
        BigDecimal monthQuotaAmount= merchantQuotaRisk.getMonthQuotaAmount();
        BigDecimal riskMonthQuotaAmount=riskQuotaDataList.stream()
                .filter(t->t.getTradeTime().equalsIgnoreCase(month))
                .map(RiskQuotaData::getAmount)
                .findFirst().orElse(new BigDecimal(0));
        BigDecimal totalRiskMonthQuotaAmount=riskMonthQuotaAmount.add(trnasOrderAmount);
        state(!(totalRiskMonthQuotaAmount.compareTo(monthQuotaAmount)>0),format("【商户风控】商户号：%s,订单号：%s：该商户交易金额超过本月金额最大额度！",merId,merOrderId));
    }

}
