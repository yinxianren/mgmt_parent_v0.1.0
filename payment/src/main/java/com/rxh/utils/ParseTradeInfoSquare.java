package com.rxh.utils;

import com.rxh.exception.PayException;
import com.rxh.pojo.payment.NoCardInfo;
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
 * User: 陈俊雄
 * Date: 2018/8/21
 * Time: 15:03
 * Project: Management
 * Package: com.rxh.utils
 */
public class ParseTradeInfoSquare {
    private static final Logger logger = LoggerFactory.getLogger(ParseTradeInfoSquare.class);
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
    private final static Map<String, ParamRule> paramValidateSquare = new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("merId", new ParamRule(REQUIRED, 10, 1003));// 商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1004));// 商户订单号
            put("amount", new ParamRule(MONEY, 1005));// 总金额
            put("benefitName", new ParamRule(REQUIRED, 32, 1006));// 开户人
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1007));// 银行卡号
            put("bankName", new ParamRule(REQUIRED, 64, 1008));// 开户行
            put("bankBranchName", new ParamRule(REQUIRED, 64, 1009));// 开户行网点名称
            put("bankBranchNum", new ParamRule(OTHER, 12, 1010));// 开户行号
            put("bankCardType", new ParamRule(REQUIRED, 2, 1011));// 卡类型
            put("identityType", new ParamRule(REQUIRED, 2, 1012));// 证件类型
            put("identityNum", new ParamRule(REQUIRED, 20, 1013));// 证件号码
            put("returnUrl", new ParamRule(OTHER, 20, 1014));// 返回地址
            put("noticeUrl", new ParamRule(OTHER, 20, 1015));// 通知地址
            put("md5Info", new ParamRule(REQUIRED, 150, 1016));// md5验证码
            put("bizType", new ParamRule(REQUIRED, 10, 1017));//
            put("charset", new ParamRule(REQUIRED, 10, 1018));//
            put("signType", new ParamRule(REQUIRED, 10, 1019));//
        }
    };
    private final static Map<String, ParamRule> paramValidateNoCardInfo = new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("bizType", new ParamRule(REQUIRED, 10, 1015));//
            put("charset", new ParamRule(REQUIRED, 10, 1016));//
            put("signType", new ParamRule(REQUIRED, 10, 1017));//
            put("merId", new ParamRule(REQUIRED, 10, 1001));// 商户号
            put("merOrderId", new ParamRule(REQUIRED, 32, 1002));// 商户订单号
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1005));// 银行卡号
            put("expireYear", new ParamRule(REQUIRED, 4, 1005));//
            put("expireMonth", new ParamRule(REQUIRED, 2, 1005));//
            put("cvv", new ParamRule(REQUIRED, 4, 1005));//
            put("currency", new ParamRule(REQUIRED, 3, 1005));//
            put("amount", new ParamRule(MONEY, 1003));// 总金额
            put("identityType", new ParamRule(REQUIRED, 2, 1010));// 证件类型
            put("identityNum", new ParamRule(REQUIRED, 20, 1011));// 证件号码
            put("ext1", new ParamRule(OTHER, 20, 1013));//
            put("noticeUrl", new ParamRule(REQUIRED, 20, 1013));// 通知地址
        }
    };



    public static void checkMd5(TradeObjectSquare tradeObjectSquare, String secretKey) throws PayException {
        String str =
                tradeObjectSquare.getMerId() +
                tradeObjectSquare.getMerOrderId() +
                tradeObjectSquare.getAmount() +
                tradeObjectSquare.getBankCardNum()+
                secretKey;
        if (!StringUtils.equalsAnyIgnoreCase(tradeObjectSquare.getMd5Info(), DigestUtils.md5Hex(str).toUpperCase())) {
            throw new PayException("MD5Info校验错误！", 1300);
        }
    }


    /**
     * 四方代付接口报文解析
     * @param systemOrderTrack
     */
    public static TradeObjectSquare parse(SystemOrderTrack systemOrderTrack) throws  PayException{
        try {
            List<String> tradeInfoKeys = new ArrayList<>();
            // 报文参数
            Map<String, Object> tradeInfoMap = JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
            Set<String> keys = tradeInfoMap.keySet();
            for (String key : keys) {
                validateValue(key,tradeInfoMap.get(key).toString());
                tradeInfoKeys.add(key);
            }
            // 校验必要参数
            validateItem(tradeInfoKeys);
            TradeObjectSquare tradeObjectSquare = JsonUtils.jsonToPojo(systemOrderTrack.getTradeInfo(), TradeObjectSquare.class);
            // systemOrderTrack
            initSystemOrderTrack(systemOrderTrack, tradeObjectSquare);
            return tradeObjectSquare;
        } catch (PayException e) {
            logger.error(systemOrderTrack.getTradeInfo(), e);
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1100);
        }
    }

    /**
     * 无卡支付
     * @param systemOrderTrack
     */
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
            throw new PayException("请求报文无法解析！" + e.getMessage(), 1100);
        }
    }

    static void initSystemOrderTrack(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) {
        systemOrderTrack.setMerId(tradeObjectSquare.getMerId());
        systemOrderTrack.setMerOrderId(tradeObjectSquare.getMerOrderId());
        systemOrderTrack.setAmount(tradeObjectSquare.getAmount());
        systemOrderTrack.setReturnUrl(tradeObjectSquare.getReturnUrl() != null ? tradeObjectSquare.getReturnUrl() : "");
        systemOrderTrack.setNoticeUrl(tradeObjectSquare.getNoticeUrl() != null ? tradeObjectSquare.getNoticeUrl() : "");
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
            throw new PayException("报文缺少必要参数：" + differenceSet.toString(), 1200, differenceSet.toString());
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

    private static void validateItem(List<String> tradeInfoKeys) throws PayException {
        List<String> requiredKeys = new ArrayList<>();
        paramValidateSquare.forEach((s, paramRule) -> {
            if (paramRule.getType() != OTHER) {
                requiredKeys.add(s);
            }
        });
        List<String> differenceSet = requiredKeys.stream().filter(s -> !tradeInfoKeys.contains(s)).collect(Collectors.toList());
        if (differenceSet.size() != 0) {
            throw new PayException("报文缺少必要参数：" + differenceSet.toString(), 1200, differenceSet.toString());
        }
    }
    private static void validateValue(String key, String value) throws PayException {
        ParamRule paramRule = paramValidateSquare.get(key);
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
}