package com.rxh.service;

import com.alibaba.fastjson.JSONObject;
import com.rxh.activeMQ.TransOrderMQ;
import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.cache.ehcache.MerchantSquareRateCache;
import com.rxh.cache.ehcache.MerchantSquareSettingCache;
import com.rxh.cache.ehcache.RiskQuotaDataCache;
import com.rxh.cache.ehcache.SysConstantCache;
import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayThreadPool;
import com.rxh.payInterface.PayUtil;
import com.rxh.payLock.MerchantsLock;
import com.rxh.pojo.AbstractParamModel;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantBasicInformationRegistration;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.kuaijie.KuaijiePayQueryService;
import com.rxh.service.merchant.MerchantAddCusService;
import com.rxh.service.merchant.MerchantRegisterCollectService;
import com.rxh.service.merchant.MerchantRegisterInfoService;
import com.rxh.service.merchant.MerchantSettingService;
import com.rxh.service.oldKuaijie.GetOrderRiskQuotaDataService;
import com.rxh.service.oldKuaijie.GetOrderWalletService;
import com.rxh.service.oldKuaijie.RecordPaymentSquareService;
import com.rxh.service.square.*;
import com.rxh.service.trading.PayOrderService;
import com.rxh.service.trading.TransOrderService;
import com.rxh.service.wallet.KuaiJiePayService;
import com.rxh.square.pojo.*;
import com.rxh.strategy.PayOrderOptimalChannelStrategy;
import com.rxh.utils.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


/**
 *   从子类中抽取公共的方法
 * @version
 * @date 20190624
 *
 */
public abstract class AbstractCommonService implements PayAssert, PayUtil, PayThreadPool{

    protected  Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected MerchantsLock merchantsLock;
    @Autowired
    protected RedisCacheCommonCompoment redisCacheCommonCompoment;
    @Autowired
    protected MerchantCardService  merchantCardService;
    @Autowired
    protected MerchantInfoService merchantInfoService;
    @Autowired
    protected ChannelInfoService channelInfoService;
    @Autowired
    protected PaymentRecordSquareService paymentRecordSquareService;
    @Autowired
    protected MerchantSettingService merchantSettingService;
    @Autowired
    protected MerchantRegisterCollectService merchantRegisterCollectService;
    @Autowired
    protected MerchantRegisterInfoService merchantRegisterInfoService;
    @Autowired
    protected ExtraChannelInfoService extraChannelInfoService;
    @Autowired
    protected RecordPaymentSquareService recordPaymentSquareService;
    @Autowired
    protected MerchantSquareSettingCache merchantSquareSettingCache;
    @Autowired
    protected TransOrderService transOrderService;
    @Autowired
    protected MerchantSquareRateCache merchantSquareRateCache;
    @Autowired
    protected RiskQuotaDataCache riskQuotaDataCache;
    @Autowired
    protected KuaiJiePayService kuaiJiePayService;
    @Autowired
    protected TransOrderMQ transOrderMQ;
    @Autowired
    protected RiskQuotaDataService riskQuotaDataService;
    @Autowired
    protected PayOrderOptimalChannelStrategy payOrderOptimalChannelStrategy;
    @Resource
    protected KuaijiePayQueryService kuaijiePayQueryService;
    @Autowired
    protected PayOrderService payOrderService;
    @Autowired
    protected MerchantAddCusService merchantAddCusService;
    @Autowired
    protected GetOrderWalletService getOrderWalletService;
    @Autowired
    protected GetOrderRiskQuotaDataService getOrderRiskQuotaDataService;
    @Autowired
    protected SysConstantCache sysConstantCache;
    @Autowired
    protected BankCodeService bankCodeService;


    protected static final String IPV4_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    protected static final String IPV6_REGEX = "^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$";
    protected static final int OTHER = 0; // 可空
    protected static final int REQUIRED = 1; // 必填项
    protected static final int NUMBER = 2; // 数字输入
    protected static final int MONEY = 3;  // 金钱格式
    protected static final int EMAIL = 4; // 邮箱格式
    protected static final int IP = 5;// IP格式
    protected static final int GOOD_LIST = 6; // 货物列表



