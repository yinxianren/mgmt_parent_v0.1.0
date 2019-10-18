package com.rxh.service.kuaijie;

import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.AbstractPayService;
import com.rxh.service.haiyi.HaiYiPayService;
import com.rxh.square.pojo.*;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.MerchantsLockUtils;
import com.rxh.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: hul
 * Date: 2019/5/25
 * Time: 10:30
 */
@Service
public class InwardSquareService extends AbstractPayService {




    /**
     * 数据库插入
     * @param systemOrderTrack 风控前置对象
     * @param tradeObjectSquare 报文对象
     */
    public void payment(SystemOrderTrack systemOrderTrack, TradeObjectSquare tradeObjectSquare) throws PayException {
        recordSquareService.payment(systemOrderTrack, tradeObjectSquare);
    }



    public void afterPay(SquareTrade trade, BankResult result, TransAudit transAudit, String transferer) throws Exception {
        TransOrder transOrder = trade.getTransOrder();
        ChannelInfo channelInfo = trade.getChannelInfo();
        transOrder.setTradeResult(result.getParam().length()>=500?result.getParam().substring(0,499):result.getParam());
//        MerchantQuotaRisk merchantQuotaRisk = recordSquareService.getMerchantQuotaRiskByMerId(transOrder.getMerId());
        MerchantQuotaRisk merchantQuotaRisk =  redisCacheCommonCompoment.merchantQuotaRiskCache.getOne(transOrder.getMerId());
        if(!Objects.equals(transOrder.getOrderStatus(), Integer.valueOf(result.getStatus()))&&result.getStatus()==0){
            transOrder.setOrderStatus(Integer.valueOf(result.getStatus()));
            transOrder.setBankTime(result.getBankTime());
            transOrder.setOrgOrderId(result.getBankOrderId());
            //更新钱包和订单
            try {
                merchantsLockUtils.getLock(transOrder.getMerId()).lock();
                allinPayService.updateWallet(transOrder,trade.getPayOrder());
            }finally {
                merchantsLockUtils.getLock(transOrder.getMerId()).unlock();
            }
            recordSquareService.updateRiskQuotaData(transOrder,channelInfo,merchantQuotaRisk);
        }
        //2.
        transAudit.setTransferer(transferer);
        transAudit.setTransferTime(new Date());
        transAudit.setStatus(0);// 已经审核
        recordSquareService.updateTransAudit(transAudit);

    }

    public String getReturnJson(SquareTrade trade, BankResult result) {
        TransOrder transOrder = trade.getTransOrder();
        MerchantInfo merchantInfo = trade.getMerchantInfo();
        String merId = merchantInfo.getMerId();
        String merOrderId = trade.getMerOrderId();
        String amount = transOrder.getAmount().toString();
        String transId = transOrder.getTransId();
        Short status = result.getStatus();
        String resultjson = haiYiPayService.resultToString(result,merchantInfo,trade);
        return resultjson;
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
    private BankResult toPay(SquareTrade trade)  throws PayException{
        logger.info(JsonUtils.objectToJsonNonNull(trade));
        // 发送请求至接口
//        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getChannelInfo().getPayUrl()+"quickPay/trade", JsonUtils.objectToJsonNonNull(trade));
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), "http://localhost:8077/quickPay/trade", JsonUtils.objectToJson(trade));
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

    public String goPay(String tranMap) throws  Exception {
        Map<String,Object> map = JsonUtils.jsonToMap(tranMap);
        String transId =  map.get("transId").toString();
        String transferer =  map.get("transferer").toString();
        TransAudit transAudit =  recordSquareService.getTransAuditByTransId(transId);
        SquareTrade trade = JsonUtils.jsonToPojo(transAudit.getTrade(), SquareTrade.class);
        // BankResult bankResult = toPay(trade);
        BankResult bankResult = new BankResult();
        bankResult.setStatus(Short.valueOf("0"));
        afterPay(trade,bankResult,transAudit,transferer);
        String resultJson = getReturnJson(trade,bankResult);
        // HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), trade.getTradeObjectSquare().getNoticeUrl(), resultJson);
        return  resultJson;
    }
}