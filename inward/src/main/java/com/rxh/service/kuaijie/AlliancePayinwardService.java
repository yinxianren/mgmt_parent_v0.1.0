package com.rxh.service.kuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractPayService;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AlliancePayinwardService extends AbstractPayService {




    public String toSaveOrder(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        SquareTrade trade= payment(systemOrderTrack, tradeObjectSquare);

        BankResult bankResult = toPay(trade);
        inwardSquareService.afterPay(trade,bankResult,trade.getTransAudit(),trade.getTransAudit().getTransferer());
        String returnJson = inwardSquareService.getReturnJson(trade, bankResult);
        logger.info("代付请求返回："+returnJson );
        return returnJson;

    }

    private String getRetrunJson(SquareTrade trade) {
        TransOrder transOrder = trade.getTransOrder();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String amount = transOrder.getAmount().toString();
        String merId = merchantInfo.getMerId();
        String merOrderId = trade.getMerOrderId();
        String orderId = transOrder.getTransId();
        String status = transOrder.getOrderStatus().toString();
        String msg="受理成功";

        String secretKey = merchantInfo.getSecretKey();

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("Amount;",amount);
        resultMap.put("MerId",merId);
        resultMap.put("MerOrderId",merOrderId);
        resultMap.put("Msg;",msg);
        resultMap.put("OrderId;",orderId);
        resultMap.put("Status;",status);
        resultMap.put("TradeTime;",transOrder.getTradeTime());
        String paramJson= JsonUtils.objectToJson(resultMap);
        String md5Str= DigestUtils.md5Hex(paramJson+secretKey);
        resultMap.put("SignMsg;",md5Str);
        String resultjson = JsonUtils.objectToJson(resultMap);
        return resultjson;
    }



    public SquareTrade payment (SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws  Exception{

//        MerchantInfo merchantInfo = recordSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(tradeObjectSquare.getMerId());
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        //封装交易参数
        SquareTrade trade = new SquareTrade();
        //查询是否为重复订单
        TransOrder item=paymentRecordSquareService.getTransOrderByMerOrderIdAndMerIdAndTerMerId(systemOrderTrack.getMerOrderId(),tradeObjectSquare.getMerId(),tradeObjectSquare.getTerminalMerId());
        isNotNull(item,"该笔代付订单已存在");
        PayOrder payOrder = paymentRecordSquareService.getPayOrderByMerOrderIdAndMerIdAndTerMerId(tradeObjectSquare.getOriginalMerOrderId(),tradeObjectSquare.getMerId(),tradeObjectSquare.getTerminalMerId());
        isNull(payOrder,"不存在付款订单请先付款");
        //获取收单支付通道
//        ChannelInfo payOrderChannel= recordSquareService.getChannelInfo(payOrder.getChannelId());
        ChannelInfo payOrderChannel= redisCacheCommonCompoment.channelInfoCache.getOne(payOrder.getChannelId());
        trade.setPayOrderChannel(payOrderChannel);
        //获取代付支付通道
//        ChannelInfo outChannel= recordSquareService.getChannelInfo(payOrderChannel.getOutChannelId());
        ChannelInfo outChannel= redisCacheCommonCompoment.channelInfoCache.getOne(payOrderChannel.getOutChannelId());
        //获取获取通道对应第三方接口
//        ExtraChannelInfo extraChannelInfo =recordSquareService.searchExtraChannelInfo(payOrderChannel.getOrganizationId(), SystemConstant.BONDCARD);
        ExtraChannelInfo extraChannelInfo = redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                .stream()
                .filter(t->
                        t.getOrganizationId().equals(payOrderChannel.getOrganizationId())
                                && t.getType().equals(SystemConstant.BONDCARD)

                )
                .distinct()
                .findAny()
                .orElse(null);
        //获取商户持卡人信息
//        MerchantCard merchantCard=recordSquareService.searchMerchantCard(extraChannelInfo,tradeObjectSquare);
        MerchantCard merchantCard=redisCacheCommonCompoment.merchantCardCache.getMore(tradeObjectSquare.getMerId(),tradeObjectSquare.getTerminalMerId())
                .stream()
                .filter(t-> t.getExtraChannelId().equals(extraChannelInfo.getExtraChannelId()))
                .distinct()
                .findAny()
                .orElse(null);

        MerchantRegisterCollect merchantRegisterCollect=recordSquareService.searchMerchantRegisterCollect(extraChannelInfo,tradeObjectSquare);

        trade.setMerchantRegisterCollect(merchantRegisterCollect);
        trade.setPayOrder(payOrder);
        trade.setMerOrderId(tradeObjectSquare.getMerOrderId());
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setChannelInfo(outChannel);
        trade.setMerchantInfo(merchantInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        //查询商户是否代付过
        checkTransAmount(systemOrderTrack,tradeObjectSquare,trade);
        // checkMd5(tradeObjectSquare,merchantInfo.getSecretKey());
        // 获取商户配置
//        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
//        MerchantSetting merchantSetting =redisCacheCommonCompoment.merchantSettingCache.getOne(tradeObjectSquare.getMerId());
        // 获取商户费率对象
//        MerchantRate merchantRate = merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merchantInfo.getMerId(),outChannel.getType().toString());
        MerchantRate merchantRate =redisCacheCommonCompoment.merchantRateCache.getOne(merchantInfo.getMerId(),outChannel.getType().toString());
        // 保存代付业务订单主表  保存结算业务收款账号信息
        recordSquareService.saveOrUpadateTransBankInfo(tradeObjectSquare);
        //保存及获取订单对象
        TransOrder transOrder = recordSquareService.saveOrUpadateTransOrder(systemOrderTrack,tradeObjectSquare,outChannel,merchantRate,merchantInfo);
        systemOrderTrack.setOrderId(transOrder.getTransId());
        // 保存记录代付审核的信息   商户风控对象
        MerchantQuotaRisk merchantQuotaRisk =  redisCacheCommonCompoment.merchantQuotaRiskCache.getOne(tradeObjectSquare.getMerId());
//        MerchantQuotaRisk merchantQuotaRisk = recordSquareService.getMerchantQuotaRiskByMerId(tradeObjectSquare.getMerId());
        isNull(merchantQuotaRisk,"商户风控对象不存在");
        //执行风控
        recordSquareService.analysis(outChannel,transOrder,merchantQuotaRisk);
//        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setTransOrder(transOrder);
        TransAudit transAudit = recordSquareService.saveOrUpadateTransAudit(tradeObjectSquare, transOrder, trade);
        trade.setTransAudit(transAudit);
        return trade;
    }

    private void checkTransAmount(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare, SquareTrade trade) throws PayException {
        PayOrder payOrder = trade.getPayOrder();

        //商户号
        String merId = systemOrderTrack.getMerId();
        String terminalMerId= tradeObjectSquare.getTerminalMerId();
        //代付金额
        BigDecimal dfAmount = systemOrderTrack.getAmount();
        //收单订单号
        String originalMerOrderId = tradeObjectSquare.getOriginalMerOrderId();
        BigDecimal terminalFee = payOrder.getTerminalFee() == null ? new BigDecimal(0) : payOrder.getTerminalFee();
        //获取当前收单订单的可代付金额  订单金额-商户手续费
        BigDecimal payOrderAmount=payOrder.getAmount().subtract(terminalFee);
        if (dfAmount.compareTo(payOrderAmount)>0){
            throw new PayException("代付金额大于订单金额",1000);
        }
        //当前代付金额+手续费


        //获取当前订单的已代付金额
        BigDecimal transAmount=recordSquareService.getTransOrderAmount(merId,terminalMerId,originalMerOrderId);
        if (transAmount==null){
            transAmount=new BigDecimal(0);
        }
        if (dfAmount.compareTo( payOrderAmount.subtract(transAmount))>0){
            throw new PayException("代付金额大于订单剩余可代付金额",1000);
        }

        //判断商户钱包金额是否支持代付

        MerchantWallet merchantWallet = recordSquareService.getMerchantWallet(merId);
        //下级商户钱包
        TerminalMerchantsWallet terminalMerchantsWallet = recordSquareService.getTerminalMerchantsWallet(merId,terminalMerId);

        BigDecimal terMertotalAvailableAmount = terminalMerchantsWallet.getTotalAvailableAmount();
        if (null==terMertotalAvailableAmount){
            terMertotalAvailableAmount=new BigDecimal(0);
        }
        if (dfAmount.compareTo(terMertotalAvailableAmount)>0){
            throw new PayException("代付金额大于终端商户可用余额",1000);
        }
        BigDecimal totalAvailableAmount = merchantWallet.getTotalAvailableAmount();
        if (null==totalAvailableAmount){
            totalAvailableAmount=new BigDecimal(0);
        }
        if (dfAmount.compareTo(totalAvailableAmount)>0){
            throw new PayException("代付金额大于商户可用余额",1000);
        }

    }



    public String goPay(String tranMap ) throws Exception {
        Map<String,Object> map = JsonUtils.jsonToMap(tranMap);
        String transId =  map.get("transId").toString();
        String transferer =  map.get("transferer").toString();
        TransAudit transAudit =  recordSquareService.getTransAuditByTransId(transId);
        SquareTrade trade = JsonUtils.jsonToPojo(transAudit.getTrade(), SquareTrade.class);

        BankResult bankResult = toPay(trade);
        inwardSquareService.afterPay(trade,bankResult,transAudit,transferer);
        String returnJson = inwardSquareService.getReturnJson(trade, bankResult);
        return returnJson;


    }



    private BankResult toPay(SquareTrade trade)  throws Exception{
//        logger.info(JsonUtils.objectToJsonNonNull(trade));
        // 发送请求至接口
        logger.info("发送代付请求"+trade.getChannelInfo().getPayUrl());
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getChannelInfo().getPayUrl(), JsonUtils.objectToJsonNonNull(trade));

        logger.info("代付请求结果"+result);
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), "http://localhost:8077/quickPay/trade", JsonUtils.objectToJson(trade));
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
}