    /**
     *  请求cross工程
     * @param trade
     * @return
     * @throws PayException
     */
    public BankResult doPostOnCross(SquareTrade trade) throws PayException {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        String merId=trade.getMerchantInfo().getMerId();
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraChannelInfo.getUrl(), JsonUtils.objectToJsonNonNull(trade));
        isBlank(result,format("【请求CROSS】商户号：%s,绑卡请求交互异常",merId));
        BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
        isNull(bankResult,format("【请求CROSS】商户号：%s,BankResultJson转BankResult异常",merId));
        return bankResult;
    }

    /**
     * 获取商户所支持的通道
     * @param merchantSetting
     * @param tradeObjectSquare
     * @return
     */
    protected List<ChannelInfo>  getMerchantSurpportChannel(MerchantSetting merchantSetting, TradeObjectSquare tradeObjectSquare) throws PayException, Exception {
        String merId=tradeObjectSquare.getMerId();
        String merOrderId=tradeObjectSquare.getMerOrderId();
        String organizetionIdStr=merchantSetting.getOrganizationId();
        isBlank(organizetionIdStr,format("【获取商户所支持的通道】商户号：%s,订单号：%s,商户通道未配置",merId,merOrderId));
        String channelIdStr= merchantSetting.getChannelId();
        isBlank(channelIdStr,format("【获取商户所支持的通道】商户号：%s,订单号：%s,商户通道未配置",merId,merOrderId));
        ChannelInfo channelInfo=new ChannelInfo();
        channelInfo.setChannelIds(Arrays.asList(channelIdStr.trim().split(",")));
        channelInfo.setOrganizationIds(Arrays.asList(organizetionIdStr.trim().split(",")));
        channelInfo.setStatus(0);
        if(null !=tradeObjectSquare.getBizType() ) channelInfo.setType(Integer.valueOf(tradeObjectSquare.getBizType()));
        List<ChannelInfo>  channelInfoList=channelInfoService.selectByWhereCondition(channelInfo);
        isNotElement(channelInfoList,format("【获取商户所支持的通道】商户号：%s,订单号：%s,商户通道配置错误",merId,merOrderId));
        return channelInfoList;
    }

    /**
     *   验证绑卡
     * @param systemOrderTrack
     * @return
     * @throws PayException
     */
    public TradeObjectSquare verifyParam(SystemOrderTrack systemOrderTrack, Map<String, ParamRule> paramRuleMap) throws PayException {
        TradeObjectSquare tradeObjectSquare =null;

        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(paramRuleMap,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(paramRuleMap,tradeInfoKeys);
            return tradeObjectSquare;
        } catch (PayException  e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }
    /**
     *  验证参数格式
     * @param map
     * @param key
     * @param value
     * @throws PayException
     */
    protected void validateValue(Map<String, ParamRule> map, String key, String value) throws PayException {
        ParamRule paramRule = map.get(key);
        if (paramRule == null) {
            return;
        }
        switch (paramRule.getType()) {
            case OTHER:
                if (value.length() > paramRule.getMaxLength()) {
                    throw new PayException("报文参数：" + key + " 超出长度：" + paramRule.getMaxLength(), paramRule.getExceptionCode());
                }
                break;
            case REQUIRED:
                if (StringUtils.isBlank(value)) {
                    throw new PayException("报文参数：" + key + " 不能为空", paramRule.getExceptionCode());
                } else if (paramRule.getMaxLength() != null && value.length() > paramRule.getMaxLength()) {
                    throw new PayException("报文参数：" + key + " 超出长度：" + paramRule.getMaxLength(), paramRule.getExceptionCode());
                }
                break;
            case NUMBER:
                if (!value.matches("^\\d{1," + paramRule.getMaxLength() + "}$")) {
                    throw new PayException("报文参数：" + key + " 数字格式不正确，错误值为：" + value, paramRule.getExceptionCode());
                }
                break;
            case MONEY:
                if (!value.matches("^\\d{1,10}$|^.\\d{0,2}$|^\\d{1,10}.\\d{0,2}")) {
                    throw new PayException("报文参数：" + key + " 金额格式不正确，错误值为：" + value, paramRule.getExceptionCode());
                }
                break;
            case EMAIL:
//                if (!value.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")) {
                if (!value.matches("^(\\w+([-+.]\\w+)*[\\w-]*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)$")) {
                    throw new PayException("报文参数：" + key + " 邮箱格式不正确，错误值为：" + value, paramRule.getExceptionCode());
                }
                break;
            case IP:
                if (!value.matches(IPV4_REGEX) && !value.matches(IPV6_REGEX)) {
                    throw new PayException("报文参数：" + key + " 格式不正确，错误值为：" + value, paramRule.getExceptionCode());
                }
                break;
            case GOOD_LIST:
                if (StringUtils.isBlank(value)) {
                    throw new PayException("报文参数：" + key + " 货物列表为空！", paramRule.getExceptionCode());
                }
                break;
            default:
                break;
        }
    }


    /**
     *  抽取公共的代码
     * @param systemOrderTrack
     * @param abstractParamModel
     * @author 詹光活
     * @date 20190628
     */
    protected  void setSystemOrderTrack(SystemOrderTrack systemOrderTrack, AbstractParamModel abstractParamModel){
        if(!org.springframework.util.StringUtils.isEmpty(abstractParamModel.getMerId())){
            systemOrderTrack.setMerId(abstractParamModel.getMerId());
        }
        if(!org.springframework.util.StringUtils.isEmpty(abstractParamModel.getMerOrderId())){
            systemOrderTrack.setMerOrderId(abstractParamModel.getMerOrderId());
        }
        if(!org.springframework.util.StringUtils.isEmpty(abstractParamModel.getAmount())){
            systemOrderTrack.setAmount(abstractParamModel.getAmount());
        }
        if(!org.springframework.util.StringUtils.isEmpty(abstractParamModel.getReturnUrl())){
            systemOrderTrack.setReturnUrl(abstractParamModel.getReturnUrl() != null ? abstractParamModel.getReturnUrl():"");
        }
        if(!org.springframework.util.StringUtils.isEmpty(abstractParamModel.getNoticeUrl())){
            systemOrderTrack.setNoticeUrl(abstractParamModel.getNoticeUrl() != null ? abstractParamModel.getNoticeUrl():"");
        }
    }



    /**
     *  验证必要参数
     * @param map
     * @param tradeInfoKeys
     * @throws PayException
     */
    protected void ValidateItem(Map<String, ParamRule> map, List<String> tradeInfoKeys) throws PayException {
        List<String> requiredKeys = new ArrayList<>();
        map.forEach((s, paramRule) -> {
            if (paramRule.getType() != OTHER) {
                requiredKeys.add(s);
            }
        });
        List<String> differenceSet = requiredKeys.stream().filter(s -> !tradeInfoKeys.contains(s)).collect(Collectors.toList());
        if (differenceSet.size() != 0) {
            throw new PayException("报文缺少必要参数：" + differenceSet.toString(), 1002, differenceSet.toString());
        }
    }


    protected String getMd5Sign(Map<String,Object> param)  {
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
    protected String getMd5SignWithKey(Map<String,Object> param,String md5Key)  {
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

    /**
     *  默认默认返回参数
     * @param merId
     * @param merOrderId
     * @param smg
     * @return
     */
    protected Map<String, Object> returnMap(String merId,String merOrderId,String smg){
        Map<String, Object> map = new TreeMap<>();
        map.put("merId", merId);
        map.put("merOrderId",merOrderId);
        map.put("status", 1);
        map.put("msg", smg);
        map.put("signMsg", getMd5Sign(map));
        return map;
    }


    /**
     *  失败返回信息
     * @param systemOrderTrack
     * @param abstractParamModel
     * @return
     */
    public String errorResult(SystemOrderTrack systemOrderTrack, AbstractParamModel abstractParamModel, String message) throws PayException {
        PayTreeMap<String,Object> map= new PayTreeMap<>();
        String merId= systemOrderTrack.getMerId();
        String merOrderId=(null != abstractParamModel ? abstractParamModel.getMerOrderId() : "");
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        String signMsg = "";
        map.lput("merId",merId)
                .lput("merOrderId",merOrderId)
                .lput("status", 1)
                .lput("msg", message)
                .lput("resultCode","RXH00013");
        if (merchantInfo != null){
            signMsg = getMd5SignWithKey(map,merchantInfo.getSecretKey());
        }
        map.lput("signMsg",signMsg);
        return JSONObject.toJSONString(map);
    }


    /**
     *  失败返回信息
     * @param systemOrderTrack
     * @param abstractParamModel
     * @return
     */
    public String errorResult(SystemOrderTrack systemOrderTrack, AbstractParamModel abstractParamModel, String message, String resultCode) throws PayException {
        PayTreeMap<String,Object> map= new PayTreeMap<>();
        String merId= systemOrderTrack.getMerId();
        String merOrderId=(null != abstractParamModel ? abstractParamModel.getMerOrderId() : "");
        MerchantInfo merchantInfo =redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        String signMsg = "";
        map.lput("merId",merId)
                .lput("merOrderId",merOrderId)
                .lput("status", 1)
                .lput("msg", message)
                .lput("resultCode",resultCode);
        if (merchantInfo != null){
            signMsg = getMd5SignWithKey(map,merchantInfo.getSecretKey());
        }
        map.lput("signMsg",signMsg);
        return JSONObject.toJSONString(map);
    }

    /**
     *  异常返回参数
     * @param systemOrderTrack
     * @param type
     * @return
     */
    public String resultToString1(SystemOrderTrack systemOrderTrack,Integer type){
        String merId= "";
        String merOrderId = "";
        String status = "";
        String msg ="";
        String signMsg = "";
        String secretKey = "";
        if(null != systemOrderTrack) merOrderId=systemOrderTrack.getMerOrderId();
        if(null != systemOrderTrack){
            //获取商户信息
            try{
                MerchantInfo merchantInfo = merchantInfoService.selectByMerId(merId);
                secretKey= merchantInfo.getSecretKey();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        TreeMap map = new TreeMap();
        if (type == 2){
            map.put("orderId","");
        }
        if (systemOrderTrack.getTradeInfo() == null){
            map.put("merId",merId);
            map.put("merOrderId",merOrderId);
            map.put("status",status);
            map.put("msg",systemOrderTrack.getTradeResult());
            map.put("resultCode","RXH00013");
            map.put("signMsg",signMsg);
        }else {
            MerchantBasicInformationRegistration merchantBasicInformationRegistration = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), MerchantBasicInformationRegistration.class);
            merId = merchantBasicInformationRegistration.getMerId();
            merOrderId = merchantBasicInformationRegistration.getMerOrderId();
            if (type == 3){
                map.put("orderId","");
                map.put("amount",systemOrderTrack.getAmount());
            }
            map.put("merId",merId==null?"": merId);
            map.put("merOrderId",merOrderId == null ? "" : merOrderId);
            map.put("status",systemOrderTrack.getOrderTrackStatus());
            map.put("msg",systemOrderTrack.getTradeResult());
            map.put("resultCode","RXH00013");
            map.put("signMsg",getMd5SignWithKey(map,secretKey));

        }
        return JSONObject.toJSONString(map);
    }

    /**
     *  异常返回参数
     * @param systemOrderTrack
     * @param type
     * @return
     */
    public String resultToString1(SystemOrderTrack systemOrderTrack,Integer type,String resultCode){
        String merId= "";
        String merOrderId = "";
        String status = "";
        String msg ="";
        String signMsg = "";
        String secretKey = "";
        if(null != systemOrderTrack) merOrderId=systemOrderTrack.getMerOrderId();
        if(null != systemOrderTrack){
            //获取商户信息
            try{
                merId=systemOrderTrack.getMerId();
                MerchantInfo merchantInfo = merchantInfoService.selectByMerId(merId);
                secretKey= merchantInfo.getSecretKey();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        TreeMap map = new TreeMap();
        if (type == 2){
            map.put("orderId","");
        }
        if (systemOrderTrack.getTradeInfo() == null){
            map.put("merId",merId);
            map.put("merOrderId",merOrderId);
            map.put("status",status);
            map.put("resultCode",resultCode);
            map.put("msg",systemOrderTrack.getTradeResult());
            map.put("signMsg",signMsg);
        }else {
            MerchantBasicInformationRegistration merchantBasicInformationRegistration = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), MerchantBasicInformationRegistration.class);
            merId = merchantBasicInformationRegistration.getMerId();
            merOrderId = merchantBasicInformationRegistration.getMerOrderId();
            if (type == 3){
                map.put("orderId","");
                map.put("amount",systemOrderTrack.getAmount());
            }
            map.put("merId",merId==null?"": merId);
            map.put("merOrderId",merOrderId == null ? "" : merOrderId);
            map.put("status",systemOrderTrack.getOrderTrackStatus());
            map.put("resultCode",resultCode);
            map.put("msg",systemOrderTrack.getTradeResult());
            map.put("signMsg",getMd5SignWithKey(map,secretKey));

        }
        return JSONObject.toJSONString(map);
    }

    /**
     *  成功返回参数
     * @param bankResult
     * @param type
     * @return
     */
    public String resultToString(BankResult bankResult, MerchantInfo merchantInfo, SquareTrade squareTrade, Integer type){
        TreeMap map = new TreeMap();
        map.put("merId",merchantInfo.getMerId());
        map.put("merOrderId",squareTrade.getMerOrderId());
        map.put("status",bankResult.getStatus());
        map.put("msg",bankResult.getBankResult());
        if (bankResult.getStatus() == SystemConstant.BANK_STATUS_SUCCESS) map.put("resultCode","RXH00000");
        else  map.put("resultCode","RXH00013");
        if (type == 2){
            map.put("orderId",squareTrade.getPayOrder().getPayId());
        }else if (type == 3){
            map.put("orderId",squareTrade.getPayOrder().getPayId());
            map.put("amount",squareTrade.getPayOrder().getAmount());
        }
        map.put("signMsg",getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JSONObject.toJSONString(map);
    }
}
