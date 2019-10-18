package com.rxh.channel;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.MerchantCard;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/CoalitionPay")
public class CoalitionPay {


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
        String bankResult = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), param.getString("payUrl"), tradeParam);
       System.out.println(tradeParam);
            return   checkResult(bankResult,trade);

    }


    @RequestMapping("/bondCard")
    @ResponseBody
    public BankResult bondCard(@RequestBody SquareTrade trade)throws PayException {
        BankResult bankResult = new BankResult();
        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject param = JSON.parseObject(channelInfo.getOthers());
        String bondParam = getBondParam(trade);
        String bondResult = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "https://103.62.90.20:443/sdk/isv/index", bondParam);
        JSONObject result = (JSONObject) JSON.parse(bondResult);
        String resultCode = result.getString("resultCode");
        if("0000".equals(resultCode)){
            bankResult.setStatus(Short.valueOf("0"));
            bankResult.setBankResult("绑卡成功");
            bankResult.setParam(bondResult);
        }else {
             bankResult = new BankResult(SystemConstant.BANK_STATUS_TIME_OUT, "error.5001");
             bankResult.setBankResult("绑卡失败");
             bankResult.setParam(bondResult);
        }

        return bankResult;
    }

    private BankResult checkResult(String content, SquareTrade trade) {
        logger.info("getOrderPay返回参数：" + content);
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
            String responseCode = result.getString("returnCode");// 结果状态码
            if (responseCode.equals("0000")){
                String respType = result.getString("respType");
                switch (respType){
                    case "S":
                        bankResult.setStatus(Short.valueOf("0"));
                        bankResult.setBankResult("支付成功,代付成功");
                        break;
                    case "E":
                        bankResult.setStatus(Short.valueOf("1"));
                        bankResult.setBankResult("支付失败");
                        break;
                    case "R":
                        bankResult.setStatus(Short.valueOf("3"));
                        bankResult.setBankResult("不确定(处理中)");
                        break;
                    case "F":
                        bankResult.setStatus(Short.valueOf("0"));
                        bankResult.setBankResult("支付成功,代付失败");
                        break;
                        default:

                            break;

                }


            }else {
               bankResult.setStatus(Short.valueOf("0"));
            }


        }else {

        }


        return null;
    }



    private String getBondParam(SquareTrade trade) throws PayException {
        TradeObjectSquare tradeObjectSquare = trade.getTradeObjectSquare();
        ChannelInfo channelInfo = trade.getChannelInfo();
        MerchantCard merchantCard = trade.getMerchantCard();
        JSONObject param = JSON.parseObject(channelInfo.getOthers());
        Integer bankCardType = Integer.valueOf(merchantCard.getCardType());
        HashMap<String, Object> postData = new LinkedHashMap<>();
        String str="";
        if (bankCardType==2){
            str ="accountType=" +bankCardType+
                 "&accountName=" +merchantCard.getName()+
                 "&bankAccount=" +merchantCard.getCardNum()+
                 "&bankCode=" +merchantCard.getBankCode()+
                 "&bankCvv=" +merchantCard.getCvv()+
                 "&bankYxq=" +merchantCard.getExpireDate()+
                 "&certNbr=" +merchantCard.getIdentityNum()+
                 "&orderNum=" +trade.getTransOrder().getTransId()+
                 "&memberCode=" +param.getString("memberCode")+
                 "&tel="+merchantCard.getPhone();
            String base64Info= Base64.getEncoder().encodeToString(str.getBytes());
            String rasInfo = kuaiQianRSAUtils.encrypt(base64Info);

            postData.put("memberCode",param.getString("memberCode"));
            postData.put("orderNum",trade.getTransOrder().getTransId());
            postData.put("accountType",bankCardType);
            postData.put("bankAccount",merchantCard.getCardNum());
            postData.put("accountName",merchantCard.getName());
            postData.put("certNbr",merchantCard.getIdentityNum());
            postData.put("tel",merchantCard.getPhone());
            postData.put("bankCode",merchantCard.getBankCode());
            postData.put("bankCvv",merchantCard.getCvv());
            postData.put("bankYxq",merchantCard.getExpireDate());
            postData.put("signStr",rasInfo);
            String postJson = JsonUtils.objectToJson(postData);
            return postJson;
        }
         str="accountType=" +bankCardType+
                "&accountName=" +merchantCard.getName()+
                "&bankAccount=" +merchantCard.getCardNum()+
                "&bankCode=" +merchantCard.getBankCode()+
                "&certNbr=" +merchantCard.getIdentityNum()+
                "&orderNum=" +trade.getTransOrder().getTransId()+
                "&memberCode=" +param.getString("memberCode")+
                "&tel="+merchantCard.getPhone();
        String base64Info= Base64.getEncoder().encodeToString(str.getBytes());
        String rasInfo = kuaiQianRSAUtils.encrypt(base64Info);
        postData.put("memberCode",param.getString("memberCode"));
        postData.put("orderNum",trade.getTransOrder().getTransId());
        postData.put("accountType",bankCardType);
        postData.put("bankAccount",merchantCard.getCardNum());
        postData.put("accountName",merchantCard.getName());
        postData.put("certNbr",merchantCard.getIdentityNum());
        postData.put("tel",merchantCard.getPhone());
        postData.put("bankCode",merchantCard.getBankCode());
        postData.put("signStr",rasInfo);
        String postJson = JsonUtils.objectToJson(postData);
        return postJson;
    }

    private String getTradeParam(SquareTrade trade) throws PayException {
        TradeObjectSquare tradeObjectSquare = trade.getTradeObjectSquare();
        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject param = JSON.parseObject(channelInfo.getOthers());
        Integer bankCardType = tradeObjectSquare.getBankCardType();
        HashMap<String, Object> postData = new LinkedHashMap<>();
        String str="";
        if (bankCardType==2){
            str =   "accountType=" +bankCardType+
                    "&accountName=" +tradeObjectSquare.getCardholderName()+
                    "&bankAccount=" +tradeObjectSquare.getBankCardNum()+
                    "&bankCvv=" +trade.getBankCvv()+
                    "&bankYxq=" +trade.getBankYxq()+
                    "&callbackUrl=" +param.getString("callbackUrl")+
                    "&certNbr=" +tradeObjectSquare.getIdentityNum()+
                    "&memberCode=" +param.getString("memberCode")+
                    "&orderNum=" +trade.getTransOrder().getTransId()+
                    "&payMoney=" +trade.getTransOrder().getAmount()+
                    "&settleFee="+tradeObjectSquare.getFee();
            String base64Info= Base64.getEncoder().encodeToString(str.getBytes());
            String rasInfo = kuaiQianRSAUtils.encrypt(base64Info);

            postData.put("accountType",bankCardType);
            postData.put("accountName",tradeObjectSquare.getCardholderName());
            postData.put("bankAccount",tradeObjectSquare.getBankCardNum());
            postData.put("bankCvv",trade.getBankCvv());
            postData.put("bankYxq",trade.getBankYxq());
            postData.put("callbackUrl",param.getString("memberCode"));
            postData.put("certNbr",tradeObjectSquare.getIdentityNum());
            postData.put("memberCode",param.getString("memberCode"));
            postData.put("orderNum",trade.getTransOrder().getTransId());
            postData.put("payMoney",tradeObjectSquare.getAmount());
            postData.put("signStr",rasInfo);
            String postJson = JsonUtils.objectToJson(postData);
            return postJson;
        }
             str="accountType=" +bankCardType+
                "&accountName=" +tradeObjectSquare.getCardholderName()+
                "&bankAccount=" +tradeObjectSquare.getBankCardNum()+
                "&callbackUrl=" +param.getString("callbackUrl")+
                "&certNbr=" +tradeObjectSquare.getIdentityNum()+
                "&memberCode=" +param.getString("memberCode")+
                "&orderNum=" +trade.getTransOrder().getTransId()+
                "&payMoney=" +trade.getTransOrder().getAmount()+
                "&settleFee=" +tradeObjectSquare.getFee();
        String base64Info= Base64.getEncoder().encodeToString(str.getBytes());
        String rasInfo = kuaiQianRSAUtils.encrypt(base64Info);
        postData.put("memberCode",param.getString("memberCode"));
        postData.put("orderNum",trade.getTransOrder().getTransId());
        postData.put("payMoney",trade.getTransOrder().getAmount());
        postData.put("accountType",bankCardType);
        postData.put("bankAccount",tradeObjectSquare.getBankCardNum());
        postData.put("accountName",tradeObjectSquare.getCardholderName());
        postData.put("certNbr",tradeObjectSquare.getIdentityNum());
        postData.put("settleFee",tradeObjectSquare.getFee());
        postData.put("callbackUrl",param.getString("callbackUrl"));
        postData.put("signStr",rasInfo);
        String postJson = JsonUtils.objectToJson(postData);
        return postJson;

    }
}
