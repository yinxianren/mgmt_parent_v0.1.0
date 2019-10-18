package com.rxh.service.oldKuaijie;

import com.rxh.cache.ehcache.MerchantSquareSettingCache;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractCommonService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AlliancePayPamentBondCardService extends AbstractCommonService {
    private final static Logger logger = LoggerFactory.getLogger(AlliancePayPamentBondCardService.class);

    private final RecordPaymentSquareService recordPaymentSquareService;
    private final MerchantSquareSettingCache merchantSquareSettingCache;

    @Autowired
    public AlliancePayPamentBondCardService(RecordPaymentSquareService recordPaymentSquareService, MerchantSquareSettingCache merchantSquareSettingCache) {
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.merchantSquareSettingCache = merchantSquareSettingCache;
    }

    @Autowired
    private MerchantInfoService merchantInfoService;


    public String toBondCard(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        SquareTrade trade = getBondParam(systemOrderTrack, tradeObjectSquare);
        BankResult result = toBond(trade);
        updateBondStatus(trade, result);
        String returnJson = getBondReturnJson(trade, result);
        logger.info("绑卡申请返回信息："+returnJson);
        return returnJson;
    }

    public SquareTrade getBondParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        logger.info("绑卡申请");
        List<MerchantCard> allMerchantCard = recordPaymentSquareService.getAllMerchantCard(tradeObjectSquare.getMerId(), tradeObjectSquare.getTerminalMerId(), SystemConstant.SUCCESS);
        // 查询商户信息
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        // 获取商户配置
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        MerchantSetting merchantSetting = merchantSquareSettingCache.getMerchantSettingByMerId(tradeObjectSquare.getMerId());
        Map<String, Object> extraChannelInfos = recordPaymentSquareService.getChannelInfosByPayType(merchantSetting, tradeObjectSquare, allMerchantCard);
        MerchantCard merchantCard = recordPaymentSquareService.insertMerchantCard(tradeObjectSquare, extraChannelInfos);
        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfo(extraChannelInfos, merchantCard);
        MerchantRegisterInfo merchantRegisterInfo=  recordPaymentSquareService.getMerchantRegisterInfo(extraChannelInfo,extraChannelInfos);
        MerchantRegisterCollect merchantRegisterCollect= recordPaymentSquareService.getMerchantRegisterCollectByMap(extraChannelInfos,extraChannelInfo);
        SquareTrade trade = new SquareTrade();
        //封装交易参数
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantCard(merchantCard);
//        trade.setChannelInfo(channelInfo);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setMerOrderId(tradeObjectSquare.getMerOrderId());
        trade.setMerchantInfo(merchantInfo);
        trade.setMerchantRegisterCollect(merchantRegisterCollect);
        return trade;
    }


    private void checkBondMd5(TradeObjectSquare tradeObjectSquare, String secretKey) throws PayException {
        Map<String, Object> map = new TreeMap<>();
//        map.put("bizType",tradeObjectSquare.getBizType());
        map.put("charset",tradeObjectSquare.getCharset());
        map.put("signType",tradeObjectSquare.getSignType());
        map.put("merId",tradeObjectSquare.getMerId());
        map.put("merOrderId",tradeObjectSquare.getMerOrderId());
        map.put("userName",tradeObjectSquare.getUserName());
        map.put("identityType",tradeObjectSquare.getIdentityType());
        map.put("bankCardType",tradeObjectSquare.getBankCardType());
        map.put("identityNum",tradeObjectSquare.getIdentityNum());
        map.put("bankCardNum",tradeObjectSquare.getBankCardNum());
        map.put("bankCardPhone",tradeObjectSquare.getBankCardPhone());
        map.put("bankCode",tradeObjectSquare.getBankCode());
        if (tradeObjectSquare.getValidDate()!=null){
        map.put("validDate",tradeObjectSquare.getValidDate());
        }
        if (tradeObjectSquare.getSecurityCode()!=null) {
        map.put("securityCode",tradeObjectSquare.getSecurityCode());
        }
//        map.put("payFee",tradeObjectSquare.getPayFee());
//        map.put("backFee",tradeObjectSquare.getBackFee());
//        map.put("backCardNum",tradeObjectSquare.getBackCardNum());
//        map.put("backBankCode",tradeObjectSquare.getBackBankCode());
//        map.put("backCardPhone",tradeObjectSquare.getBackCardPhone());
        map.put("terminalMerId",tradeObjectSquare.getTerminalMerId());
        map.put("terminalMerName",tradeObjectSquare.getTerminalMerName());
        map.put("returnUrl",tradeObjectSquare.getReturnUrl());
        map.put("noticeUrl",tradeObjectSquare.getNoticeUrl());
        String md5Info = CheckMd5Utils.getMd5SignWithKey(map,secretKey).toUpperCase();
        if (md5Info.equals(tradeObjectSquare.getSignMsg().toUpperCase())){
            throw new PayException("签名验证不正确",1023);
        }
    }
    private void checkGetBondSmsMd5(TradeObjectSquare tradeObjectSquare, String secretKey) throws PayException {
        Map<String, Object> map = new TreeMap<>();
//        map.put("bizType",tradeObjectSquare.getBizType());
        map.put("charset",tradeObjectSquare.getCharset());
        map.put("signType",tradeObjectSquare.getSignType());
        map.put("merId",tradeObjectSquare.getMerId());
        map.put("merOrderId",tradeObjectSquare.getMerOrderId());
        map.put("userName",tradeObjectSquare.getUserName());
        map.put("identityType",tradeObjectSquare.getIdentityType());
        map.put("bankCardType",tradeObjectSquare.getBankCardType());
        map.put("identityNum",tradeObjectSquare.getIdentityNum());
        map.put("bankCardNum",tradeObjectSquare.getBankCardNum());
        map.put("bankCardPhone",tradeObjectSquare.getBankCardPhone());
        map.put("bankCode",tradeObjectSquare.getBankCode());
        if (tradeObjectSquare.getValidDate()!=null){
        map.put("validDate",tradeObjectSquare.getValidDate());
        }
        if (tradeObjectSquare.getSecurityCode()!=null) {
        map.put("securityCode",tradeObjectSquare.getSecurityCode());
        }
//        map.put("payFee",tradeObjectSquare.getPayFee());
//        map.put("backFee",tradeObjectSquare.getBackFee());
//        map.put("backCardNum",tradeObjectSquare.getBackCardNum());
//        map.put("backBankCode",tradeObjectSquare.getBackBankCode());
//        map.put("backCardPhone",tradeObjectSquare.getBackCardPhone());
        map.put("terminalMerId",tradeObjectSquare.getTerminalMerId());
        map.put("terminalMerName",tradeObjectSquare.getTerminalMerName());
        map.put("returnUrl",tradeObjectSquare.getReturnUrl());
        map.put("noticeUrl",tradeObjectSquare.getNoticeUrl());
        String md5Info = CheckMd5Utils.getMd5SignWithKey(map,secretKey).toUpperCase();
        if (md5Info.equals(tradeObjectSquare.getSignMsg().toUpperCase())){
            throw new PayException("签名验证不正确",1023);
        }
    }
    private void checkConfirmBondMd5(TradeObjectSquare tradeObjectSquare, String secretKey) throws PayException {
        Map<String, Object> map = new TreeMap<>();
//        map.put("bizType",tradeObjectSquare.getBizType());
        map.put("charset",tradeObjectSquare.getCharset());
        map.put("signType",tradeObjectSquare.getSignType());
        map.put("merId",tradeObjectSquare.getMerId());
        map.put("merOrderId",tradeObjectSquare.getMerOrderId());
        map.put("userName",tradeObjectSquare.getUserName());
        map.put("identityType",tradeObjectSquare.getIdentityType());
        map.put("bankCardType",tradeObjectSquare.getBankCardType());
        map.put("identityNum",tradeObjectSquare.getIdentityNum());
        map.put("bankCardNum",tradeObjectSquare.getBankCardNum());
        map.put("bankCardPhone",tradeObjectSquare.getBankCardPhone());
        map.put("bankCode",tradeObjectSquare.getBankCode());
        if (tradeObjectSquare.getValidDate()!=null){
        map.put("validDate",tradeObjectSquare.getValidDate());
        }
        if (tradeObjectSquare.getSecurityCode()!=null) {
        map.put("securityCode",tradeObjectSquare.getSecurityCode());
        }
        map.put("payFee",tradeObjectSquare.getPayFee());
        map.put("backFee",tradeObjectSquare.getBackFee());
        map.put("backCardNum",tradeObjectSquare.getBackCardNum());
        map.put("backBankCode",tradeObjectSquare.getBackBankCode());
        map.put("backCardPhone",tradeObjectSquare.getBackCardPhone());
        map.put("smsCode",tradeObjectSquare.getSmsCode());
        map.put("terminalMerId",tradeObjectSquare.getTerminalMerId());
        map.put("terminalMerName",tradeObjectSquare.getTerminalMerName());
        map.put("returnUrl",tradeObjectSquare.getReturnUrl());
        map.put("noticeUrl",tradeObjectSquare.getNoticeUrl());
        String md5Info = CheckMd5Utils.getMd5SignWithKey(map,secretKey).toUpperCase();
        if (md5Info.equals(tradeObjectSquare.getSignMsg().toUpperCase())){
            throw new PayException("签名验证不正确",1023);
        }
    }

    private BankResult toBond(SquareTrade trade) throws PayException {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
//
        logger.info("发送绑卡请求地址"+extraChannelInfo.getUrl());
        logger.info("发送绑卡请求至Cross参数"+JsonUtils.objectToJsonNonNull(trade));
//        logger.info("发送绑卡请求"+param.get("cardApply").toString());
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://localhost:8200/merchantBindCard/bindCard", JsonUtils.objectToJsonNonNull(trade));
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl(), JsonUtils.objectToJsonNonNull(trade));
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl() + param.get("cardApply").toString(), JsonUtils.objectToJsonNonNull(trade));
        logger.info("绑卡请求返回"+result);
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


    private void updateBondStatus(SquareTrade trade, BankResult result) {
        MerchantCard merchantCard = trade.getMerchantCard();
        merchantCard.setStatus(Integer.valueOf(result.getStatus()));
        merchantCard.setResult(result.getBankData());
        merchantCard.setBackData(result.getParam());
        recordPaymentSquareService.updateMerchantCard(merchantCard);

    }

    private String getBondReturnJson(SquareTrade trade, BankResult result)  {
        MerchantCard merchantCard = trade.getMerchantCard();
        String merId = merchantCard.getMerId();
        String merOrderId = merchantCard.getMerOrderId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();
        MerchantInfo merchantInfo = merchantInfoService.selectByMerId(merId);
        return this.resultToString(result,merchantInfo,trade,1);
    }


    public String toConfirmBond(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        SquareTrade trade = getConfirmBondParam(systemOrderTrack, tradeObjectSquare);
        BankResult result = confirmBond(trade);
        updateBondStatus(trade, result);
        String bondReturnJson = getBondReturnJson(trade, result);
        logger.info("绑卡确认返回信息："+bondReturnJson);
        return bondReturnJson;
    }
    private SquareTrade getConfirmBondParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        logger.info("绑卡确认");

        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        //校验签名
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermId(tradeObjectSquare.getMerId(), tradeObjectSquare.getMerOrderId(),tradeObjectSquare.getTerminalMerId(),SystemConstant.BANK_STATUS_PENDING_PAYMENT);
        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByExtraChannelId(merchantCard.getExtraChannelId());
        MerchantRegisterInfo merchantRegisterInfo = recordPaymentSquareService.searchMerchantRegisterInfoByParam(tradeObjectSquare);
        MerchantRegisterCollect merchantRegisterCollect=recordPaymentSquareService.searchMerchantRegisterCollect(extraChannelInfo,merchantRegisterInfo,tradeObjectSquare);
        SquareTrade trade = new SquareTrade();
        trade.setMerchantRegisterCollect(merchantRegisterCollect);
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setMerchantInfo(merchantInfo);
        return trade;
    }


    private BankResult confirmBond(SquareTrade trade) throws PayException {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        logger.info("发送绑卡确认请求地址"+extraChannelInfo.getUrl());
        logger.info("发送绑卡确认请求至Cross参数"+JsonUtils.objectToJsonNonNull(trade));
//        logger.info("发送绑卡确认请求"+map.get("cardConfirm").toString());
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl()  , JsonUtils.objectToJsonNonNull(trade));
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




    public String reGetBondCode(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        SquareTrade trade = getBondCodeParam(systemOrderTrack, tradeObjectSquare);
        BankResult result = toGetBondCode(trade);
        String bondCodeReturnJson = getBondCodeReturnJson(trade, result);
        logger.info("重新获取绑卡验证码返回："+bondCodeReturnJson);
        return bondCodeReturnJson;
    }
    private SquareTrade getBondCodeParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        logger.info("重新获取绑卡验证码");
        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermId(tradeObjectSquare.getMerId(), tradeObjectSquare.getMerOrderId(),tradeObjectSquare.getTerminalMerId(),SystemConstant.BANK_STATUS_PENDING_PAYMENT);
        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByExtraChannelId(merchantCard.getExtraChannelId());
        MerchantInfo merchantInfo = recordPaymentSquareService.getMerchantInfoByMerId(tradeObjectSquare.getMerId());
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        MerchantRegisterInfo merchantRegisterInfo = recordPaymentSquareService.searchMerchantRegisterInfoByParam(tradeObjectSquare);
        MerchantRegisterCollect merchantRegisterCollect=recordPaymentSquareService.searchMerchantRegisterCollect(extraChannelInfo,merchantRegisterInfo,tradeObjectSquare);
        SquareTrade trade = new SquareTrade();
        trade.setMerchantRegisterCollect(merchantRegisterCollect);
        trade.setMerchantRegisterInfo(merchantRegisterInfo);
        trade.setMerchantCard(merchantCard);
        trade.setExtraChannelInfo(extraChannelInfo);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setMerchantInfo(merchantInfo);
        return trade;
    }

    private BankResult toGetBondCode(SquareTrade trade) throws PayException {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        logger.info("发送获取绑卡验证码确认请求地址"+extraChannelInfo.getUrl() );
        logger.info("发送获取绑卡验证码请求至Cross参数"+JsonUtils.objectToJsonNonNull(trade));
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl() , JsonUtils.objectToJsonNonNull(trade));
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://localhost:8200/allinPay/reGetCode", JsonUtils.objectToJsonNonNull(trade));
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
        MerchantInfo merchantInfo = merchantInfoService.selectByMerId(merOrderId);
        return this.resultToString(result,merchantInfo,trade,1);
    }
}
