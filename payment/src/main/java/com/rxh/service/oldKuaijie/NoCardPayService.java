package com.rxh.service.oldKuaijie;

import com.rxh.cache.ehcache.*;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.*;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: hul
 * Date: 2019/5/25
 * Time: 10:30
 */
@Service
public class NoCardPayService {

    private final static Logger logger = LoggerFactory.getLogger(NoCardPayService.class);

    private  final RecordPaymentSquareService recordPaymentSquareService;
    private  final SweepCodePaymentService sweepCodePaymentService;
    private  final GetOrderWalletService getOrderWalletService;
    private final MerchantSquareRateCache merchantSquareRateCache;
    private final MerchantSquareSettingCache merchantSquareSettingCache;
    private final ChannelInfoSquareCache channelInfoSquareCache;
    private final OrganizationInfoSquareCache organizationInfoSquareCache;
    private final RiskQuotaDataCache riskQuotaDataCache;

    @Autowired
    public NoCardPayService(RecordPaymentSquareService recordPaymentSquareService, SweepCodePaymentService sweepCodePaymentService, GetOrderWalletService getOrderWalletService, MerchantSquareRateCache merchantSquareRateCache, MerchantSquareSettingCache merchantSquareSettingCache,
                            ChannelInfoSquareCache channelInfoSquareCache, OrganizationInfoSquareCache organizationInfoSquareCache, RiskQuotaDataCache riskQuotaDataCache) {
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.sweepCodePaymentService = sweepCodePaymentService;
        this.getOrderWalletService = getOrderWalletService;
        this.merchantSquareRateCache = merchantSquareRateCache;
        this.merchantSquareSettingCache = merchantSquareSettingCache;
        this.channelInfoSquareCache = channelInfoSquareCache;
        this.organizationInfoSquareCache = organizationInfoSquareCache;
        this.riskQuotaDataCache = riskQuotaDataCache;
    }

    /**
     * 四方代付，数据库插入
     * @param systemOrderTrack 风控前置对象
     * @param tradeObjectSquare 报文对象
     */
    public String payment(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        SquareTrade trade = sweepCodePaymentService.getTrade(systemOrderTrack, tradeObjectSquare);
        BankResult result= toPay(trade);
        afterPay(trade,result);
        String returnJson=getReturnJson(trade,result);
        return returnJson;
    }

    private BankResult toPay(SquareTrade trade)  throws PayException{
        logger.info(JsonUtils.objectToJsonNonNull(trade));
        // 发送请求至接口
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getChannelInfo().getPayUrl()+"trade", JsonUtils.objectToJsonNonNull(trade));
        // 处理接口返回信息
        if (StringUtils.isBlank(result)) {
            throw new PayException("Cross请求发生错误！URL：" + trade.getChannelInfo().getPayUrl(), 4001);
        }
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        if (bankResult != null) {
            return bankResult;
        } else {
            throw new PayException("BankResultJson转BankResult异常", 6000);
        }
    }

    private void afterPay(SquareTrade trade, BankResult result) {
        TransOrder transOrder = trade.getTransOrder();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        if(!Objects.equals(transOrder.getOrderStatus(), Integer.valueOf(result.getStatus())) &&result.getStatus()==0){
            getOrderWalletService.updateWallet(trade);
        }
        transOrder.setOrderStatus(Integer.valueOf(result.getStatus()));
        recordPaymentSquareService.UpdateTransOrder(transOrder);
    }
    private String getReturnJson(SquareTrade trade, BankResult result) {
        TransOrder transOrder = trade.getTransOrder();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = merchantInfo.getMerId();
        String merOrderId = trade.getMerOrderId();
        String amount = transOrder.getAmount().toString();
        String transId = transOrder.getTransId();
        Short status = result.getStatus();
        String secretKey = merchantInfo.getSecretKey();
        String md5Str=merId+merOrderId+amount+transId+status+secretKey;
        md5Str= DigestUtils.md5Hex(md5Str);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("MerId",merId);
        resultMap.put("MerOrderId",merOrderId);
        resultMap.put("OrderId;",transId);
        resultMap.put("Amount;",amount);
        resultMap.put("TradeTime;",transOrder.getTradeTime());
        resultMap.put("Status;",status);
        resultMap.put("Msg;",result.getBankResult());
        resultMap.put("SignMsg;",md5Str);
        String resultjson = JsonUtils.objectToJson(resultMap);
        return resultjson;
    }
    private  void checkMd5(TradeObjectSquare tradeObjectSquare, String secretKey) throws PayException {
        String str =
                tradeObjectSquare.getBizType() +
                        tradeObjectSquare.getMerId() +
                        tradeObjectSquare.getMerOrderId() +
                        tradeObjectSquare.getCurrency() +
                        tradeObjectSquare.getAmount() +
                        secretKey;
        if (!StringUtils.equalsAnyIgnoreCase(tradeObjectSquare.getSignMsg(), DigestUtils.md5Hex(str).toUpperCase())) {
            throw new PayException("SignMsg校验错误！", 1300);
        }
    }

}