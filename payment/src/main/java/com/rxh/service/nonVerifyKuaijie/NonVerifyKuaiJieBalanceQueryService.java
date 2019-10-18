package com.rxh.service.nonVerifyKuaijie;

import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.ParamRule;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractCommonService;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class NonVerifyKuaiJieBalanceQueryService  extends AbstractCommonService  {


    public Map<String, ParamRule> noVerifyKuaijieBalanceQueryValidate= new HashMap<String, ParamRule>() {
        {
            // 交易数据
            put("charset", new ParamRule(REQUIRED, 10, 1004));//
            put("signType", new ParamRule(REQUIRED, 10, 1004));//
            put("merId", new ParamRule(REQUIRED, 10, 1005));//
            put("terminalMerId", new ParamRule(REQUIRED, 32, 1011));//
            put("terminalMerName", new ParamRule(REQUIRED, 32, 1013));//
            put("bankCardNum", new ParamRule(REQUIRED, 19, 1013));//
            put("signMsg", new ParamRule(REQUIRED, 256, 1013));//

        }
    };

    public SquareTrade getQueryParam(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws Exception {
        logger.info("===========无验证快捷余额查询===========");
        String merId=tradeObjectSquare.getMerId();
        String merOrderId=tradeObjectSquare.getMerOrderId();
        String terminalMerId=tradeObjectSquare.getTerminalMerId();
        String bankCardNum = tradeObjectSquare.getBankCardNum();
        //获取商户信息
        MerchantInfo merchantInfo = redisCacheCommonCompoment.merchantInfoCache.getOne(merId);
        isNull(merchantInfo,format("【无验证快捷余额查询】商户号：%s,订单号：%s,未找到商户相关信息",merId,merOrderId));
        CheckMd5Utils.checkMd5(systemOrderTrack.getTradeInfo(),merchantInfo.getSecretKey());
        //获取商户设置
        MerchantSetting merchantSetting =redisCacheCommonCompoment.merchantSettingCache.getOne(tradeObjectSquare.getMerId());
        isNull(merchantSetting,format("【无验证快捷余额查询】商户号：%s,订单号：%s,未找到商户设置相关信息",merId,merOrderId));
        //获取商户绑卡信
//        MerchantCard merchantCard = recordPaymentSquareService.getMerchantCardByMerIdAndMerOrderIdAndTermIdAndCardNum(merId, merOrderId,terminalMerId,bankCardNum, SystemConstant.BANK_STATUS_SUCCESS);
        MerchantCard merchantCard = redisCacheCommonCompoment.merchantCardCache.getMore(merId,terminalMerId)
                .stream()
                .filter(t-> t.getTerminalMerId().equals(terminalMerId) && t.getCardNum().equals(bankCardNum))
                .distinct()
                .findAny()
                .orElse(null);
        isNull(merchantCard,format("【无验证快捷余额查询】商户号：%s,订单号：%s,未找到获取商户绑卡相关信息",merId,merOrderId));
        ChannelInfo channelInfo =recordPaymentSquareService.getChannelInfosByMerchantSettingAndType(merchantSetting,tradeObjectSquare,SystemConstant.CHANNEL_XINSHENG);
        isNull(channelInfo,format("【无验证快捷余额查询】商户号：%s,订单号：%s,未获取到商户通道信息",merId,merOrderId));
        return  new SquareTrade()
                .lsetMerchantInfo(merchantInfo)
                .lsetMerchantCard(merchantCard)
                .lsetChannelInfo(channelInfo)
                .lsetTradeObjectSquare(tradeObjectSquare)
                ;
    }

    public String getErrorReturnJson(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare, SquareTrade trade, BankResult bankResult, String message) {
        PayTreeMap<String,Object> map= new PayTreeMap<>();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = merchantInfo.getMerId();
        String merOrderId = tradeObjectSquare.getMerOrderId();
        map.lput("merId", merId)
                .lput("merOrderId", merOrderId)
                .lput("balance",new BigDecimal(0))
                .lput("status", 1)
                .lput("msg",message)
                .lput("signMsg", CheckMd5Utils.getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JSONObject.toJSONString(map);
    }

    public String getReturnJson(BankResult result, SquareTrade trade) {
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        TradeObjectSquare tradeObjectSquare = trade.getTradeObjectSquare();
        String merId = merchantInfo.getMerId();
        String merOrderId = tradeObjectSquare.getMerOrderId();
        String status = result.getStatus().toString();
        String msg = result.getBankResult();
        BigDecimal balance = result.getRealAmount();
        if (balance==null){
            balance=new BigDecimal(0);
        }

        PayTreeMap<String, Object> map = new PayTreeMap<>();
        map.lput("merId", merId)
                .lput("merOrderId", merOrderId)
                .lput("balance", balance.toString())
                .lput("status", status)
                .lput("msg", msg)
                .lput("signMsg", CheckMd5Utils.getMd5SignWithKey(map,merchantInfo.getSecretKey()));
        return JsonUtils.objectToJson(map);
    }


    public BankResult nonVerifyKuaiJieToQueryBalance(SquareTrade trade) {
        try{
            logger.info("【无验证快捷余额查询】商户号[{}]:  发送查询请求请求：{}",trade.getMerchantInfo().getMerId(),JsonUtils.objectToJson(trade));
//            result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), "http://192.168.1.67:8040/cross/creditBalanceQuery/balanceQuery", JsonUtils.objectToJsonNonNull(trade));
            String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), JSONObject.parseObject(trade.getChannelInfo().getOthers()).getString("queryBalanceUrl"), JsonUtils.objectToJsonNonNull(trade));
            logger.info("【无验证快捷余额查询】商户号[{}]: 请求结果：{}",trade.getMerchantInfo().getMerId(),result);
            if (StringUtils.isBlank(result))  throw  new NullPointerException(String.format("商户号[%s]:响应结果为空",trade.getMerchantInfo().getMerId()));
            BankResult bankResult = JsonUtils.jsonToPojo(result, BankResult.class);
            Assert.notNull(bankResult,format("【无验证快捷余额查询】商户号[%s]:转换BankResult异常",trade.getMerchantInfo().getMerId()));
            return bankResult;
        }catch ( Exception exception){
            logger.info("【无验证快捷余额查询】 商户号[{}]:请求过程中抛出异常！",trade.getMerchantInfo().getMerId());
            throw exception;
        }
    }
}
