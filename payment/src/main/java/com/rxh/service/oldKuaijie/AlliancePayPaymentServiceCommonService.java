package com.rxh.service.oldKuaijie;

import com.rxh.cache.ehcache.MerchantSquareRateCache;
import com.rxh.cache.ehcache.MerchantSquareSettingCache;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractCommonService;
import com.rxh.service.merchant.MerchantAddCusService;
import com.rxh.service.square.ChannelInfoService;
import com.rxh.service.square.ExtraChannelInfoService;
import com.rxh.square.pojo.*;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.StringUtils;
import com.rxh.utils.SystemConstant;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class AlliancePayPaymentServiceCommonService extends AbstractCommonService {


    private final static Logger logger = LoggerFactory.getLogger(AlliancePayPaymentServiceCommonService.class);
    private final RecordPaymentSquareService recordPaymentSquareService;
    private final MerchantSquareSettingCache merchantSquareSettingCache;
    private final MerchantSquareRateCache merchantSquareRateCache;
    private final GetOrderRiskQuotaDataService getOrderRiskQuotaDataService;
    private final MerchantAddCusService merchantAddCusService;
    private final ChannelInfoService channelInfoService;
    private final GetOrderWalletService getOrderWalletService;
    private final ExtraChannelInfoService extraChannelInfoService;
    @Autowired
    public AlliancePayPaymentServiceCommonService(RecordPaymentSquareService recordPaymentSquareService, MerchantSquareSettingCache merchantSquareSettingCache, MerchantSquareRateCache merchantSquareRateCache, GetOrderRiskQuotaDataService getOrderRiskQuotaDataService, MerchantAddCusService merchantAddCusService, ChannelInfoService channelInfoService, GetOrderWalletService getOrderWalletService, ExtraChannelInfoService extraChannelInfoService) {
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.merchantSquareSettingCache = merchantSquareSettingCache;
        this.merchantSquareRateCache = merchantSquareRateCache;
        this.getOrderRiskQuotaDataService = getOrderRiskQuotaDataService;
        this.merchantAddCusService = merchantAddCusService;
        this.channelInfoService = channelInfoService;
        this.getOrderWalletService = getOrderWalletService;
        this.extraChannelInfoService = extraChannelInfoService;
    }



    public String toBondCard(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException, UnsupportedEncodingException {
        SquareTrade trade = getBondParam(systemOrderTrack, tradeObjectSquare);
        BankResult result = toBond(trade);
        updateStatus(trade, result);
        String returnJson = getBondReturnJson(trade, result);
        return returnJson;
    }


    private void updateStatus(SquareTrade trade, BankResult result) {
        MerchantCard merchantCard = trade.getMerchantCard();

        merchantCard.setStatus(Integer.valueOf(result.getStatus()));
        merchantCard.setResult(result.getBankData());
        merchantCard.setBackData(result.getParam());
        recordPaymentSquareService.updateMerchantCard(merchantCard);

    }
    private void updatePayStatus(SquareTrade trade, BankResult result) {
        PayOrder payOrder = trade.getPayOrder();
        payOrder.setOrderStatus(Integer.valueOf(result.getStatus()));
        getOrderWalletService.updateWallet(trade);
        recordPaymentSquareService.UpdatePayOrder(payOrder);
    }

    private BankResult toBond(SquareTrade trade) throws PayException {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
//
        Map<String, Object> param = JsonUtils.jsonToMap(extraChannelInfo.getData());
        logger.info("发送绑卡请求"+param.get("cardApply").toString());
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl() + param.get("cardApply").toString(), JsonUtils.objectToJsonNonNull(trade));
        logger.info("绑卡请求返回"+result);
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://localhost:8083/allinPay/bondCard", JsonUtils.objectToJsonNonNull(trade));
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

    public SquareTrade getBondParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        logger.info("绑卡申请");
        logger.info("获取所有绑定记录");
        List<MerchantCard> allMerchantCard = recordPaymentSquareService.getAllMerchantCard(tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId(),SystemConstant.SUCCESS);
        logger.info("获取所有绑定记录成功"+JsonUtils.objectToJsonNonNull(allMerchantCard));
        // 查询商户信息
        logger.info("获取商户信息");
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        logger.info("获取商户信息成功"+JsonUtils.objectToJsonNonNull(merchantInfo));
//        checkBondMd5(tradeObjectSquare,merchantInfo.getSecretKey());
        // 获取商户配置
        logger.info("获取商户配置");
        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
        logger.info("获取商户配置成功"+JsonUtils.objectToJsonNonNull(merchantSetting));
        logger.info("获取系统通道");
        Map<String, Object> extraChannelInfos = recordPaymentSquareService.getChannelInfosByPayType(merchantSetting, tradeObjectSquare, allMerchantCard);
        logger.info("获取系统通道成功"+JsonUtils.objectToJsonNonNull(extraChannelInfos));
        logger.info("保存绑卡信息");
        MerchantCard merchantCard = recordPaymentSquareService.insertMerchantCard(tradeObjectSquare, extraChannelInfos);
        logger.info("保存绑卡信息成功"+JsonUtils.objectToJsonNonNull(merchantCard));
        logger.info("获取绑卡通道");
        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfo(extraChannelInfos, merchantCard);
        logger.info("获取绑卡通道成功"+JsonUtils.objectToJsonNonNull(extraChannelInfo));
//        ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(extraChannelInfo.getInChannelId());
        logger.info("获取进件记录");
        MerchantRegisterInfo merchantRegisterInfo=  recordPaymentSquareService.getMerchantRegisterInfo(extraChannelInfo,extraChannelInfos);
        logger.info("获取进件记录成功"+JsonUtils.objectToJsonNonNull(merchantRegisterInfo));
        SquareTrade trade = new SquareTrade();

        //封装交易参数
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantCard(merchantCard);
//        trade.setChannelInfo(channelInfo);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setMerOrderId(tradeObjectSquare.getMerOrderId());
        trade.setMerchantInfo(merchantInfo);
        return trade;
    }

    private void checkBondMd5(TradeObjectSquare tradeObjectSquare, String secretKey) throws PayException {
        Map<String, Object> map = new TreeMap<>();
        map.put("bizType",tradeObjectSquare.getBizType());
        map.put("charset",tradeObjectSquare.getCharset());
        map.put("signType",tradeObjectSquare.getSignType());
        map.put("merId",tradeObjectSquare.getMerId());
        map.put("merOrderId",tradeObjectSquare.getMerOrderId());
        map.put("userName",tradeObjectSquare.getUserName());
        map.put("identityType",tradeObjectSquare.getIdentityType());
        map.put("bankcardType",tradeObjectSquare.getBankcardType());
        map.put("identityNum",tradeObjectSquare.getIdentityNum());
        map.put("bankCardNum",tradeObjectSquare.getBankCardNum());
        map.put("bankCardPhone",tradeObjectSquare.getBankCardPhone());
        map.put("bankCode",tradeObjectSquare.getBankCode());
        map.put("validDate",tradeObjectSquare.getValidDate());
        map.put("securityCode",tradeObjectSquare.getSecurityCode());
        map.put("payFee",tradeObjectSquare.getPayFee());
        map.put("backFee",tradeObjectSquare.getBackFee());
        map.put("backCardNum",tradeObjectSquare.getBackCardNum());
        map.put("backBankCode",tradeObjectSquare.getBackBankCode());
        map.put("backCardPhone",tradeObjectSquare.getBackCardPhone());
        map.put("terminalMerId",tradeObjectSquare.getTerminalMerId());
        map.put("terminalMerName",tradeObjectSquare.getTerminalMerName());
        map.put("returnUrl",tradeObjectSquare.getReturnUrl());
        map.put("noticeUrl",tradeObjectSquare.getNoticeUrl());
        String md5Info = getMd5SignWithKey(map,secretKey);
        if (md5Info.equals(tradeObjectSquare.getSignMsg())){
            throw new PayException("签名验证不正确",1023);
        }
    }

    private void checkMd5(TradeObjectSquare tradeObjectSquare, String secretKey) throws PayException {
        Map<String, Object> map = new TreeMap<>();
        map.put("bizType",tradeObjectSquare.getBizType());
        map.put("charset",tradeObjectSquare.getCharset());
        map.put("signType",tradeObjectSquare.getSignType());
        map.put("merId",tradeObjectSquare.getMerId());
        map.put("merOrderId",tradeObjectSquare.getMerOrderId());
        map.put("currency",tradeObjectSquare.getCurrency());
        map.put("amount",tradeObjectSquare.getAmount());
        map.put("identityType",tradeObjectSquare.getIdentityType());
        map.put("bankcardType",tradeObjectSquare.getBankcardType());
        map.put("identityNum",tradeObjectSquare.getIdentityNum());
        map.put("bankCardNum",tradeObjectSquare.getBankCardNum());
        map.put("bankCode",tradeObjectSquare.getBankCode());
        map.put("validDate",tradeObjectSquare.getValidDate());
        map.put("securityCode",tradeObjectSquare.getSecurityCode());
        map.put("payFee",tradeObjectSquare.getPayFee());
        map.put("terminalMerId",tradeObjectSquare.getTerminalMerId());
        map.put("terminalMerName",tradeObjectSquare.getTerminalMerName());
        map.put("returnUrl",tradeObjectSquare.getReturnUrl());
        map.put("noticeUrl",tradeObjectSquare.getNoticeUrl());
        String md5Info = getMd5SignWithKey(map,secretKey);
        if (md5Info.equals(tradeObjectSquare.getSignMsg())){
            throw new PayException("签名验证不正确",1023);
        }
    };





    public String toPayment(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        SquareTrade trade = getTrade(systemOrderTrack, tradeObjectSquare);
        BankResult result = toPay(trade);
        afterPay(trade,result);

        String returnJson = getTradeReturnJson(trade, result);
        return returnJson;

    }

    public void afterPay(SquareTrade trade, BankResult result) throws PayException {
        PayOrder payOrder = trade.getPayOrder();
        payOrder.setTradeResult(result.getParam().length()>=500?result.getParam().substring(0,499):result.getParam());
        ChannelInfo channelInfo = trade.getChannelInfo();
        MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(payOrder.getMerId());
        if(!Objects.equals(payOrder.getOrderStatus(), Integer.valueOf(result.getStatus())) &&result.getStatus()==0){
            payOrder.setBankTime(result.getBankTime());
            payOrder.setOrgOrderId(result.getBankOrderId());
            getOrderWalletService.updateMerchantChannelHistory(trade);
            getOrderWalletService.updateWallet(trade);
            getOrderWalletService.updateRiskQuotaData(payOrder,channelInfo,merchantQuotaRisk);
        }
        payOrder.setOrderStatus(Integer.valueOf(result.getStatus()));
        recordPaymentSquareService.UpdatePayOrder(payOrder);
    }



    private BankResult toPay(SquareTrade trade) throws PayException {
        logger.info(JsonUtils.objectToJsonNonNull(trade));
        String others = trade.getChannelInfo().getOthers();
        Map<String, Object> map = JsonUtils.jsonToMap(others);
        // 发送请求至接口
        logger.info("发送收单请求"+map.get("payApply").toString());

        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), trade.getChannelInfo().getPayUrl() +map.get("payApply").toString(), JsonUtils.objectToJsonNonNull(trade));
        logger.info("收单请求返回"+result);

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

    private SquareTrade getTrade(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        logger.info("收单请求");
        List<PayOrder> item = recordPaymentSquareService.getPayOrderByMerOrderId(tradeObjectSquare, systemOrderTrack.getMerOrderId());
        if(null != item){
            if(item.size() >0){
                List<PayOrder> fillterItem= item.stream().filter(el->el.getOrderStatus()==0|| el.getOrderStatus()==3).collect(Collectors.toList());
                if(null !=fillterItem && fillterItem.size() !=0 ){
                    fillterItem.stream().forEach(el ->logger.info("【getOrder请求】订单已经已经存在：商户号：["+el.getMerId()+"]，订单号：["+el.getMerOrderId()+"],订单状态：["+el.getOrderStatus()+"]"));
                    throw new PayException("该笔订单已存在", 3003);
                }
            }
        }
        SquareTrade trade = new SquareTrade();
        // 查询商户信息
        logger.info("查询商户信息");
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        if(null == merchantInfo) throw new PayException("未找到商户信息", 3003);
        logger.info("查询商户信息成功"+JsonUtils.objectToJsonNonNull(merchantInfo));
        // 获取商户配置
        logger.info("获取商户配置");
        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
        logger.info("获取商户配置成功"+JsonUtils.objectToJsonNonNull(merchantSetting));
//        checkMd5(tradeObjectSquare,merchantInfo.getSecretKey());
        // 商户风控对象
        logger.info("获取商户风控对象");
        MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(tradeObjectSquare.getMerId());
        logger.info("获取商户风控对象成功"+JsonUtils.objectToJsonNonNull(merchantQuotaRisk));
        logger.info("获取商户持卡信息列表");
        List<MerchantCard> merchantCards = recordPaymentSquareService.getAllMerchantCard(tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId(), SystemConstant.SUCCESS);
        logger.info("获取商户持卡信息列表成功"+JsonUtils.objectToJsonNonNull(merchantCards));
        ChannelInfo channelInfo=null;
        Boolean flag=false;
        logger.info("获取商户通道使用记录");
        MerchantChannelHistory wasUseChannel = recordPaymentSquareService.getLastUseChannel(tradeObjectSquare);
        logger.info("获取商户通道使用记录成功"+JsonUtils.objectToJsonNonNull(wasUseChannel));
        trade.setMerchantChannelHistory(wasUseChannel);



        if (wasUseChannel != null){
            channelInfo = recordPaymentSquareService.getChannelInfo(wasUseChannel.getChannelId());
            flag = recordPaymentSquareService.checkChannelByParam(channelInfo,tradeObjectSquare.getAmount());
        }

        logger.info("获取系统通道");

        Map<String, Object> extraChannelInfos = recordPaymentSquareService.getChannelInfosByMerchantCards(merchantSetting, tradeObjectSquare, merchantCards);
        logger.info("获取系统通道成功"+JsonUtils.objectToJsonNonNull(extraChannelInfos));
        logger.info("获取交易通道");
        if (!flag){
            channelInfo = recordPaymentSquareService.chooseChannel(merchantSetting, tradeObjectSquare, extraChannelInfos, merchantCards);
        }
        logger.info("获取交易通道成功"+JsonUtils.objectToJsonNonNull(channelInfo));
        logger.info("获取附属通道");
        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.chooseExtraChannelInfo(channelInfo, extraChannelInfos);
        logger.info("获取附属通道成功"+JsonUtils.objectToJsonNonNull(extraChannelInfo));
        logger.info("获取持卡人信息");
        MerchantCard merchantCard = recordPaymentSquareService.chooseMerchantCard(extraChannelInfo, tradeObjectSquare, merchantCards);
        logger.info("获取持卡人信息成功"+JsonUtils.objectToJsonNonNull(merchantCard));

        // 获取商户费率对象
        logger.info("获取商户费率对象");
        MerchantRate merchantRate = merchantSquareRateCache.getMerchantRateByMerIdAndPayType(merchantInfo.getMerId(), channelInfo.getType().toString());
        logger.info("获取商户费率对象成功"+JsonUtils.objectToJsonNonNull(merchantRate));
        // 保存代付业务订单主表
        logger.info("保存收单业务订单主表");
        PayOrder payOrder = recordPaymentSquareService.saveOrUpadatePayOrder(systemOrderTrack, tradeObjectSquare, channelInfo, merchantRate, merchantInfo);
        logger.info("保存收单业务订单主表成功"+JsonUtils.objectToJsonNonNull(payOrder));

        logger.info("获取商户进件记录");
        MerchantRegisterInfo merchantRegisterInfo = recordPaymentSquareService.getMerchantRegisterInfo(extraChannelInfo, extraChannelInfos);
        logger.info("获取商户进件记录成功"+JsonUtils.objectToJsonNonNull(merchantRegisterInfo));
        systemOrderTrack.setOrderId(payOrder.getPayId());
        // 保存结算业务收款账号信息
        recordPaymentSquareService.saveOrUpadateCardHolderInfo(tradeObjectSquare, payOrder);
        recordPaymentSquareService.saveOrUpadatePayProductDetail(tradeObjectSquare, payOrder);
        getOrderRiskQuotaDataService.analysis(channelInfo, payOrder, merchantQuotaRisk);


        trade.setPayOrder(payOrder);
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantInfo(merchantInfo);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setChannelInfo(channelInfo);
        trade.setMerchantInfo(merchantInfo);
        return trade;
    }


    private String getRetrunJson(SquareTrade trade, BankResult result) {
        PayOrder payOrder = trade.getPayOrder();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String amount = payOrder.getAmount().toString();
        String merId = merchantInfo.getMerId();
        String merOrderId = trade.getMerOrderId();
        String orderId = payOrder.getPayId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();

        String secretKey = merchantInfo.getSecretKey();

        Map<String, Object> resultMap = new TreeMap<>();
        resultMap.put("Amount;", amount);
        resultMap.put("MerId", merId);
        resultMap.put("MerOrderId", merOrderId);
        resultMap.put("Msg;", msg);
        resultMap.put("OrderId;", orderId);
        resultMap.put("Status;", status);
        resultMap.put("TradeTime;", payOrder.getTradeTime());
        String paramJson = JsonUtils.objectToJson(resultMap);
        String md5Str = DigestUtils.md5Hex(paramJson + secretKey);
        resultMap.put("SignMsg;", md5Str);
        String resultjson = JsonUtils.objectToJson(resultMap);
        return resultjson;
    }


    private String getBondReturnJson(SquareTrade trade, BankResult result)  {
        MerchantCard merchantCard = trade.getMerchantCard();
        String merId = merchantCard.getMerId();
        String merOrderId = merchantCard.getMerOrderId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();

        Map<String, Object> map = new TreeMap<>();
        map.put("merId", merId);
        map.put("merOrderId", merOrderId);
        map.put("status", status);
        map.put("msg", msg);
        String signMsg = getMd5Sign(map);
        map.put("signMsg", signMsg);
        String returnJson = JsonUtils.objectToJson(map);
        return returnJson;
    }

    private String getTradeReturnJson(SquareTrade trade, BankResult result)  {
        MerchantCard merchantCard = trade.getMerchantCard();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = merchantCard.getMerId();
        String merOrderId = merchantCard.getMerOrderId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();

        Map<String, Object> map = new TreeMap<>();
        map.put("merId", merId);
        map.put("merOrderId", merOrderId);
        map.put("orderId", trade.getPayOrder().getPayId());
        map.put("amount", trade.getPayOrder().getAmount());
        map.put("tradeTime", trade.getPayOrder().getTradeTime());
        map.put("status", status);
        map.put("msg", msg);
        String signMsg = getMd5Sign(map);
        map.put("signMsg", signMsg);
        String returnJson = JsonUtils.objectToJson(map);
        return returnJson;
    }




    public String toConfirmBond(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException, UnsupportedEncodingException {
        SquareTrade trade = getConfirmParam(systemOrderTrack, tradeObjectSquare);
        BankResult result = confirmBond(trade);
        updateStatus(trade, result);
        String bondReturnJson = getBondReturnJson(trade, result);
        return bondReturnJson;
    }
    public String toConfirmPay(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException, UnsupportedEncodingException {
        SquareTrade trade = getConfirmPayParam(systemOrderTrack, tradeObjectSquare);
        systemOrderTrack.setOrderId(trade.getPayOrder().getPayId());
        BankResult result = confirmPay(trade);
        afterPay(trade, result);
        String bondReturnJson = getTradeReturnJson(trade, result);
        return bondReturnJson;
    }

    private SquareTrade getConfirmParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {

        logger.info("绑卡确认");
        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermId(tradeObjectSquare.getMerId(), tradeObjectSquare.getMerOrderId(),tradeObjectSquare.getTerminalMerId(),SystemConstant.BANK_STATUS_PENDING_PAYMENT);
        logger.info("获取绑卡记录"+JsonUtils.objectToJsonNonNull(merchantCard));

        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByExtraChannelId(merchantCard.getExtraChannelId());
        logger.info("获取绑卡通道"+JsonUtils.objectToJsonNonNull(extraChannelInfo));


//        PayOrder payOrder = recordPaymentSquareService.getPayOrderByMerOrderId(tradeObjectSquare,tradeObjectSquare.getMerOrderId());

        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        logger.info("获取商户信息"+JsonUtils.objectToJsonNonNull(extraChannelInfo));

        MerchantRegisterInfo merchantRegisterInfo = recordPaymentSquareService.getMerchantRegisterInfoByParam(extraChannelInfo, tradeObjectSquare);
        logger.info("获取商户进件信息"+JsonUtils.objectToJsonNonNull(merchantRegisterInfo));

        SquareTrade trade = new SquareTrade();
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
//        trade.setPayOrder(payOrder);
        trade.setMerchantInfo(merchantInfo);

        return trade;
    }

    private SquareTrade getConfirmPayParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {

        PayOrder payOrder = recordPaymentSquareService.getProcessingPayOrderByMerOrderId(tradeObjectSquare,tradeObjectSquare.getMerOrderId());

        ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId());

        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByOrgId(channelInfo.getOrganizationId(),SystemConstant.BONDCARD);

        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndTermIdAndExtraId(tradeObjectSquare.getMerId(),tradeObjectSquare.getTerminalMerId(),extraChannelInfo.getExtraChannelId());

        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());

        MerchantRegisterInfo merchantRegisterInfo = recordPaymentSquareService.getMerchantRegisterInfoByParam(extraChannelInfo, tradeObjectSquare);

        MerchantChannelHistory wasUseChannel = recordPaymentSquareService.getLastUseChannel(tradeObjectSquare);

        SquareTrade trade = new SquareTrade();
        trade.setMerchantChannelHistory(wasUseChannel);
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setChannelInfo(channelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setPayOrder(payOrder);
        trade.setMerchantInfo(merchantInfo);

        return trade;
    }

    private BankResult confirmBond(SquareTrade trade) throws PayException {
        //confirmBond

        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        String data = extraChannelInfo.getData();
        Map<String, Object> map = JsonUtils.jsonToMap(data);
//        extraChannelInfo.getData()
        logger.info("发送绑卡确认请求"+map.get("cardConfirm").toString());
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl() + map.get("cardConfirm").toString(), JsonUtils.objectToJsonNonNull(trade));
        logger.info("发送绑卡确认返回"+result);
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://localhost:8200/allinPayBindCard/confirmBond", JsonUtils.objectToJsonNonNull(trade));
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
    private BankResult confirmPay(SquareTrade trade) throws PayException {
        String others = trade.getChannelInfo().getOthers();
        logger.info("支付确认请求参数"+JsonUtils.objectToJsonNonNull(trade));
        Map<String, Object> map = JsonUtils.jsonToMap(others);
        logger.info("发送支付确认请求"+map.get("payConfirm").toString());
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), trade.getChannelInfo().getPayUrl() + map.get("payConfirm").toString(), JsonUtils.objectToJsonNonNull(trade));
        logger.info("支付确认返回"+result);
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



    public String getMd5Sign(Map<String,Object> param)  {
        StringBuffer sb = new StringBuffer();
        for (String key : param.keySet()){
            String value = param.get(key) != null ? param.get(key).toString() : null;
            if(!StringUtils.isEmpty(value)){
                sb.append(key+"="+value+"&");
            }
        }
        String sign = sb.substring(0,sb.lastIndexOf("&"));
        String md5Sign=  DigestUtils.md5Hex(sign.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return md5Sign;
    }
    public String getMd5SignWithKey(Map<String,Object> param,String md5Key)  {
        StringBuffer sb = new StringBuffer();
        for (String key : param.keySet()){
            String value = param.get(key) != null ? param.get(key).toString() : null;
            if(!StringUtils.isEmpty(value)){
                sb.append(key+"="+value+"&");
            }
        }
        String sign = sb.substring(0,sb.lastIndexOf("&"))+md5Key;
        String md5Sign=  DigestUtils.md5Hex(sign.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return md5Sign;
    }

    public String reGetBondCode(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        SquareTrade trade = getBondCodeParam(systemOrderTrack, tradeObjectSquare);
        BankResult result = toGetBondCode(trade);
        String bondCodeReturnJson = getBondCodeReturnJson(trade, result);
        return bondCodeReturnJson;
    }

    private SquareTrade getBondCodeParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        logger.info("重新获取绑卡验证码");
        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermId(tradeObjectSquare.getMerId(), tradeObjectSquare.getMerOrderId(),tradeObjectSquare.getTerminalMerId(),SystemConstant.BANK_STATUS_PENDING_PAYMENT);
        logger.info("获取绑卡记录"+JsonUtils.objectToJsonNonNull(merchantCard));
        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByExtraChannelId(merchantCard.getExtraChannelId());
        logger.info("获取绑卡通道"+JsonUtils.objectToJsonNonNull(extraChannelInfo));
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        logger.info("获取商户信息"+JsonUtils.objectToJsonNonNull(extraChannelInfo));
        MerchantRegisterInfo merchantRegisterInfo = recordPaymentSquareService.getMerchantRegisterInfoByParam(extraChannelInfo, tradeObjectSquare);
        logger.info("获取商户进件信息"+JsonUtils.objectToJsonNonNull(merchantRegisterInfo));
        SquareTrade trade = new SquareTrade();
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setMerchantInfo(merchantInfo);
        return trade;
    };

    private BankResult toGetBondCode(SquareTrade trade) throws PayException {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        String data = extraChannelInfo.getData();
        Map<String, Object> map = JsonUtils.jsonToMap(data);
//        logger.info("发送获取绑卡验证码确认请求"+map.get("cardSms").toString());
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl() + map.get("cardSms").toString(), JsonUtils.objectToJsonNonNull(trade));
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://localhost:8200/allinPay/reGetCode", JsonUtils.objectToJsonNonNull(trade));
        logger.info("发送获取绑卡验证码确认返回"+result);
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

    private String getBondCodeReturnJson(SquareTrade trade, BankResult result)  {
        MerchantCard merchantCard = trade.getMerchantCard();
        String merId = merchantCard.getMerId();
        String merOrderId = merchantCard.getMerOrderId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();
        Map<String, Object> map = new TreeMap<>();
        map.put("merId", merId);
        map.put("merOrderId", merOrderId);
        map.put("status", status);
        map.put("msg", msg);
        String signMsg = getMd5Sign(map);
        map.put("signMsg", signMsg);
        String returnJson = JsonUtils.objectToJson(map);
        return returnJson;
    }

}
