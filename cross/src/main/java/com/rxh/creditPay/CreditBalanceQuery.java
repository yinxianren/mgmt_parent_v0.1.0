package com.rxh.creditPay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.MerchantCard;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * @author phoenix
 * @date 20190727
 * 信用卡代偿余额查询接口
 */

@RestController
@RequestMapping("/creditBalanceQuery")
public class CreditBalanceQuery {

        @Autowired
        private PaymentInfo paymentInfo;
        private final static Logger logger = LoggerFactory.getLogger(CreditBalanceQuery.class);
;
        /**
         * 余额
         * @param squareTrade
         * @return
         */
        @RequestMapping("/balanceQuery")
        public BankResult payment(@RequestBody SquareTrade squareTrade) throws Exception{
            BankResult bankResult = new BankResult();
            LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
            ChannelInfo channelInfo = squareTrade.getChannelInfo();
            JSONObject others = JSON.parseObject(channelInfo.getOthers());
            param.put("serviceCode","0500");
            param.put("processCode","490002");
            param.put("merchantNo",others.getString("merchantNo"));

            param.put("sequenceNo",String.valueOf(UUID.createKey("trans_order")));

            if(null!=squareTrade.getMerchantCard()){
                if(null!=squareTrade.getMerchantCard().getIdentityNum()) {
                    param.put("bindingId", squareTrade.getMerchantCard().getIdentityNum());
                }
            }

            param.put("signMsg", CreditServiceTool.sign(param, CreditConfig.reqSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd));

            String jsons = JsonUtils.objectToJson(param);
            String requestStr = null;

            requestStr = CreditServiceTool.encrypt(jsons, CreditConfig.easyPublicKey);

            logger.info("查询请求参数param："+JSONObject.toJSONString(param));
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), others.getString("queryUrl"), requestStr, "UTF-8");
            // String result = "{\"msg\":\"成功\",\"code\":\"0000\",\"data\":{\"orderNumber\":\"2019070300003\",\"bussFlowNo\":\"20190703140405520521\"}}";
            logger.info("查询返回结果result："+content);

            if(StringUtils.isNotBlank(content)) {
                HashMap<String,String> responseMap = CreditServiceTool.parseResponse(content, CreditConfig.resSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd,CreditConfig.easyPublicKey);
                logger.info("===============交易查询返回结果=================responseMap："+responseMap);

                String resultCode = responseMap.get("responseCode");
                if ("0000".equals(resultCode)) {
                    bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                    String transInfo = responseMap.get("transInfo");
                    JSONObject transInfos = (JSONObject) JSON.parse(transInfo);
                    BigDecimal balanceAmount = new BigDecimal(transInfos.getString("balance"));
                    bankResult.setRealAmount(balanceAmount);
                    bankResult.setBankTime(new Date());
                    bankResult.setParam(transInfo);
                    bankResult.setBankResult("查询成功,当前余额为: "+balanceAmount);
                } else {
                    bankResult = new BankResult(SystemConstant.BANK_STATUS_FAIL, "error.5001");
                    bankResult.setBankResult("查询失败");
                    bankResult.setParam(content);
                }
            }

             else {
                    bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                    bankResult.setBankResult("交易失败：交易查询返回结果为空！");
                    bankResult.setParam(content);
                }
            logger.info("返回参数："+JSONObject.toJSONString(bankResult));
            return bankResult;
        }


    @RequestMapping("/testBalanceQuery")
    @ResponseBody
    public BankResult testPayQuery(@RequestBody Object object) {
        logger.info("===============================开始测试=====================");
        SquareTrade trade = new SquareTrade();
        TransOrder transOrder = new TransOrder();
        TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
        MerchantCard merchantCard = new MerchantCard();
        ChannelInfo chl = new ChannelInfo();
        chl.setOthers("{\n" +
                "  \"merchantNo\": \"102700000025\",\n" +
                "  \"payApplyUrl\": \"http://183.129.219.202:9021/repayment/repayment\",\n" +
                "  \"asynResUrl1\": \"http://boc.cmtbuy.com/cross/creditPay/creditRepayNotifyUrl\",\n" +
                "  \"payQueryUrl\": \"http://183.129.219.202:9021/repayment/agentPay\",\n" +
                "  \"queryUrl\": \"http://183.129.219.202:9021/repayment/query\"\n" +
                "}");

        tradeObjectSquare.setMerOrderId("M201001");
        transOrder.setTransId("201907271447001");

      merchantCard.setIdentityNum("500234199211038433");


        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setTransOrder(transOrder);
        trade.setChannelInfo(chl);
       trade.setMerchantCard(merchantCard);

        try {
            return payment(trade);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
