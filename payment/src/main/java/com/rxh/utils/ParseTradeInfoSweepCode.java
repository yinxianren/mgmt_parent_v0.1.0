package com.rxh.utils;

import com.rxh.exception.PayException;
import com.rxh.pojo.AbstratorParamModel;
import com.rxh.pojo.merchant.MerchantAddCusInfo;
import com.rxh.pojo.merchant.MerchantPayOrderShortMessage;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.SystemOrderTrack;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Project: Management
 * Package: com.rxh.utils
 */
public class ParseTradeInfoSweepCode {

    public final  static Short SweepCode = 0;
    public final  static Short NoCard = 1;

    private static final Logger logger = LoggerFactory.getLogger(ParseTradeInfoSweepCode.class);
    private static final String IPV4_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final String IPV6_REGEX = "^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$";
    // 可空
    private static final int OTHER = 0;
    // 必填项
    private static final int REQUIRED = 1;
    // 数字输入
    private static final int NUMBER = 2;
    // 金钱格式
    private static final int MONEY = 3;
    // 邮箱格式
    private static final int EMAIL = 4;
    // IP格式
    private static final int IP = 5;
    // 货物列表
    private static final int GOOD_LIST = 6;
    // 报文参数的验证规则
    private final static Map<String, ParamRule> sweepCodepayParamValidateSquare = new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));// 支付方式
            put("charset", new ParamRule(REQUIRED, 10, 1004));// 接口编号
            put("merId", new ParamRule(REQUIRED, 10, 1005));// 商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));// 商户订单号
            put("currency", new ParamRule(REQUIRED, 3, 1007));// 商户订单号
            put("amount", new ParamRule(MONEY,10, 1008));// 交易金额
            put("authCode", new ParamRule(REQUIRED, 32, 1009));// 二维码字符串
            put("productName", new ParamRule(REQUIRED, 64, 1010));// 商品名称
            put("noticeUrl", new ParamRule(OTHER, 128, 1011));// 回调地址
            put("signMsg", new ParamRule(REQUIRED, 256, 1012));// 签名字符串
        }
    };
    private final static Map<String, ParamRule> getOrderPayParamValidateSquare = new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));// 支付方式
            put("charset", new ParamRule(REQUIRED, 10, 1004));// 接口编号
            put("signType", new ParamRule(REQUIRED, 10, 1004));// 接口编号
            put("merId", new ParamRule(REQUIRED, 10, 1005));// 商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));// 商户订单号
            put("currency", new ParamRule(REQUIRED, 3, 1007));// 商户订单号
            put("amount", new ParamRule(MONEY,10, 1008));// 交易金额
            put("fee", new ParamRule(MONEY,10, 1008));// 交易金额
            put("benefitName", new ParamRule(MONEY,10, 1008));// 交易金额
            put("bankCardNum", new ParamRule(MONEY,10, 1008));// 交易金额
            put("bankBranchName", new ParamRule(MONEY,10, 1008));// 交易金额
            put("bankBranchNum", new ParamRule(MONEY,10, 1008));// 交易金额
            put("cvv", new ParamRule(MONEY,10, 1008));// 交易金额
            put("identityType", new ParamRule(MONEY,10, 1008));// 交易金额
            put("identityNum", new ParamRule(MONEY,10, 1008));// 交易金额
            put("noticeUrl", new ParamRule(MONEY,10, 1008));// 交易金额
            put("md5Info", new ParamRule(REQUIRED, 32, 1009));// 二维码字符串
        }
    };
    // 报文参数的验证规则
    private final static Map<String, ParamRule> paramValidateNoCardInfo = new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));// 商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));// 商户订单号
            put("currency", new ParamRule(REQUIRED, 3, 1007));//
            put("amount", new ParamRule(MONEY, 1008));// 总金额
            put("noticeUrl", new ParamRule(REQUIRED, 20, 1011));// 通知地址

            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));// 银行卡号
            put("expireYear", new ParamRule(REQUIRED, 4, 1013));//
            put("expireMonth", new ParamRule(REQUIRED, 2, 1013));//
            put("cvv", new ParamRule(REQUIRED, 4, 1013));//
            put("identityType", new ParamRule(REQUIRED, 2, 1014));// 证件类型
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));// 证件号码
//            put("ext1", new ParamRule(OTHER, 20, 1013));//
        }
    };
    private final static Map<String, ParamRule> alliancePayBondCardValidate= new HashMap<String, ParamRule>() {
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
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
        }
    };

    private final static Map<String, ParamRule> allianceGetOrderPayConfirmValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("smsCode", new ParamRule(REQUIRED, 10, 1030));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(OTHER, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("payFee", new ParamRule(MONEY, 1008));// 手续费率
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//
        }
    };
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

    private final static Map<String, ParamRule> allianceAddCus=new HashMap<String, ParamRule>() {
        {
            put("cizType", new ParamRule(REQUIRED, 10, 1003));//用于区分不同的业务接口（进件接口ID）
            put("charset", new ParamRule(REQUIRED, 10, 1004));//请求使用的编码格式，固定UTF-8
            put("signType", new ParamRule(REQUIRED, 10, 1004));//固定为MD5
            put("merId", new ParamRule(REQUIRED, 10, 1005));//商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));// 商户订单号
            put("userName", new ParamRule(REQUIRED, 64, 1016));// 商户名称
            put("userShortName", new ParamRule(REQUIRED, 3, 1015));//  商户简称
            put("identityType", new ParamRule(REQUIRED, 2, 1014));//  证件类型
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));//证件号码
            put("bankCardType", new ParamRule(REQUIRED, 2, 1017));// 卡号类型
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//银行卡号
            put("bankCardPhone", new ParamRule(REQUIRED, 11, 1013));// 手机号
            put("province", new ParamRule(REQUIRED, 20, 1018));// 省份
            put("city", new ParamRule(REQUIRED, 20, 1020));// 城市
            put("payFee", new ParamRule(MONEY, 1007));// 扣款手续费
            put("backFee", new ParamRule(MONEY, 1007));// 代付手续费
            put("address", new ParamRule(REQUIRED, 64, 1021));// 详细地址
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1022));// 终端客id
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//终端客户名称
            put("returnUrl", new ParamRule(REQUIRED, 128, 1013));//返回地址
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//通知地址
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//签名字符串
        }
    };


    private final static Map<String, ParamRule> merchantShortMessage=new HashMap<String, ParamRule>() {
        {
            put("merId", new ParamRule(REQUIRED, 10, 1005));//商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));// 商户订单号
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1022));// 终端客id
        }
    };

    public static void checkMd5(TradeObjectSquare tradeObjectSquare, String secretKey) throws PayException {
        String str =
                tradeObjectSquare.getBizType() +
                        tradeObjectSquare.getMerId() +
                        tradeObjectSquare.getMerOrderId() +
                        tradeObjectSquare.getCurrency() +
                        tradeObjectSquare.getAmount() +
                        secretKey;
        if (!StringUtils.equalsAnyIgnoreCase(tradeObjectSquare.getMd5Info(), DigestUtils.md5Hex(str).toUpperCase())) {
            throw new PayException("MD5Info校验错误！", 1300);
        }
    }


    /**
     * 扫码接口报文解析
     * @param systemOrderTrack
     */
    public static TradeObjectSquare sweepCodepay(SystemOrderTrack systemOrderTrack,Short type)  throws PayException {
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(sweepCodepayParamValidateSquare,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(sweepCodepayParamValidateSquare,tradeInfoKeys);
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

    public static TradeObjectSquare parseNoCardInfo(SystemOrderTrack systemOrderTrack) throws  PayException{
        try {
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValueNoCard(key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            validateItemNoCard(tradeInfoKeys);
            TradeObjectSquare tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            // systemOrderTrack
            initSystemOrderTrack(systemOrderTrack, tradeObjectSquare);
            return tradeObjectSquare;
        } catch (PayException e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1001);
        }
    }



    private static void ValidateItem(Map<String, ParamRule> map,List<String> tradeInfoKeys) throws PayException {
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
    private static void validateValue(Map<String, ParamRule> map,String key, String value) throws PayException {
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
                } else if (value.length() > paramRule.getMaxLength()) {
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


    private static void validateValueNoCard(String key, String value) throws PayException {
        ParamRule paramRule = paramValidateNoCardInfo.get(key);
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
                } else if (value.length() > paramRule.getMaxLength()) {
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

    private final static Map<String, ParamRule> allianceGetOrderPayValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1003));//
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("merOrderId", new ParamRule(REQUIRED, 32, 1006));//
            put("currency", new ParamRule(REQUIRED, 10, 1006));//
            put("amount", new ParamRule(MONEY, 1007));//
            put("identityType", new ParamRule(REQUIRED, 2, 1014));//
            put("bankCardType", new ParamRule(REQUIRED, 2, 1014));//
            put("identityNum", new ParamRule(REQUIRED, 20, 1014));//
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCardPhone", new ParamRule(REQUIRED, 19, 1013));//
            put("bankCode", new ParamRule(REQUIRED, 32, 1013));//
            put("validDate", new ParamRule(REQUIRED, 6, 1013));//
            put("securityCode", new ParamRule(REQUIRED, 4, 1013));//
            put("payFee", new ParamRule(MONEY, 1007));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("returnUrl", new ParamRule(OTHER, 128, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 128, 1013));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//

        }
    };
    public static TradeObjectSquare allianceGetOrderPay(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(allianceGetOrderPayValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(allianceGetOrderPayValidate,tradeInfoKeys);
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
    private static void validateItemNoCard(List<String> tradeInfoKeys) throws PayException {
        List<String> requiredKeys = new ArrayList<>();
        paramValidateNoCardInfo.forEach((s, paramRule) -> {
            if (paramRule.getType() != OTHER) {
                requiredKeys.add(s);
            }
        });
        List<String> differenceSet = requiredKeys.stream().filter(s -> !tradeInfoKeys.contains(s)).collect(Collectors.toList());
        if (differenceSet.size() != 0) {
            throw new PayException("报文缺少必要参数：" + differenceSet.toString(), 1002, differenceSet.toString());
        }
    }

    static void initSystemOrderTrack(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) {
        systemOrderTrack.setMerId(tradeObjectSquare.getMerId());
        systemOrderTrack.setMerOrderId(tradeObjectSquare.getMerOrderId());
        systemOrderTrack.setAmount(tradeObjectSquare.getAmount());
        systemOrderTrack.setReturnUrl(tradeObjectSquare.getReturnUrl() != null ? tradeObjectSquare.getReturnUrl() : "");
        systemOrderTrack.setNoticeUrl(tradeObjectSquare.getNoticeUrl() != null ? tradeObjectSquare.getNoticeUrl() : "");
    }

    public static TradeObjectSquare getOrderPay(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(getOrderPayParamValidateSquare,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(getOrderPayParamValidateSquare,tradeInfoKeys);
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

    public static TradeObjectSquare alliancePayBondCard(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare =null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(alliancePayBondCardValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(alliancePayBondCardValidate,tradeInfoKeys);
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



    public static TradeObjectSquare alliancePayConfirmBond(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(allianceConfirmBondValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(allianceConfirmBondValidate,tradeInfoKeys);
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

    public static TradeObjectSquare alliancePayConfirmPay(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare = null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(allianceGetOrderPayConfirmValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(allianceGetOrderPayConfirmValidate,tradeInfoKeys);
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
     *  通联进件参数校验
     * @param systemOrderTrack
     * @return
     * @throws PayException
     */
    public static MerchantAddCusInfo allianceVerifyAddCus(SystemOrderTrack systemOrderTrack) throws PayException{
        MerchantAddCusInfo merchantAddCusInfo =null;
        try {
            merchantAddCusInfo = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), MerchantAddCusInfo.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(allianceAddCus,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            ValidateItem(allianceAddCus,tradeInfoKeys);  // 校验必要参数
            return merchantAddCusInfo;
        } catch (PayException  e) {
            logger.error("通联进件请求时，参数校验抛出异常:"+systemOrderTrack.getTradeInfo(), e);
            throw new PayException("通联进件请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != merchantAddCusInfo){
                setSystemOrderTrack(systemOrderTrack,merchantAddCusInfo);
            }
        }
    }

    /**
     *  商户收单短信参数校验
     * @return
     */
    public static MerchantPayOrderShortMessage merchantPayOrderShortMessageVerify(SystemOrderTrack systemOrderTrack) throws  Exception{
        MerchantPayOrderShortMessage merchantPayOrderShortMessage =null;
        try{
            merchantPayOrderShortMessage = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), MerchantPayOrderShortMessage.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(merchantShortMessage,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            ValidateItem(merchantShortMessage,tradeInfoKeys);  // 校验必要参数
            systemOrderTrack.setMerId(merchantPayOrderShortMessage.getMerId());
            systemOrderTrack.setMerOrderId(merchantPayOrderShortMessage.getMerOrderId());
            return merchantPayOrderShortMessage;
        }catch (Exception e){
            logger.error("商户收单短信参数校验请求时，参数校验抛出异常:"+systemOrderTrack.getTradeInfo(), e);
            throw new PayException("商户收单短信参数校验请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != merchantPayOrderShortMessage){
                setSystemOrderTrack(systemOrderTrack,merchantPayOrderShortMessage);
            }
        }
    }


    public static TradeObjectSquare alliancePayBondSmsCode(SystemOrderTrack systemOrderTrack) throws PayException {
        TradeObjectSquare tradeObjectSquare =null;
        try {
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(allianceGetBondSmsValidate,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            ValidateItem(allianceGetBondSmsValidate,tradeInfoKeys);
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

    public static TradeObjectSquare alliancePayConfirmSms(SystemOrderTrack systemOrderTrack) throws PayException{
        TradeObjectSquare tradeObjectSquare =null;
        try{
            tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            List<String> tradeInfoKeys = new ArrayList<>();
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(merchantShortMessage,key, tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            ValidateItem(merchantShortMessage,tradeInfoKeys);  // 校验必要参数
            return tradeObjectSquare;
        }catch (Exception e){
            logger.error("商户收单短信参数校验请求时，参数校验抛出异常:"+systemOrderTrack.getTradeInfo(), e);
            throw new PayException("商户收单短信参数校验请求报文无法解析！" + e.getMessage(), 1001);
        }finally {
            if(null != tradeObjectSquare){
                setSystemOrderTrack(systemOrderTrack,tradeObjectSquare);
            }
        }
    }

    /**
     *  抽取公共的代码
     * @param systemOrderTrack
     * @param abstratorParamModel
     * @author 詹光活
     * @date 20190628
     */
    public static void setSystemOrderTrack(SystemOrderTrack systemOrderTrack,AbstratorParamModel abstratorParamModel){
        if(!org.springframework.util.StringUtils.isEmpty(abstratorParamModel.getMerId())){
            systemOrderTrack.setMerId(abstratorParamModel.getMerId());
        }
        if(!org.springframework.util.StringUtils.isEmpty(abstratorParamModel.getMerOrderId())){
            systemOrderTrack.setMerOrderId(abstratorParamModel.getMerOrderId());
        }
        if(!org.springframework.util.StringUtils.isEmpty(abstratorParamModel.getAmount())){
            systemOrderTrack.setAmount(abstratorParamModel.getAmount());
        }
        if(!org.springframework.util.StringUtils.isEmpty(abstratorParamModel.getReturnUrl())){
            systemOrderTrack.setReturnUrl(abstratorParamModel.getReturnUrl() != null ? abstratorParamModel.getReturnUrl():"");
        }
        if(!org.springframework.util.StringUtils.isEmpty(abstratorParamModel.getNoticeUrl())){
            systemOrderTrack.setNoticeUrl(abstratorParamModel.getNoticeUrl() != null ? abstratorParamModel.getNoticeUrl():"");
        }
    }

}
