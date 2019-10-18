package com.rxh.service.oldKuaijie;

import com.rxh.cache.ehcache.RiskQuotaDataCache;
import com.rxh.exception.PayException;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.square.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GetOrderRiskQuotaDataService {

    private final PaymentRecordSquareService paymentRecordSquareService;
    private final RiskQuotaDataCache riskQuotaDataCache;

    @Autowired
    public GetOrderRiskQuotaDataService(PaymentRecordSquareService paymentRecordSquareService, RiskQuotaDataCache riskQuotaDataCache) {
        this.paymentRecordSquareService = paymentRecordSquareService;
        this.riskQuotaDataCache = riskQuotaDataCache;
    }

    public void analysis(ChannelInfo channelInfo, PayOrder payOrder, MerchantQuotaRisk merchantQuotaRisk) throws PayException {
        List<RiskQuotaData> quotaDataList = getRiskQuotaData(channelInfo, payOrder);
        insertRiskQuotaDataNew(quotaDataList,channelInfo,merchantQuotaRisk);
        quotaTodoSquare(payOrder,quotaDataList,channelInfo,merchantQuotaRisk);
    }

    private int insertRiskQuotaDataNew(List<RiskQuotaData> quotaDataList, ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk) {
        List<String> typeList = new ArrayList<>();
        typeList.add("1-1");
        typeList.add("1-2");
        // typeList.add("1-4");
        typeList.add("2-1");
        typeList.add("2-2");
        // typeList.add("2-4");
        if (quotaDataList.size()<=0 || quotaDataList == null){
            List<RiskQuotaData> riskQuotaDataList = new ArrayList<>();
            initRisk(channelInfo, merchantQuotaRisk, typeList, riskQuotaDataList);
            paymentRecordSquareService.insertRiskQuotaData(riskQuotaDataList);
            return 1;
        }
        // 不为空
        List<String> riskList = new ArrayList<>();
        for (RiskQuotaData riskQuotaData : quotaDataList){
            riskList.add(riskQuotaData.getRefType()+"-"+riskQuotaData.getType());
        }
        List<String> noriskList = new ArrayList<>();
        for (String str : typeList){
            if (!riskList.contains(str)){
                noriskList.add(str);
            }
        }
        if (noriskList.size()>0){
            List<RiskQuotaData> riskQuotaDataList = new ArrayList<>();
            initRisk(channelInfo, merchantQuotaRisk, noriskList, riskQuotaDataList);
            paymentRecordSquareService.insertRiskQuotaData(riskQuotaDataList);
            return 1;
        }
        return 1;
    }

    private void initRisk(ChannelInfo channelInfo, MerchantQuotaRisk merchantQuotaRisk, List<String> noriskList, List<RiskQuotaData> riskQuotaDataList) {
        for (String ss : noriskList){
            String[] ssArray = ss.split("-");
            Short refType = Short.parseShort(ssArray[0]);
            Short type = Short.parseShort(ssArray[1]);
            RiskQuotaData riskQuotaData = new RiskQuotaData();
            riskQuotaData.setAmount(new BigDecimal(0));
            riskQuotaData.setType(type);
            if (type == 1){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String day = sdf.format(new Date());
                riskQuotaData.setTradeTime(day);
            }else if (type == 2){
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                String month = sdf1.format(new Date());
                riskQuotaData.setTradeTime(month);
            }else  {
                break;
            }
            // if (type == 4){
            //     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH");
            //     String db = sdf1.format(new Date());
            //     riskQuotaData.setTradeTime(db);
            // }
            switch (refType){
                case 1: // 通道
                    riskQuotaData.setRefId(channelInfo.getChannelId());
                    riskQuotaData.setRefType(refType);
                    riskQuotaDataList.add(riskQuotaData);
                    break;
                case 2: // 商户
                    riskQuotaData.setRefId(merchantQuotaRisk.getMerId());
                    riskQuotaData.setRefType(refType);
                    riskQuotaDataList.add(riskQuotaData);
                    break;
                default:
                    break;
            }
        }
    }

    List<RiskQuotaData> getRiskQuotaData(ChannelInfo channelInfo,PayOrder payOrder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        String month = sdf1.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        String year = sdf2.format(new Date());
        return riskQuotaDataCache.getQuotaData1(payOrder.getMerId(),channelInfo.getChannelId(),day,month,year);
    }

    private void quotaTodoSquare(PayOrder payOrder, List<RiskQuotaData> quotaDataList,ChannelInfo channelInfo,MerchantQuotaRisk merchantQuotaRisk) throws  PayException{
        // 当前交易人民币金额
        BigDecimal rmbAmount = payOrder.getAmount()!= null ? payOrder.getAmount():new BigDecimal(0);
        // 限定日最小额度
        BigDecimal singleMinAmount = channelInfo.getSingleMinAmount()!= null ? channelInfo.getSingleMinAmount():new BigDecimal(0);
        // 限定日最大额度
        BigDecimal getSingleMaxAmount = channelInfo.getSingleMaxAmount() != null? channelInfo.getSingleMaxAmount():new BigDecimal(0);
        if (isExceed(singleMinAmount, rmbAmount)) {
            throw new PayException("不足通道单笔最小限额：" + singleMinAmount + "，当前交易人民币金额：" + rmbAmount, 3004);
        }
        if (isExceed(rmbAmount, getSingleMaxAmount)) {
            throw new PayException("超出通道单笔最大限额：" + getSingleMaxAmount + "，当前交易人民币金额：" + rmbAmount, 3005);
        }
        // 限定日最小额度
        BigDecimal singleQuotaAmount = merchantQuotaRisk.getSingleQuotaAmount()!= null ? merchantQuotaRisk.getSingleQuotaAmount(): new BigDecimal(0);
        if (isExceed(rmbAmount, singleQuotaAmount)) {
            throw new PayException("超出商户单笔最大限额：" + singleQuotaAmount + "，当前交易人民币金额：" + rmbAmount, 3005);
        }
        if (quotaDataList == null) {
            return;
        }

        // 单笔额度
        for (RiskQuotaData riskQuotaData : quotaDataList){
            // 通道
            if(riskQuotaData.getRefType() == RiskQuotaData.CHANNEL_REF_TYPE && riskQuotaData.getType() != RiskQuotaData.LIMIT_TYPE_ORDER_AMOUNT){
                switch (riskQuotaData.getType()) {
                    case 1:
                        if(isExceed(riskQuotaData.getAmount()!=null?riskQuotaData.getAmount().add(rmbAmount):rmbAmount, channelInfo.getDayQuotaAmount()!= null ? channelInfo.getDayQuotaAmount(): new BigDecimal(0))) {
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(channelInfo.getDayQuotaAmount());
                            throw new PayException("通道每日最大限额：" + channelInfo.getDayQuotaAmount() + "，超出：" + excessAmount, 3006);
                        }
                    case 2:
                        if(isExceed(riskQuotaData.getAmount() !=null?riskQuotaData.getAmount().add(rmbAmount):rmbAmount, channelInfo.getMonthQuotaAmount() != null ? channelInfo.getMonthQuotaAmount(): new BigDecimal(0))) {
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(channelInfo.getMonthQuotaAmount());
                            throw new PayException("通道每月最大限额：" + channelInfo.getMonthQuotaAmount()+ "，超出：" + excessAmount, 3007);
                        }
                    default:
                        break;
                }
            }
            // 商户
            if(riskQuotaData.getRefType() == RiskQuotaData.MERCHANT_REF_TYPE && riskQuotaData.getType() != RiskQuotaData.LIMIT_TYPE_ORDER_AMOUNT){
                switch (riskQuotaData.getType()) {
                    case 1:
                        if(isExceed(riskQuotaData.getAmount()!=null?riskQuotaData.getAmount().add(rmbAmount):rmbAmount, merchantQuotaRisk.getDayQuotaAmount()!= null ? merchantQuotaRisk.getDayQuotaAmount(): new BigDecimal(0))){
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(merchantQuotaRisk.getDayQuotaAmount());
                            throw new PayException("商户每日最大限额：" + merchantQuotaRisk.getDayQuotaAmount() + "，超出：" + excessAmount, 3006);
                        }
                    case 2:
                        if(isExceed(riskQuotaData.getAmount()!=null?riskQuotaData.getAmount().add(rmbAmount):rmbAmount, merchantQuotaRisk.getMonthQuotaAmount() != null ? merchantQuotaRisk.getMonthQuotaAmount(): new BigDecimal(0))){
                            // 超出限定额度的值
                            BigDecimal excessAmount = riskQuotaData.getAmount().add(rmbAmount).subtract(merchantQuotaRisk.getMonthQuotaAmount());
                            throw new PayException("商户每月最大限额：" + merchantQuotaRisk.getMonthQuotaAmount()+ "，超出：" + excessAmount, 3007);
                        }
                    default:
                        break;
                }
            }
        }
    }

    private boolean isExceed(BigDecimal amount, BigDecimal target) {
        return amount.compareTo(target) > 0;
    }

    void updateRiskQuotaData(PayOrder payOrder, ChannelInfo channelInfo,MerchantQuotaRisk merchantQuotaRisk) throws  PayException{
        List<RiskQuotaData> quotaDataList = getRiskQuotaData(channelInfo, payOrder);
        // quotaTodoSquare(transOrder,quotaDataList,channelInfo,merchantQuotaRisk);
        quotaDataList.forEach(riskQuotaData -> {
            // if (StringUtils.equals(riskQuotaData.getType().toStringquotaTodoSquare(), transOrder.getPayType())) {
            riskQuotaData.setAmount(riskQuotaData.getAmount() == null ? payOrder.getAmount() : riskQuotaData.getAmount().add(payOrder.getAmount()));
            // }
        });
        paymentRecordSquareService.insertOrUpdateRiskQuotaData(quotaDataList);
    }
}
