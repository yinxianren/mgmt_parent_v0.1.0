package com.rxh.service.kuaijie;


import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractCommonService;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 *
 * 描述：
 * @author panda
 * @date 20190718
 *
 *
 */
@Service
public class KuaiJiePayPaymentBondCardService extends AbstractCommonService {





    /**
     *
     * @param trade
     * @return
     * @throws PayException
     */
    public BankResult confirmBond(SquareTrade trade) throws PayException {
        String merId=trade.getMerchantInfo().getMerId();
        if (trade.getMerchantCard().getStatus() == ((int)SystemConstant.BANK_STATUS_SUCCESS)){
            BankResult bankResult = new BankResult();
            bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
            bankResult.setBankResult(format("【绑卡确认】商户号：%s,绑卡成功，请勿重新绑卡",merId));
            return bankResult;
        }
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.getMessage();
        }
        String ip = address.getHostAddress();
        trade.setIp(ip);
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl()  , JsonUtils.objectToJsonNonNull(trade));
        logger.info("【绑卡确认】商户号：{},cross返回参数：{}",merId,result);
        isNull(result,format("【绑卡确认】商户号：%s,绑卡请求交互异常",merId),"RXH00013");
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        isNull(bankResult,format("【绑卡确认】商户号：%s,BankResultJson转BankResult异常",merId),"RXH00013");
        return bankResult;
    }
    /**
     *
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @return
     * @throws Exception
     */
    public SquareTrade getConfirmBondParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        String merId=tradeObjectSquare.getMerId();
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【绑卡确认】商户号：%s,未获取到商户信息",merId),"RXH00013");
        //校验签名
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());

        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermId(merId, tradeObjectSquare.getMerOrderId(),tradeObjectSquare.getTerminalMerId(),SystemConstant.BANK_STATUS_PENDING_PAYMENT);
        if(merchantCard == null){
            MerchantCard merchantCard1 = recordPaymentSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermId(merId, tradeObjectSquare.getMerOrderId(),tradeObjectSquare.getTerminalMerId(),SystemConstant.BANK_STATUS_SUCCESS);
           if (merchantCard1 != null)
            return  new SquareTrade()
                    .lsetMerchantCard(merchantCard1)
                    .lsetTradeObjectSquare(tradeObjectSquare)
                    .lsetMerchantInfo(merchantInfo);
        }
        isNull(merchantCard,format("【绑卡确认】商户号：%s,未获取到绑卡信息",merId),"RXH00013");
        if (!(merchantCard.getIdentityNum().equals(tradeObjectSquare.getIdentityNum()) && merchantCard.getCardNum().equals(tradeObjectSquare.getBankCardNum()))){
            throw new PayException(format("【绑卡确认】商户号：%s,订单号：%s,确认信息与申请信息不一致，请核对",merId,tradeObjectSquare.getMerOrderId()),"RXH00013");
        }

//        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByExtraChannelId(merchantCard.getExtraChannelId());
        ExtraChannelInfo extraChannelInfo = redisCacheCommonCompoment.extraChannelInfoCache.getOne(merchantCard.getExtraChannelId());
        isNull(extraChannelInfo,format("【绑卡确认】商户号：%s,未获取到附属通道信息",merId),"RXH00013");
