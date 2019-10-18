package com.rxh.service.oldKuaijie;

import com.rxh.cache.ehcache.RiskQuotaDataCache;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.square.pojo.*;
import com.rxh.utils.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Service
public class GetOrderPayService {


    @Autowired
    private PaymentRecordSquareService paymentRecordSquareService;

    private final static Logger logger = LoggerFactory.getLogger(SweepCodePaymentService.class);
    private  final RecordPaymentSquareService recordPaymentSquareService;
    private  final GetOrderWalletService getOrderWalletService;
    private final SweepCodePaymentService sweepCodePaymentService;
    private final RiskQuotaDataCache riskQuotaDataCache;

    @Autowired
    public GetOrderPayService(RecordPaymentSquareService recordPaymentSquareService, GetOrderWalletService getOrderWalletService,  SweepCodePaymentService sweepCodePaymentService, RiskQuotaDataCache riskQuotaDataCache) {
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.getOrderWalletService = getOrderWalletService;
        this.sweepCodePaymentService = sweepCodePaymentService;
        this.riskQuotaDataCache = riskQuotaDataCache;
    }

    public String payment(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        SquareTrade trade = sweepCodePaymentService.getTrade(systemOrderTrack, tradeObjectSquare);
        BankResult bankResult = sweepCodePaymentService.toPay(trade);
        sweepCodePaymentService.afterPay(trade,bankResult);
        String returnJson=getReturnJson(trade,bankResult);

        return returnJson;
    }

    public void afterPay(SquareTrade trade, BankResult result) throws PayException {
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        PayOrder payOrder = trade.getPayOrder();
        ChannelInfo channelInfo = trade.getChannelInfo();
        MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(payOrder.getMerId());
        if(!Objects.equals(payOrder.getOrderStatus(), Integer.valueOf(result.getStatus())) &&result.getStatus()==0){
            getOrderWalletService.updateWallet(trade);
            this.updateRiskQuotaData(payOrder,channelInfo,merchantQuotaRisk);

        }
        payOrder.setOrderStatus(Integer.valueOf(result.getStatus()));
        recordPaymentSquareService.UpdatePayOrder(payOrder);


    }


    public void updateRiskQuotaData(PayOrder payOrder, ChannelInfo channelInfo,MerchantQuotaRisk merchantQuotaRisk) throws  PayException{
        List<RiskQuotaData> quotaDataList = getRiskQuotaData(channelInfo, payOrder);
        // quotaTodoSquare(payOrder,quotaDataList,channelInfo,merchantQuotaRisk);
        quotaDataList.forEach(riskQuotaData -> {
            // if (StringUtils.equals(riskQuotaData.getType().toString(), payOrder.getPayType())) {
            riskQuotaData.setAmount(riskQuotaData.getAmount() == null ? payOrder.getAmount() : riskQuotaData.getAmount().add(payOrder.getAmount()));
            // }
        });
        insertRiskQuotaData(quotaDataList);
    }

    public Integer insertRiskQuotaData(List<RiskQuotaData> quotaDataList) {
        return paymentRecordSquareService.insertOrUpdateRiskQuotaData(quotaDataList);
    }
    List<RiskQuotaData> getRiskQuotaData(ChannelInfo channelInfo, PayOrder payOrder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        String month = sdf1.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        String year = sdf2.format(new Date());
        return riskQuotaDataCache.getQuotaData1(payOrder.getMerId(),channelInfo.getChannelId(),day,month,year);
    }

    public String getReturnJson(SquareTrade trade, BankResult bankResult) {
        PayOrder payOrder = trade.getPayOrder();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = merchantInfo.getMerId();
        String merOrderId = trade.getMerOrderId();
        String currency = payOrder.getCurrency();
        String amount = payOrder.getAmount().toString();
        String transId = payOrder.getPayId();
        Short status = bankResult.getStatus();
        String secretKey = merchantInfo.getSecretKey();
        String md5Str=merId+merOrderId+currency+amount+transId+status+secretKey;
        md5Str= DigestUtils.md5Hex(md5Str);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("MerId",merId);
        resultMap.put("MerOrderId",merOrderId);
        resultMap.put("OrderId;",transId);
        resultMap.put("Amount;",amount);
        resultMap.put("TradeTime;",payOrder.getTradeTime());
        resultMap.put("Status;",status);
        resultMap.put("Msg;",bankResult.getBankResult());
        resultMap.put("SignMsg;",md5Str);
        String resultjson = JsonUtils.objectToJson(resultMap);
        return resultjson;
    }
}
