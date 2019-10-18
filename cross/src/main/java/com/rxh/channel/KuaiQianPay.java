package com.rxh.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.PayOrder;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/KuaiQianPay")
public class KuaiQianPay {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(KuaiQianPay.class);
//    @Autowired
    KuaiQianRSAUtils kuaiQianRSAUtils;

    @RequestMapping("/trade")
    @ResponseBody
    public BankResult trade(@RequestBody SquareTrade trade) throws ParseException, PayException {
        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject param = JSON.parseObject(channelInfo.getOthers());
        String tradeParam = getTradeParam(trade);
        System.out.println(tradeParam);

        BankResult bankResult = new BankResult();
        bankResult.setOrderId(Long.parseLong(trade.getPayOrder().getPayId()));
        Random random = new Random();
        bankResult.setStatus(random.nextInt(10)%2>0?Short.valueOf("0"):Short.valueOf("1"));
        return bankResult;
//        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "https://103.62.90.20:443/sdk/isv/index", tradeParam);
//        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), param.getString("payUrl"), tradeParam);
//        if (StringUtils.isNotBlank(content)) {
//            return checkResult(content,trade,tradeParam);
//        } else {
//            return new BankResult(SystemConstant.BANK_STATUS_TIME_OUT, "error.5001");
//        }
    }