//        MerchantRegisterInfo merchantRegisterInfo = recordPaymentSquareService.searchMerchantRegisterInfoByParam(tradeObjectSquare);
        MerchantRegisterInfo merchantRegisterInfo = redisCacheCommonCompoment.merchantRegisterInfoCache.getAll()
                .stream()
                .filter( t-> t.getMerId().equals(merId)
                        && t.getTerminalMerId().equals(tradeObjectSquare.getTerminalMerId()))
                .findAny()
                .orElse(null);

        isNull(merchantRegisterInfo,format("【绑卡确认】商户号：%s,未获取到商户进件信息",merId),"RXH00013");
        MerchantRegisterCollect merchantRegisterCollect=recordPaymentSquareService.searchMerchantRegisterCollect(extraChannelInfo,merchantRegisterInfo,tradeObjectSquare);
        isNull(merchantRegisterCollect,format("【绑卡确认】商户号：%s,未获取到商户进件附属信息",merId),"RXH00013");
        return  new SquareTrade()
                .lsetMerchantRegisterCollect(merchantRegisterCollect)
                .lsetMerchantRegisterInfo(merchantRegisterInfo)
                .lsetMerchantCard(merchantCard)
                .lsetExtraChannelInfo(extraChannelInfo)
                .lsetTradeObjectSquare(tradeObjectSquare)
                .lsetMerchantInfo(merchantInfo);
    }
    /**
     *
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @return
     * @throws PayException
     */
    public SquareTrade getBondCodeParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        String merId=tradeObjectSquare.getMerId();
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【重新获取绑卡验证码】商户号：%s,未获取到商户信息",merId),"RXH00013");
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());

        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermId(tradeObjectSquare.getMerId(), tradeObjectSquare.getMerOrderId(),tradeObjectSquare.getTerminalMerId(),SystemConstant.BANK_STATUS_PENDING_PAYMENT);
        isNull(merchantCard,format("【重新获取绑卡验证码】商户号：%s,未获取到商户卡信息",merId),"RXH00013");
//        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfoByExtraChannelId(merchantCard.getExtraChannelId());
        ExtraChannelInfo extraChannelInfo = redisCacheCommonCompoment.extraChannelInfoCache.getOne(merchantCard.getExtraChannelId());
        isNull(extraChannelInfo,format("【重新获取绑卡验证码】商户号：%s,未获取到商户进件附属信息",merId),"RXH00013");
