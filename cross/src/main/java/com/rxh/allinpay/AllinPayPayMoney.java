package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.TransOrderInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.enums.StatusEnum;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.AlinTradeObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.MerchantRegisterCollect;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * 付款 /payMoney
 */
@Controller
@RequestMapping("/allinPay")
public class AllinPayPayMoney {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(AllinPayPayMoney.class);

    private final PaymentInfo paymentInfo;

    @Autowired
    public AllinPayPayMoney(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    /**
     * 成功交易结果通知
     * @param alinTradeObject
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/payMoneyNotifyurl")
    @ResponseBody
    public String payMoneyNotifyurl(@RequestBody AlinTradeObject alinTradeObject) throws UnsupportedEncodingException {
        logger.info(String.format("通联异步回调：%s",JSONObject.toJSONString(alinTradeObject)));
        Map<String,Object> map = new TreeMap<>();
        map.put("acct",alinTradeObject.getAcct());
        map.put("appid",alinTradeObject.getAppid());
        map.put("cusid",alinTradeObject.getCusid());
        map.put("cusorderid",alinTradeObject.getCusorderid());
        map.put("fee",alinTradeObject.getFee());
        map.put("initamt",alinTradeObject.getInitamt());
        map.put("outtrxid",alinTradeObject.getOuttrxid());
        map.put("paytime",alinTradeObject.getPaytime());
        map.put("trxamt",alinTradeObject.getTrxamt());
        map.put("trxcode",alinTradeObject.getTrxcode());
        map.put("trxdate",alinTradeObject.getTrxdate());
        map.put("trxid",alinTradeObject.getTrxid());
        map.put("trxreserved",alinTradeObject.getTrxreserved());
        map.put("trxstatus",alinTradeObject.getTrxstatus());
        String md5Sign=  AlinPayUtils.getMd5Sign(map);
        if (StringUtils.equals(md5Sign,alinTradeObject.getSign())){
            /**
             支付结果处理
             */
            BankResult bankResult = new BankResult();
            bankResult.setOrderId((alinTradeObject.getCusorderid()));// 商户订单号
            bankResult.setBankOrderId(alinTradeObject.getOuttrxid());// 第三方app交易号(商户交易流水号)
            bankResult.setParam(JsonUtils.objectToJson(alinTradeObject));
            Date date = DateUtils.dateFormat(dateFormat, alinTradeObject.getPaytime());
            bankResult.setBankTime(date == null ? new Date() : date);
            if ("0000".equals(alinTradeObject.getTrxstatus())) {// 支付成功
                bankResult.setStatus(StatusEnum._0.getStatus());
                bankResult.setBankResult("付款成功");
                bankResult.setParam(JsonUtils.objectToJson(alinTradeObject));
            } else {
                bankResult.setStatus(StatusEnum._1.getStatus());
                try {
                    bankResult.setBankCode(BankResultInfoCode.CommunicateCodeTwo.valueOf("A" + alinTradeObject.getTrxstatus()).getStatusMsg());
                } catch (Exception e) {
                    logger.info("AllinPay未定义的错误信息,错误编码为：" + alinTradeObject.getTrxstatus());
                    bankResult.setBankCode("error.5000");
                }
                bankResult.setBankResult("付款失败：" + alinTradeObject.getTrxstatus());
                bankResult.setParam(JsonUtils.objectToJson(alinTradeObject));
            }
            String msg = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), paymentInfo.getBankNotifyUrl(), JsonUtils.objectToJson(bankResult));
            Result paymentResult = JsonUtils.jsonToPojo(msg, Result.class);
            if (paymentResult == null) {
                return "FAIL";
            }
            if (paymentResult.getCode() == Result.SUCCESS) {
                return "";
            } else {
                return paymentResult.getMsg();
            }
        }else{
            logger.info("非法请求：签名信息验证失败");
            return "签名信息验证失败";
        }
    }


    @RequestMapping("/payMoney")
    @ResponseBody
    public BankResult payMoney(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException, InterruptedException {
        BankResult bankResult = new BankResult();
//        bankResult.setTrade(trade);
        Map<String, Object> bondParam = getBondParam(trade);

        JSONObject others = (JSONObject) JSON.parse(trade.getChannelInfoTable().getChannelParam());
        logger.info("allinPay付款交易参数："+JSONObject.toJSONString(bondParam));
        // String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://ipay.allinpay.com/apiweb/acct/pay", bondParam); //生产环境
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), others.getString("oneTransUrl"), bondParam);// 测试环境
//        String content = "{\"acctno\":\"622700****7047\",\"actualamount\":\"200\",\"amount\":\"1000\",\"appid\":\"6666678\",\"errmsg\":\"提交成功,等待返回结果\",\"fee\":\"800\",\"fintime\":\"20190628100755\",\"orderid\":\"1906281007470130001\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"76403287ABD89F8637426FA2BC1BA4F9\",\"trxid\":\"19060000001378\",\"trxstatus\":\"0000\"}";
        logger.info("allinPay付款银行返回："+content);
