package com.rxh.service;

import com.rxh.activeMQ.TransOrderMQ;
import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.cache.ehcache.MerchantSquareRateCache;
import com.rxh.cache.ehcache.MerchantSquareSettingCache;
import com.rxh.cache.ehcache.RiskQuotaDataCache;
import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.pojo.AbstratorParamModel;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.service.haiyi.HaiYiAgentWalletComponent;
import com.rxh.service.haiyi.HaiYiChannelWalletComponent;
import com.rxh.service.haiyi.HaiYiMerchantWalletComponent;
import com.rxh.service.haiyi.HaiYiPayService;
import com.rxh.service.kuaijie.AllinPayService;
import com.rxh.service.kuaijie.InwardSquareService;
import com.rxh.service.kuaijie.MerchantSquareNotifyService;
import com.rxh.service.kuaijie.RecordSquareService;
import com.rxh.service.square.*;
import com.rxh.service.sys.SysConstantService;
import com.rxh.service.trading.PayOrderService;
import com.rxh.service.trading.TransOrderService;
import com.rxh.service.wallet.AllinPayWalletService;
import com.rxh.service.wallet.HaiYiPayWalletService;
import com.rxh.service.wallet.KuaiJiePayService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.strategy.OptimalChannelStrategy;
import com.rxh.utils.MerchantsLockUtils;
import com.rxh.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 描述：支付 service 的部分
 * @author  panda
 * @date 20190710
 */
public class AbstractPayService implements PayAssert, PayUtil{



    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected RecordSquareService recordSquareService;
    @Autowired
    protected OrganizationService organizationService;
    @Autowired
    protected ChannelInfoService channelInfoService;
    @Autowired
    protected MerchantSquareSettingCache merchantSquareSettingCache;
    @Autowired
    protected MerchantSquareRateCache merchantSquareRateCache;
    @Autowired
    protected PaymentRecordSquareService paymentRecordSquareService;
    @Autowired
    protected TransOrderMQ transOrderMQ;
    @Autowired
    protected TransOrderService transOrderService;
    @Autowired
    protected HaiYiPayWalletService haiYiPayWalletService;
    @Autowired
    protected MerchantInfoService merchantInfoService;
    @Autowired
    protected HaiYiMerchantWalletComponent haiYiMerchantWalletComponent;
    @Autowired
    protected HaiYiAgentWalletComponent haiYiAgentWalletComponent;
    @Autowired
    protected HaiYiChannelWalletComponent haiYiChannelWalletComponent;
    @Resource
    protected RiskQuotaDataService riskQuotaDataService;
    @Autowired
    protected OptimalChannelStrategy optimalChannelStrategy;
    @Autowired
    protected KuaiJiePayService kuaiJiePayService;
    @Autowired
    protected SysConstantService sysConstantService;
    @Resource
    protected AllinPayService allinPayService;
    @Autowired
    protected RedisCacheCommonCompoment redisCacheCommonCompoment;
    @Autowired
    protected  InwardSquareService inwardSquareService;
    @Autowired
    protected  RiskQuotaDataCache riskQuotaDataCache;
    @Autowired
    protected AllinPayWalletService allinPayWalletService;
    @Autowired
    protected MerchantSquareNotifyService merchantSquareNotifyService;
    @Autowired
    protected HaiYiPayService haiYiPayService;
    @Autowired
    protected MerchantsLockUtils merchantsLockUtils;
    @Autowired
    protected PayOrderService payOrderService;



    protected static final Lock lock=new ReentrantLock(true);


    protected static final String IPV4_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    protected static final String IPV6_REGEX = "^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$";
    protected static final int OTHER = 0; // 可空
    protected static final int REQUIRED = 1; // 必填项
    protected static final int NUMBER = 2; // 数字输入
    protected static final int MONEY = 3;  // 金钱格式
    protected static final int EMAIL = 4; // 邮箱格式
    protected static final int IP = 5;// IP格式
    protected static final int GOOD_LIST = 6; // 货物列表

    //创建固定线程池
    public static final  ExecutorService pool =Executors.newFixedThreadPool(25);

    /**
     *  验证参数格式
     * @param map
     * @param key
     * @param value
     * @throws PayException
     */
    public void validateValue(Map<String, ParamRule> map, String key, String value) throws PayException {
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
                if (!StringUtils.isBlank(value) && !value.matches("^\\d{1,10}$|^.\\d{0,2}$|^\\d{1,10}.\\d{0,2}")) {
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
     * @param abstratorParamModel
     * @author 詹光活
     * @date 20190628
     */
    public  void setSystemOrderTrack(SystemOrderTrack systemOrderTrack, AbstratorParamModel abstratorParamModel){
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



    /**
     *  验证必要参数
     * @param map
     * @param tradeInfoKeys
     * @throws PayException
     */
    public void ValidateItem(Map<String, ParamRule> map, List<String> tradeInfoKeys) throws PayException {
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

    /**
     *  默认默认返回参数
     * @param merId
     * @param merOrderId
     * @param smg
     * @return
     */
    public Map<String, Object> returnMap(String merId,String merOrderId,String smg){
        Map<String, Object> map = new TreeMap<>();
        map.put("merId", merId);
        map.put("merOrderId",merOrderId);
        map.put("status", 1);
        map.put("msg", smg);
        map.put("signMsg", getMd5Sign(map));
        return map;
    }
}