//        MerchantRegisterInfo merchantRegisterInfo = recordPaymentSquareService.searchMerchantRegisterInfoByParam(tradeObjectSquare);
        MerchantRegisterInfo merchantRegisterInfo = redisCacheCommonCompoment.merchantRegisterInfoCache.getAll()
                .stream()
                .filter( t-> t.getMerId().equals(tradeObjectSquare.getMerId())
                        &&  t.getTerminalMerId().equals(tradeObjectSquare.getTerminalMerId())
                )
                .distinct()
                .findAny()
                .orElse(null);

        isNull(merchantRegisterInfo,format("【重新获取绑卡验证码】商户号：%s,未获取到商户进件信息",merId),"RXH00013");
        MerchantRegisterCollect merchantRegisterCollect=recordPaymentSquareService.searchMerchantRegisterCollect(extraChannelInfo,merchantRegisterInfo,tradeObjectSquare);

        isNull(merchantRegisterCollect,format("【重新获取绑卡验证码】商户号：%s,未获取到商户进件附属信息",merId),"RXH00013");
        return  new SquareTrade()
                .lsetMerchantRegisterCollect(merchantRegisterCollect)
                .lsetMerchantRegisterInfo(merchantRegisterInfo)
                .lsetMerchantCard(merchantCard)
                .lsetExtraChannelInfo(extraChannelInfo)
                .lsetTradeObjectSquare(tradeObjectSquare)
                .lsetMerchantInfo(merchantInfo);
    }

    /**
     *
     * @param squareTrade
     * @param bankResult
     * @return
     */
    public String getBondReturnJson(SquareTrade squareTrade, BankResult bankResult)  {
        String merId=squareTrade.getMerchantInfo().getMerId();
        MerchantInfo merchantInfo = merchantInfoService.selectByMerId(merId);
        int status = bankResult.getStatus();
        String resultCode = "RXH00013";
        if (!StringUtils.isBlank(bankResult.getBankCode())){
             if (bankResult.getBankCode().equals("300")){
                 status = SystemConstant.BANK_STATUS_FAIL;
                 resultCode = "RXH00004";
             }else if (bankResult.getBankCode().equals("301")){
                 status = SystemConstant.BANK_STATUS_FAIL;
                 resultCode = "RXH00003";
             }else if (bankResult.getBankCode().equals("302")){
                 status = SystemConstant.BANK_STATUS_FAIL;
                 resultCode = "RXH00005";
             }
        }
        PayTreeMap<String,Object> map = new PayTreeMap();
        map.lput("merId",merchantInfo.getMerId())
                .lput("merOrderId",squareTrade.getMerOrderId())
                .lput("status",status)
                .lput("msg",bankResult.getBankResult())
                .lput("signMsg",getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        if (bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS)
            map.lput("resultCode","RXH00000");
        else
            map.lput("resultCode",resultCode);
        return JSONObject.toJSONString(map);
    }

    /**
     *
     * @param squareTrade
     * @param bankResult
     * @return
     */
    public String getBondReturnJsonS(SquareTrade squareTrade, BankResult bankResult)  {
        String merId=squareTrade.getMerchantInfo().getMerId();
        MerchantInfo merchantInfo = merchantInfoService.selectByMerId(merId);
        int status = bankResult.getStatus();
        String resultCode = "RXH00013";
        if (!StringUtils.isBlank(bankResult.getBankCode())){
            if (bankResult.getBankCode().equals("300")){
                status = SystemConstant.BANK_STATUS_FAIL;
                resultCode = "RXH00004";
            }else if (bankResult.getBankCode().equals("301")){
                status = SystemConstant.BANK_STATUS_FAIL;
                resultCode = "RXH00003";
            }else if (bankResult.getBankCode().equals("302")){
                status = SystemConstant.BANK_STATUS_FAIL;
                resultCode = "RXH00005";
            }
        }
        PayTreeMap<String,Object> map = new PayTreeMap();
        map.lput("merId",merchantInfo.getMerId())
                .lput("merOrderId",squareTrade.getMerOrderId())
                .lput("status",status)
                .lput("msg",bankResult.getBankResult())
                .lput("signMsg",getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        if (bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS)
            map.lput("resultCode","RXH00014");
        else
            map.lput("resultCode",resultCode);
        return JSONObject.toJSONString(map);
    }

    /**
     *
     * @param trade
     * @return
     * @throws PayException
     */
    public BankResult toBond(SquareTrade trade) throws PayException {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        String merId=trade.getMerchantInfo().getMerId();
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.getMessage();
        }
        String ip = address.getHostAddress();
        trade.setIp(ip);
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl(), JsonUtils.objectToJsonNonNull(trade));
        logger.info("【绑卡请求】商户号：{},cross返回参数：{}",merId,result);
        isNull(result,format("【绑卡请求】商户号：%s,绑卡请求交互异常",merId),"RXH00013");
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        isNull(bankResult,format("【绑卡请求】商户号：%s,BankResultJson转BankResult异常",merId),"RXH00013");
        return bankResult;
    }

    /**
     *
     * @param trade
     * @return
     * @throws PayException
     */
    public BankResult toGetBondCode(SquareTrade trade) throws PayException {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        String merId=trade.getMerchantInfo().getMerId();
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.getMessage();
        }
        String ip = address.getHostAddress();
        trade.setIp(ip);
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl() , JsonUtils.objectToJsonNonNull(trade));
        logger.info("【重新获取绑卡验证码】商户号：{},cross返回参数：{}",merId,result);
        isNull(result,format("【重新获取绑卡验证码】商户号：%s,绑卡请求交互异常",merId),"RXH00013");
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        isNull(bankResult,format("【重新获取绑卡验证码】商户号：%s,BankResultJson转BankResult异常",merId),"RXH00013");
        return bankResult;
    }

    /**
     *  更新redis
     * @param merchantCard
     */
    public void updateBondStatus(MerchantCard merchantCard) throws PayException {
        paymentRecordSquareService.updateMerchantCard(merchantCard);
    }
    /**
     *
     * @param systemOrderTrack
     * @param tradeObjectSquare
     * @return
     * @throws PayException
     */
    public SquareTrade getBondParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {

        String merId=tradeObjectSquare.getMerId();
        String terminalMerId=tradeObjectSquare.getTerminalMerId();
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【绑定银行卡】商户号：%s,未获取到该商户信息！",merId),"RXH00013");
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        MerchantCard merchantCard1 =  new MerchantCard();
        merchantCard1.setMerOrderId(tradeObjectSquare.getMerOrderId());
        merchantCard1 = merchantCardService.search(merchantCard1);
        isNotNull(merchantCard1,format("【绑定银行卡】商户号：%s,该订单已存在",merId),"RXH00009");
        MerchantCard parm = new MerchantCard();
        parm.setCardNum(tradeObjectSquare.getBankCardNum());
        parm.setTerminalMerId(tradeObjectSquare.getTerminalMerId());
        parm.setStatus((int)SystemConstant.BANK_STATUS_SUCCESS);
        MerchantCard merchantCard2 = merchantCardService.search(parm);
        if (merchantCard2 != null){
            SquareTrade squareTrade = new SquareTrade().lsetMerchantCard(merchantCard2).lsetMerchantInfo(merchantInfo);
            squareTrade.setMerOrderId(tradeObjectSquare.getMerOrderId());
            return squareTrade;
        }
        List<MerchantCard> allMerchantCard =  redisCacheCommonCompoment.merchantCardCache.getMore(merId,terminalMerId);
//        isNull(allMerchantCard,format("【绑定银行卡】商户号：%s,未获取到商户卡信息",merId),"RXH00013");
        MerchantSetting merchantSetting = redisCacheCommonCompoment.merchantSettingCache.getOne(merId);
        isNull(merchantSetting,format("【绑定银行卡】商户号无可用绑定通道：%s,未获取到商户设置信息",merId),"RXH00013");
        Map<String, Object> extraChannelInfos = recordPaymentSquareService.getChannelInfosByPayType(merchantSetting, tradeObjectSquare, allMerchantCard);
        isNotElement(extraChannelInfos,format("【绑定银行卡】商户号：%s,未获取到相关附属通道信息",merId),"RXH00013");
        //保存绑卡信息
        MerchantCard merchantCard = recordPaymentSquareService.insertMerchantCard(tradeObjectSquare, extraChannelInfos);
        isNull(merchantCard,format("【绑定银行卡】商户号：%s,保存绑卡信息操作异常",merId),"RXH00013");
        ExtraChannelInfo extraChannelInfo = recordPaymentSquareService.getExtraChannelInfo(extraChannelInfos, merchantCard);
        isNull(extraChannelInfo,format("【绑定银行卡】商户号：%s,操作配置卡绑定四要素认证等接口信息异常",merId),"RXH00013");
        MerchantRegisterInfo merchantRegisterInfo=  recordPaymentSquareService.getMerchantRegisterInfo(extraChannelInfo,extraChannelInfos);
        if (!merchantRegisterInfo.getIdentityNum().equals(tradeObjectSquare.getIdentityNum())){
            throw new PayException(format("【绑定银行卡】商户号：%s,操作商户进件信息异常,请先进行进件操作",merId),"RXH00013");
        }
        isNull(merchantRegisterInfo,format("【绑定银行卡】商户号：%s,操作商户进件信息异常",merId),"RXH00013");
        MerchantRegisterCollect merchantRegisterCollect= recordPaymentSquareService.getMerchantRegisterCollectByMap(extraChannelInfos,extraChannelInfo);
        isNull(merchantRegisterCollect,format("【绑定银行卡】商户号：%s,操作进件附属信息表异常",merId),"RXH00013");
        //封装交易参数
        return new SquareTrade().lsetMerchantRegisterInfo(merchantRegisterInfo)
                .lsetMerchantCard(merchantCard)
                .lsetExtraChannelInfo(extraChannelInfo)
                .lsetTradeObjectSquare(tradeObjectSquare)
                .lsetMerOrderId(tradeObjectSquare.getMerOrderId())
                .lsetMerchantInfo(merchantInfo)
                .lsetMerchantRegisterCollect(merchantRegisterCollect);
    }


    /**
     *
     */
    public  Map<String, ParamRule> alliancePayBondCardValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