//        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
        JSONObject result = (JSONObject) JSON.parse(content);
        String resultCode = result.getString("retcode");

        if("SUCCESS".equals(resultCode)){

            String trxstatus = result.getString("trxstatus");
            bankResult.setResult(result.getString("errmsg"));
            bankResult.setBankTime(new Date());
            switch (trxstatus){
                case "0000":
                    bankResult.setStatus(StatusEnum._0.getStatus());
                    bankResult.setBankResult("付款成功");
                    bankResult.setBankOrderId(result.getString("trxid"));
                    bankResult.setParam(content);
                    break;
                case "2000":
                    bankResult.setStatus(StatusEnum._3.getStatus());
                    bankResult.setBankResult("交易已受理");
                    bankResult.setParam(content);
                    break;
                case "0003":
                    bankResult.setStatus(StatusEnum._3.getStatus());
                    bankResult.setBankResult("交易异常,请查询交易");
                    bankResult.setBankCode("305");
                    bankResult.setParam(content);
                    break;
                case "3999":
                    bankResult.setStatus(StatusEnum._1.getStatus());
                    bankResult.setBankResult("其他错误");
                    bankResult.setParam(content);
                    break;
                default:
                    bankResult = new BankResult(StatusEnum._1.getStatus(), "error.5001");
                    bankResult.setBankResult("付款失败");
                    bankResult.setParam(content);
                    break;
            }
        }else {
            bankResult = new BankResult(StatusEnum._1.getStatus(), "error.5001");
            bankResult.setBankResult("付款失败:"+result.getString("errmsg"));
            bankResult.setParam(content);
        }
        logger.info("allinPay付款返回参数："+JSONObject.toJSONString(bankResult));
        return bankResult;
    }


    private Map<String, Object> getBondParam(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
        ChannelInfoTable channelInfo = trade.getChannelInfoTable();
        JSONObject param = JSON.parseObject(channelInfo.getChannelParam());
        RegisterCollectTable merchantRegisterCollect= trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        TreeMap<String, Object> postData = new TreeMap<>();
        TransOrderInfoTable transOrder = trade.getTransOrderInfoTable();
        String amount = transOrder.getAmount().toString();
        String agreeid = trade.getMerchantCardTable().getCrossRespResult();
        String appid = param.getString("appid");// 公共必填
        String key = param.getString("key");// 公共必填
        String cusid = registerInfo.getString("cusid"); //商户号
        String fee =transOrder.getBackFee().toString();
//        String isall = param.getString("isall");
        String notifyurl = param.getString("notifyurl");
        String orderid = transOrder.getPlatformOrderId();
        String orgid = param.getString("orgid");// 公共必填
//        String trxreserve = param.getString("trxreserve");
        postData.put("cusid",cusid);
        postData.put("orderid",orderid);

        String amountStr=new BigDecimal(amount).multiply(new BigDecimal(100)).toString();
        postData.put("amount",amountStr.contains(".") ? amountStr.substring(0,amountStr.indexOf(".")): amountStr);
//        if(StringUtils.isNotEmpty(isall)) {
//        	postData.put("isall",isall);
//        }
        if(StringUtils.isNotEmpty(fee)) {
            fee=new BigDecimal(fee).multiply(new BigDecimal(100)).setScale(0).toString();
            postData.put("fee",fee);
        }
        postData.put("agreeid",agreeid);
//        if(StringUtils.isNotEmpty(trxreserve)) {
//            postData.put("trxreserve",trxreserve);
//        }
        postData.put("notifyurl",notifyurl);
        postData.put("orgid",orgid);
        postData.put("appid",appid);
        postData.put("randomstr",AlinPayUtils.getRandomSecretkey());
        postData.put("key", key);
        postData.put("sign",AlinPayUtils.getMd5Sign(postData));
        postData.remove("key");
        return postData;
    }
}