    private BankResult checkResult(String content, SquareTrade trade, String tradeParam) throws PayException {
        logger.info("KuaiQianPay返回参数：" + content);
        BankResult bankResult = new BankResult();
        ChannelInfo channelInfo = trade.getChannelInfo();
        TransOrder transOrder = trade.getTransOrder();
        JSONObject param = JSON.parseObject(channelInfo.getOthers());
        String merchantId = param.getString("merchantId");
        String terminalId = param.getString("terminalId");
        bankResult.setParam(content);
        bankResult.setBankTime(new Date());



        if (StringUtils.isNotBlank(content)) {
            JSONObject result = (JSONObject) JSON.parse(content);
            bankResult.setOrderId(Long.parseLong(transOrder.getTransId()));
            String responseCode = result.getString("responseCode");// 结果状态码
            String responseMsg = result.getString("responseMsg");// 结果状态码
            String signMsg = result.getString("sign");// 结果状态码
            //签名验证
            String sign="data="+result.getString("data")+"&responseCode="+responseCode+"&responseMsg="+responseMsg;
            String md5Info=DigestUtils.md5Hex(sign);
            String base64Info= Base64.getEncoder().encodeToString(md5Info.getBytes());
            String rasInfo = kuaiQianRSAUtils.encrypt(base64Info);
            if(!rasInfo.equals(signMsg)){
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("签名验证失败:" + result.get("responseMsg"));
                bankResult.setBankCode("error.5000");
                return bankResult;
            }
            // 判断数据是正常返回还是只返回了错误编码和错误信息
            switch(responseCode){
                case "00":
                    bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                    bankResult.setBankResult("交易成功");
                    bankResult.setBankCode("pay.1047");
                    break;
                case "68":
                    bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                    bankResult.setBankResult("交易处理中");
                    bankResult.setBankCode("pay.1047");
                    break;
                case "C0":
                    bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                    bankResult.setBankResult("交易处理中");
                    bankResult.setBankCode("pay.1047");
                    break;
                default:
                    bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                    bankResult.setBankResult("付款失败:" + result.get("responseMsg"));
                    bankResult.setBankCode("error.5000");
                    break;
            }
            if(responseCode.equals("68")||responseCode.equals("C0")){
                polling(trade,tradeParam);
            }



        }else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_TIME_OUT);
            bankResult.setBankResult("付款失败：支付返回结果为空！");
            bankResult.setBankCode("error.5000");
        }

        return bankResult;
    }


    public void polling(SquareTrade trade, String tradeParam) throws PayException {
        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject param = JSON.parseObject(channelInfo.getOthers());
//        TransOrder transOrder = trade.getTransOrder();
        PayOrder payOrder = trade.getPayOrder();
        String orderId = payOrder.getPayId();
        BigDecimal amt = payOrder.getAmount();
        String merchantId = param.getString("merchantId");
        String terminalId = param.getString("terminalId");
        LinkedHashMap<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("orderId",orderId);
        dataMap.put("amt",amt.toString());
        dataMap.put("merchantId",merchantId);
        dataMap.put("terminalId",terminalId);
        String dataMapjson = JsonUtils.objectToJson(dataMap);

        //签名验证
        String sign="bizType=ISV001"+"&data="+dataMapjson+"&version=1.0";
        String md5Info=DigestUtils.md5Hex(sign);
        String base64Info= Base64.getEncoder().encodeToString(md5Info.getBytes());
        String rasInfo = kuaiQianRSAUtils.encrypt(base64Info);
        HashMap<String, Object> postData = new LinkedHashMap<>();
        postData.put("version","1.0");
        postData.put("bizType","ISV001");
        postData.put("charset","UTF-8");
        postData.put("sign",rasInfo);
        postData.put("signType","RSA");
        postData.put("merchantId",merchantId);
        postData.put("terminalId",terminalId);
        postData.put("data",dataMapjson);
        String postJson = JsonUtils.objectToJson(postData);



        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), param.getString("checkUrl"), postJson);
        BankResult bankResult = new BankResult();
        bankResult.setOrderId(Long.parseLong(trade.getTransOrder().getTransId()));
        bankResult.setParam(content);
        JSONObject result = (JSONObject) JSON.parse(content);
        String responseCode = result.getString("responseCode");// 结果状态码
        if(responseCode.equals("68")||responseCode.equals("C0")){
            polling(trade,tradeParam);
        }else if (responseCode.equals("00")){
            bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
            bankResult.setBankResult("交易成功");
            bankResult.setBankCode("pay.1047");
        }else {
            //撤销交易
            Revoke(trade,tradeParam);
            return;
        }

        String msg= HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), "payment地址",JsonUtils.objectToJson(bankResult));
        logger.info(msg);

    }

    public void Revoke(SquareTrade trade, String tradeParam) throws PayException {
        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject param = JSON.parseObject(channelInfo.getOthers());
//        TransOrder transOrder = trade.getTransOrder();
        PayOrder payOrder = trade.getPayOrder();

        String orderId = payOrder.getPayId();
        BigDecimal amt = payOrder.getAmount();
        String merchantId = param.getString("merchantId");
        String terminalId = param.getString("terminalId");
        LinkedHashMap<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("orderId",orderId);
        dataMap.put("amt",amt.toString());
        dataMap.put("originalOrderId",orderId);
        dataMap.put("originalPayType",merchantId);
        dataMap.put("terminalId",terminalId);
        String dataMapjson = JsonUtils.objectToJson(dataMap);
        //签名验证
        String sign="bizType=ISV001"+"&data="+dataMapjson+"&version=1.0";
        String md5Info=DigestUtils.md5Hex(sign);
        String base64Info= Base64.getEncoder().encodeToString(md5Info.getBytes());
        String rasInfo = kuaiQianRSAUtils.encrypt(base64Info);
        HashMap<String, Object> postData = new LinkedHashMap<>();
        postData.put("version","1.0");
        postData.put("bizType","ISV001");
        postData.put("charset","UTF-8");
        postData.put("sign",rasInfo);
        postData.put("signType","RSA");
        postData.put("merchantId",merchantId);
        postData.put("terminalId",terminalId);
        postData.put("data",dataMapjson);
        String postJson = JsonUtils.objectToJson(postData);
        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), param.getString("refundUrl"), postJson);

        BankResult bankResult = new BankResult();
        bankResult.setOrderId(Long.parseLong(trade.getTransOrder().getTransId()));
        bankResult.setParam(content);
        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
        bankResult.setBankResult("交易失败 撤销交易");
        bankResult.setBankCode("error.5000");

        //发往payment
        String msg= HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), "payment地址",JsonUtils.objectToJson(bankResult));
        logger.info(msg);


    }





    public String getTradeParam(SquareTrade trade) throws  PayException {
        ChannelInfo channelInfo = trade.getChannelInfo();
//        TransOrder transOrder = trade.getTransOrder();
        PayOrder payOrder = trade.getPayOrder();
        JSONObject param = JSON.parseObject(channelInfo.getOthers());
        String merchantId = param.getString("merchantId");
        String terminalId = param.getString("terminalId");
        HashMap<String, Object> data = new HashMap<>();
        data.put("orderId",payOrder.getPayId());
        data.put("cur",payOrder.getCurrency());
        data.put("amt",payOrder.getAmount());
        data.put("merchName",trade.getMerchName());
        data.put("merchantId",merchantId);
        data.put("terminalId",terminalId);
        String dataJson = JsonUtils.objectToJson(data);
        String  sign="bizType=ISV001&charset=UTF-8&+"+dataJson+"&merchantId="+merchantId+"&signType=RSA&terminalId="+terminalId+"&version=1.0";
        String md5Info=DigestUtils.md5Hex(sign);
        String base64Info= Base64.getEncoder().encodeToString(md5Info.getBytes());
        String rasInfo = kuaiQianRSAUtils.encrypt(base64Info);
        HashMap<String, Object> postData = new LinkedHashMap<>();
        postData.put("version","1.0");
        postData.put("bizType","ISV001");
        postData.put("charset","UTF-8");
        postData.put("sign",rasInfo);
        postData.put("signType","RSA");
        postData.put("merchantId",merchantId);
        postData.put("terminalId",terminalId);
        postData.put("data",dataJson);
        String postJson = JsonUtils.objectToJson(postData);
        return postJson;
    }




}