//            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("userName", new ParamRule(REQUIRED, 10, 1006));//
            put("identityType", new ParamRule(REQUIRED, 2, 1014));//
            put("bankCardType", new ParamRule(REQUIRED, 2, 1014));//
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));//
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCardPhone", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCode", new ParamRule(REQUIRED, 32, 1013));//
//            put("validDate", new ParamRule(REQUIRED, 6, 1013));//
//            put("securityCode", new ParamRule(REQUIRED, 4, 1013));//
//            put("payFee", new ParamRule(MONEY, 1007));//
//            put("backFee", new ParamRule(MONEY, 1008));//
//            put("backCardNum", new ParamRule(REQUIRED, 19,1008));//
//            put("backBankCode", new ParamRule(REQUIRED, 32,1008));//
//            put("backCardPhone", new ParamRule(REQUIRED, 32,1008));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
        }
    };

    public  TradeObjectSquare alliancePayBondCard(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare =null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            String value = "";
            for (String key : keys) {
                if (tradeInfoMap.get(key) != null){
                    value = tradeInfoMap.get(key).toString();
                }
                validateValue(alliancePayBondCardValidate,key, value);
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(alliancePayBondCardValidate,tradeInfoKeys);
            return tradeObjectSquare;
        } catch (Exception  e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(),"RXH00013");
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }

    /**
     *
     */
    private final static Map<String, ParamRule> allianceConfirmBondValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
