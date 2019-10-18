package com.rxh.creditPay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.*;
import com.rxh.utils.UUID;
import com.rxh.yacolpay.utils.YaColIPayTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author phoenix
 * @date 20190727
 * 信用卡代偿代付接口
 */
@RestController
@RequestMapping("/creditPay")
public class CreditRepay {

    @Autowired
    private PaymentInfo paymentInfo;
    private final static Logger logger = LoggerFactory.getLogger(CreditRepay.class);
    private static SimpleDateFormat dateFormat =new SimpleDateFormat("yyyyMMdd");

    /**
     * 代付申请
     * @param squareTrade
     * @return
     */
    @RequestMapping("/payment")
    public BankResult payment(@RequestBody SquareTrade squareTrade) throws Exception{
        logger.info("====================paymentInfo.getFpxKeyPath()====================："+paymentInfo.getFpxKeyPath());
        BankResult bankResult = new BankResult();

        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        JSONObject others = JSON.parseObject(channelInfo.getOthers());
        TransOrder transOrder = squareTrade.getTransOrder();

        LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
        param.put("serviceCode","0700");
        param.put("processCode","790042");
        param.put("merchantNo",others.getString("merchantNo"));
        param.put("merchantOrderNo",transOrder.getTransId());
        param.put("sequenceNo",String.valueOf(UUID.createKey("trans_order")));
      //  param.put("sequenceNo",squareTrade.getTransOrder().getTransId());
        param.put("bindingId",squareTrade.getTradeObjectSquare().getIdentityNum());
        param.put("repayAccountNo",squareTrade.getTradeObjectSquare().getBankCardNum());
        param.put("amount",transOrder.getAmount().toString());
        param.put("mobileNo",squareTrade.getTradeObjectSquare().getBankCardPhone());
        param.put("asynResUrl1",others.get("asynResUrl1").toString());
        param.put("signMsg", CreditServiceTool.sign(param, CreditConfig.reqSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd));

        String jsons = JsonUtils.objectToJson(param);
        String requestStr = null;
        requestStr = CreditServiceTool.encrypt(jsons, CreditConfig.easyPublicKey);

        logger.info("代付请求参数param："+JSONObject.toJSONString(param));
        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), others.getString("payApplyUrl"), requestStr, "UTF-8");
       // String result = "{\"msg\":\"成功\",\"code\":\"0000\",\"data\":{\"orderNumber\":\"2019070300003\",\"bussFlowNo\":\"20190703140405520521\"}}";
        bankResult.setBankTime(new Date());
        logger.info("代付返回结果result："+content);
        if(StringUtils.isNotBlank(content)) {
            HashMap<String,String> responseMap = CreditServiceTool.parseResponse(content, CreditConfig.resSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd,CreditConfig.easyPublicKey);
            logger.info("===============代付返回结果=================responseMap："+responseMap);
            content = JSONObject.toJSONString(responseMap);
            String resultCode = responseMap.get("responseCode");
            if ("0000".equals(resultCode)) {
                String orderState = responseMap.get("orderState");
                switch (orderState) {
                    case "11":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("未付款");
                        bankResult.setBankOrderId(responseMap.get("orderNo"));
                        bankResult.setParam(content);
                        break;
                    case "12":
                         bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("一级审批通过");
                        bankResult.setBankOrderId(responseMap.get("orderNo"));
                        bankResult.setParam(content);
                        break;

                    case "13":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("二级审批通过");
                        bankResult.setBankOrderId(responseMap.get("orderNo"));
                        bankResult.setParam(content);
                        break;

                    case "14":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("付款中");
                        bankResult.setBankOrderId(responseMap.get("orderNo"));
                        bankResult.setParam(content);
                        break;
                    case "15":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                        bankResult.setBankResult("付款成功");
                        bankResult.setBankOrderId(responseMap.get("orderNo"));
                        bankResult.setParam(content);
                        break;
                    case "16":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("付款失败 :"+responseMap.get("responseRemark"));
                        bankResult.setBankOrderId(responseMap.get("orderNo"));
                        bankResult.setParam(content);
                        break;
                    case "17":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("平台处理中");
                        bankResult.setBankOrderId(responseMap.get("orderNo"));
                        bankResult.setParam(content);
                        break;
                    default:
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("付款失败 : "+responseMap.get("responseRemark") );
                        bankResult.setParam(content);
                        break;
                }
            } else if (resultCode.contains("S") || resultCode.contains("H")){
                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                bankResult.setBankResult("付款处理中，请查询订单 : "+responseMap.get("responseRemark"));
                bankResult.setBankOrderId(responseMap.get("orderNo"));
                bankResult.setParam(content);
            }else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("付款失败 : "+responseMap.get("responseRemark"));
                bankResult.setParam(content);
            }
        }
        else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("交易失败：代付返回结果为空！");
                bankResult.setParam(content);
            }
        logger.info("付款返回参数："+JSONObject.toJSONString(bankResult));
        return bankResult;
    }



    /**
     * 成功交易结果通知
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/creditRepayNotifyUrl")
    @ResponseBody
    public String payMoneyNotifyurl(@RequestBody String creditRepayNotify) throws Exception {
        logger.info("===============================代付异步回调值=====================:"+creditRepayNotify);
        HashMap<String,String> map = CreditServiceTool.parseResponse(creditRepayNotify,CreditConfig.resSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd,CreditConfig.easyPublicKey);
      //String result = URLDecoder.decode(creditRepayNotify,"UTF-8");
        String like_result = YaColIPayTools.createLinkString(map, false);
        logger.info("===============================代付异步回调结果=====================:"+like_result);

        /**
         支付结果处理
         */
        BankResult bankResult = new BankResult();
        bankResult.setOrderId(Long.valueOf(map.get("merchantOrderNo").toString()));// 商户订单号
        bankResult.setParam(JsonUtils.objectToJson(like_result));
        bankResult.setBankOrderId(map.get("orderNo"));
        Date date = DateUtils.dateFormat(dateFormat, map.get("settleDate").toString());
        bankResult.setBankTime(date == null ? new Date() : date);
        if ("0000".equals(map.get("responseCode"))) {// 支付成功
            bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
            bankResult.setBankResult("交易成功");
            bankResult.setParam(JsonUtils.objectToJson(like_result));
        } else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            logger.info("代付错误信息,错误编码为：" + map.get("responseCode"));
            bankResult.setBankCode("error.5000");
            bankResult.setBankResult("代付处理失败");
            bankResult.setParam(JsonUtils.objectToJson(like_result));
        }
        String msg = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), paymentInfo.getBankNotifyUrl(), JsonUtils.objectToJson(bankResult));
        Result paymentResult = JsonUtils.jsonToPojo(msg, Result.class);
        if (paymentResult == null) {
            return "FAIL";
        }
        if (paymentResult.getCode() == Result.SUCCESS) {
            return "success";
        } else {
            return paymentResult.getMsg();
        }
    }


    @RequestMapping("/testCreditRepay")
    @ResponseBody
    public BankResult testYacolSinglePay(@RequestBody Object object) {
        logger.info("===============================开始测试=====================");
        SquareTrade trade = new SquareTrade();
        TransOrder transOrder = new TransOrder();
        TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
        ChannelInfo chl = new ChannelInfo();
        chl.setOthers("{\n" +
                "  \"merchantNo\": \"102700000025\",\n" +
                "  \"payApplyUrl\": \"http://183.129.219.202:9021/repayment/repayment\",\n" +
                "  \"asynResUrl1\": \"http://192.168.1.67:8040/creditPay/creditRepayNotifyUrl\",\n" +
                "  \"payQueryUrl\": \"http://183.129.219.202:9021/repayment/agentPay\",\n" +
                "  \"queryUrl\": \"http://183.129.219.202:9021/repayment/query\"\n" +
                "}");


        tradeObjectSquare.setMerOrderId("M201001");
        transOrder.setTransId("201907271447001");
        tradeObjectSquare.setCardholderPhone("13599543918");
        tradeObjectSquare.setInAcctNo("6227001935600377518");
        tradeObjectSquare.setAmount(new BigDecimal("20"));
        tradeObjectSquare.setBankCardPhone("13599543918");

        trade.setChannelInfo(chl);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setTransOrder(transOrder);

        try {
            return payment(trade);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        logger.info("===============================开始测试=====================");
        CreditRepay cr = new CreditRepay();
        SquareTrade trade = new SquareTrade();
        TransOrder transOrder = new TransOrder();
        TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
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
        tradeObjectSquare.setCardholderPhone("13599543918");
        tradeObjectSquare.setInAcctNo("6227001935600377518");
        tradeObjectSquare.setAmount(new BigDecimal("20"));
        tradeObjectSquare.setBankCardPhone("13599543918");

        trade.setChannelInfo(chl);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setTransOrder(transOrder);

        try {
          cr.payment(trade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
