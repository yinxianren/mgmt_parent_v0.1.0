package com.rxh.service.kuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractCommonService;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AlliancePayPamentPayService  extends AbstractCommonService {





    public String toConfirmSMS(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        {

            String merId=tradeObjectSquare.getMerId();
            String merOrderId=tradeObjectSquare.getMerOrderId();
            String terminalMerId=tradeObjectSquare.getTerminalMerId();
            //获取商户信息
            MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
            if(null == merchantInfo) throw new PayException("确认商户收单短信时，获取MerchantInfo信息为空");
            logger.info("【###########确认商户短信###########】根据-->merId={};查询到的结果为：TransOrder=[{}]",merId,merchantInfo.toString());
            //验签
            CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
            //1.获取订单信息
            PayOrder payOrder= recordPaymentSquareService.getProcessingPayOrderByMerOrderId(tradeObjectSquare,merOrderId);
            if(null == payOrder ) throw new PayException("确认商户收单短信时，获取PayOrder信息为空");
            logger.info("【#########确认商户短信###########】根据-->merId={},merOrderId={},terminalMerId={};查询到的结果为：PayOrder=[{}]",merId,merOrderId,terminalMerId,payOrder.toString());

            //2.获取通道信息
//            ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId());
            ChannelInfo channelInfo = redisCacheCommonCompoment.channelInfoCache.getOne(payOrder.getChannelId());
            if(null == channelInfo) throw new PayException("确认商户收单短信时，获取ChannelInfo信息为空");

            //根据组织id和绑卡获取附属通道信息
//            ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByOrgId(channelInfo.getOrganizationId(), SystemConstant.BONDCARD);
            ExtraChannelInfo extraChannelInfo = redisCacheCommonCompoment.extraChannelInfoCache.getAll()
                    .stream()
                    .filter(t-> t.getOrganizationId().equals(channelInfo.getOrganizationId()) && t.getType().equals(SystemConstant.BONDCARD))
                    .distinct()
                    .findAny()
                    .orElse(null);
            if(null == extraChannelInfo) throw new PayException("确认商户收单短信时，获取 ExtraChannelInfo信息为空");
            //3.根据商户号、终端号、和附属通道id、状态为成功做条件获取绑卡信息
//            MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndTermIdAndExtraId(merId,terminalMerId,extraChannelInfo.getExtraChannelId());
            MerchantCard merchantCard =  redisCacheCommonCompoment.merchantCardCache.getMore(merId,terminalMerId)
                    .stream()
                    .filter(t->t.getExtraChannelId().equals(extraChannelInfo.getExtraChannelId()) && t.getCardNum().equals(payOrder.getBankCardNum()))
                    .distinct()
                    .findAny()
                    .orElse(null);
            if(null == merchantCard) throw new PayException("确认商户收单短信时，获取MerchantCard信息为空");

            logger.info("获取商户配置");
//            MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
            MerchantSetting merchantSetting = redisCacheCommonCompoment.merchantSettingCache.getOne(tradeObjectSquare.getMerId());

            logger.info("获取商户配置成功"+JsonUtils.objectToJsonNonNull(merchantSetting));

            //验证请求URL
            String  payURL = channelInfo.getPayUrl();
            if(org.springframework.util.StringUtils.isEmpty(payURL)) new PayException("确认商户收单短信时，请求URL不完整，请查看！");

            //4.获取进件信息
            MerchantRegisterCollect merchantRegisterCollect = recordPaymentSquareService.searchMerchantRegisterCollectToSms(extraChannelInfo,payOrder,tradeObjectSquare);

            logger.info("获取商户进件记录成功"+JsonUtils.objectToJsonNonNull(merchantRegisterCollect));
            //封装请求所需要的参数
            SquareTrade trade = new SquareTrade();
            trade.setMerchantInfo(merchantInfo);
            trade.setChannelInfo(channelInfo);
            trade.setPayOrder(payOrder);
            trade.setMerchantCard(merchantCard);
            trade.setTradeObjectSquare(tradeObjectSquare);
            trade.setMerchantRegisterCollect(merchantRegisterCollect);

            //验证url最后一个字符是否是 “/”
            // String tab=payURL.substring(payURL.length()-1);
            //发送请求
            String result=HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(),payURL,JsonUtils.objectToJsonNonNull(trade));
            if(org.springframework.util.StringUtils.isEmpty(result)) throw new PayException("确认商户收单短信时，获取result信息为空");
            logger.info("【###########确认商户短信###########】请求结果为：result=[{}]",result);
            //取出必要的参数和设置必要的参数
            BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
            Short status=bankResult.getStatus();
            String param=bankResult.getParam();
            String  saveResult=JsonUtils.objectToJson(bankResult);
            systemOrderTrack.setTradeResult(saveResult);
            systemOrderTrack.setOrderTrackStatus((int)status);
            //返回处理结果
            String returnJson = this.resultToString(bankResult,merchantInfo,trade,2);
            logger.info("收单短信验证码返回："+returnJson);
            return  returnJson;
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
        //验签
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        // 获取商户配置
        logger.info("获取商户配置");
        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
        logger.info("获取商户配置成功"+JsonUtils.objectToJsonNonNull(merchantSetting));
//        checkMd5(tradeObjectSquare,merchantInfo.getSecretKey());
        // 商户风控对象
        logger.info("获取商户风控对象");
        MerchantQuotaRisk merchantQuotaRisk = recordPaymentSquareService.getMerchantQuotaRiskByMerId(tradeObjectSquare.getMerId());
        if (merchantQuotaRisk==null){
            throw new PayException("商户风控对象不存在",2004);
        }
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
        MerchantRegisterInfo merchantRegisterInfo=  recordPaymentSquareService.getMerchantRegisterInfo(extraChannelInfo,extraChannelInfos);
        logger.info("获取商户进件信息"+JsonUtils.objectToJsonNonNull(merchantRegisterInfo));
        MerchantRegisterCollect merchantRegisterCollect=recordPaymentSquareService.searchMerchantRegisterCollect(extraChannelInfo,merchantRegisterInfo,tradeObjectSquare);
        logger.info("获取商户进件记录成功"+JsonUtils.objectToJsonNonNull(merchantRegisterInfo));
        systemOrderTrack.setOrderId(payOrder.getPayId());
        // 保存结算业务收款账号信息
        recordPaymentSquareService.saveOrUpadateCardHolderInfo(tradeObjectSquare, payOrder);
        recordPaymentSquareService.saveOrUpadatePayProductDetail(tradeObjectSquare, payOrder);
        getOrderRiskQuotaDataService.analysis(channelInfo, payOrder, merchantQuotaRisk);


        trade.setPayOrder(payOrder);
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantRegisterCollect(merchantRegisterCollect);
        trade.setMerchantInfo(merchantInfo);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setChannelInfo(channelInfo);
        trade.setMerchantInfo(merchantInfo);
        return trade;
    }



    public void afterPay(SquareTrade trade, BankResult result) throws Exception {
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

    private String getTradeReturnJson(SquareTrade trade, BankResult result)  {
        MerchantCard merchantCard = trade.getMerchantCard();
//        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = merchantCard.getMerId();
        String merOrderId = merchantCard.getMerOrderId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();
        MerchantInfo merchantInfo = merchantInfoService.selectByMerId(merId);
        String returnJson = this.resultToString(result,merchantInfo,trade,3);
        return returnJson;
    }

    private SquareTrade getConfirmPayParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {


        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        logger.info("获取商户信息"+JsonUtils.objectToJsonNonNull(merchantInfo));
        //验签
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        PayOrder payOrder = recordPaymentSquareService.getProcessingPayOrderByMerOrderId(tradeObjectSquare,tradeObjectSquare.getMerOrderId());
        logger.info("获取订单信息"+JsonUtils.objectToJsonNonNull(payOrder));
        ChannelInfo channelInfo = recordPaymentSquareService.getChannelInfo(payOrder.getChannelId());
        logger.info("获取商户通道信息"+JsonUtils.objectToJsonNonNull(channelInfo));
        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByOrgId(channelInfo.getOrganizationId(),SystemConstant.BONDCARD);
        logger.info("获取商户附属通道信息"+JsonUtils.objectToJsonNonNull(extraChannelInfo));
        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndTermIdAndExtraId(tradeObjectSquare.getMerId(),tradeObjectSquare.getTerminalMerId(),extraChannelInfo.getExtraChannelId());
        logger.info("获取持卡人信息"+JsonUtils.objectToJsonNonNull(merchantCard));


        MerchantRegisterCollect merchantRegisterCollect = recordPaymentSquareService.searchMerchantRegisterCollectToSms(extraChannelInfo,payOrder,tradeObjectSquare);
        logger.info("获取商户进件记录成功"+JsonUtils.objectToJsonNonNull(merchantRegisterCollect));

        MerchantChannelHistory wasUseChannel = recordPaymentSquareService.getLastUseChannel(tradeObjectSquare);

        SquareTrade trade = new SquareTrade();
        trade.setMerchantChannelHistory(wasUseChannel);
        trade.setMerchantRegisterCollect(merchantRegisterCollect);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setChannelInfo(channelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setPayOrder(payOrder);
        trade.setMerchantInfo(merchantInfo);

        return trade;
    }
    private BankResult confirmPay(SquareTrade trade) throws PayException {
        String others = trade.getChannelInfo().getOthers();
        logger.info("支付确认请求参数"+JsonUtils.objectToJsonNonNull(trade));
        logger.info("发送支付确认请求地址"+trade.getChannelInfo().getPayUrl());
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), trade.getChannelInfo().getPayUrl(), JsonUtils.objectToJsonNonNull(trade));
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

//    public String toConfirmPay(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
//        SquareTrade trade = getConfirmPayParam(systemOrderTrack, tradeObjectSquare);
//        systemOrderTrack.setOrderId(trade.getPayOrder().getPayId());
//        BankResult result = confirmPay(trade);
//        afterPay(trade, result);
//        String bondReturnJson = getTradeReturnJson(trade, result);
//        logger.info("收单支付确认返回："+bondReturnJson);
//        return bondReturnJson;
//    }
    //    public String toPayment(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
//        SquareTrade trade = getTrade(systemOrderTrack, tradeObjectSquare);
//        BankResult result = toPay(trade);
////        BankResult result = new BankResult();
////        result.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
////        result.setBankResult("sccess");
////        result.setParam("asd");
//        afterPay(trade,result);
//        String returnJson = getTradeReturnJson(trade, result);
//        logger.info("收单申请返回："+returnJson);
//        return returnJson;
//    }
//    private BankResult toPay(SquareTrade trade) throws PayException {
//        logger.info(JsonUtils.objectToJsonNonNull(trade));
//        String url = trade.getChannelInfo().getPayUrl();
//        logger.info("收单请求地址"+url);
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), url, JsonUtils.objectToJsonNonNull(trade));
//        logger.info("收单请求返回"+result);
//
//        // 处理接口返回信息
//        if (StringUtils.isBlank(result)) {
//            throw new PayException("Cross请求发生错误！URL：" + trade.getChannelInfo().getPayUrl(), 4001);
//        }
//        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
//        if (bankResult != null) {
//            return bankResult;
//        } else {
//            throw new PayException("BankResultJson转BankResult异常", 6000);
//        }
//    }
}