//            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("userName", new ParamRule(REQUIRED, 10, 1006));//
            put("identityType", new ParamRule(REQUIRED, 2, 1014));//
            put("bankCardType", new ParamRule(REQUIRED, 2, 1014));//
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));//
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCardPhone", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCode", new ParamRule(REQUIRED, 32, 1013));//
            put("validDate", new ParamRule(REQUIRED, 6, 1013));//
            put("securityCode", new ParamRule(REQUIRED, 4, 1013));//
            put("payFee", new ParamRule(MONEY, 1007));//
            put("backFee", new ParamRule(MONEY, 1008));//
            put("backCardNum", new ParamRule(REQUIRED, 19,1008));//
            put("backBankCode", new ParamRule(REQUIRED, 32,1008));//
            put("backCardPhone", new ParamRule(REQUIRED, 32,1008));//
            put("smsCode", new ParamRule(REQUIRED, 32,1008));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//

        }
    };
    /**
     *
     * @param systemOrderTrack
     * @return
     * @throws PayException
     */
    public  TradeObjectSquare alliancePayConfirmBond(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            String value = "";
            for (String key : keys) {
                if (tradeInfoMap.get(key) != null){
                    value = tradeInfoMap.get(key).toString();
                }
            validateValue(allianceConfirmBondValidate,key, value);
            tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(allianceConfirmBondValidate,tradeInfoKeys);
        } catch (PayException  e) {
            throw new PayException(format("请求报文无法解析:%s",e.getMessage()),"RXH00013");
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
            return tradeObjectSquare;
        }
    }

    /**
     *
     */
    private final static Map<String, ParamRule> allianceGetBondSmsValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
//            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("userName", new ParamRule(REQUIRED, 10, 1006));//
            put("identityType", new ParamRule(REQUIRED, 2, 1014));//
            put("bankCardType", new ParamRule(REQUIRED, 2, 1014));//
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));//
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCardPhone", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCode", new ParamRule(REQUIRED, 32, 1013));//
            put("validDate", new ParamRule(REQUIRED, 6, 1013));//
            put("securityCode", new ParamRule(REQUIRED, 4, 1013));//
//            put("payFee", new ParamRule(MONEY, 1007));//
//            put("backFee", new ParamRule(MONEY, 1008));//
//            put("backCardNum", new ParamRule(REQUIRED, 19,1008));//
//            put("backBankCode", new ParamRule(REQUIRED, 32,1008));//
//            put("backCardPhone", new ParamRule(REQUIRED, 32,1008));//
//            put("smsCode", new ParamRule(REQUIRED, 32,1008));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//

        }
    };

    /**
     *
     * @param systemOrderTrack
     * @return
     * @throws PayException
     */
    public  TradeObjectSquare alliancePayBondSmsCode(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare =null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            String value = "";
            for (String key : keys) {
                if (tradeInfoMap.get(key) != null){
                    value = tradeInfoMap.get(key).toString();
                }
            validateValue(allianceGetBondSmsValidate,key, value);
            tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(allianceGetBondSmsValidate,tradeInfoKeys);
        } catch (PayException  e) {
            throw new PayException(format("请求报文无法解析:%s" , e.getMessage()),"RXH00013");
        }finally {
            if(null != tradeObjectSquare)
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            return tradeObjectSquare;
        }
    }
}
