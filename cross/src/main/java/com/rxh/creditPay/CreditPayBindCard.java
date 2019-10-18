package com.rxh.creditPay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 绑卡（包含异步返回）
 */
@RestController
@RequestMapping("/creditPayBindCard")
public class CreditPayBindCard {

    @Autowired
    private PaymentInfo paymentInfo;

    private final static Logger logger = LoggerFactory.getLogger(CreditPayBindCard.class);

    public BankResult trade(@RequestBody SquareTrade trade)  throws Exception {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        JSONObject param = JSON.parseObject(extraChannelInfo.getData());
        LinkedHashMap<String,String> bondParam = getTradeParam(trade);
        String json = JsonUtils.objectToJson(bondParam);
        logger.info("绑卡请求参数"+json);
        String requestStr = CreditServiceTool.encrypt(json,CreditConfig.easyPublicKey);
        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), param.getString("creditPayBindCardUrl"), requestStr, "UTF-8");
        // String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://183.129.219.202:9021/repayment/repaymentApp", requestStr, "UTF-8");// 测试环境
        // logger.info("绑卡银行返回"+content);
        return  checkResult(content);
    }

    @RequestMapping("/testCreditRepay")
    @ResponseBody
    public String testYacolSinglePay(@RequestBody Object object) throws Exception {
        LinkedHashMap<String,String> bean = new LinkedHashMap<>();
        bean.put("serviceCode", "0800");
        bean.put("processCode", "890004");
        bean.put("merchantNo", "201907300200");
//        bean.put("accountNo",  "");
        bean.put("mobileNo", "");
        bean.put("signMsg", CreditServiceTool.sign(bean,CreditConfig.reqSignFields, paymentInfo.getFpxKeyPath(), CreditConfig.merchantPrivateKeyPwd));
        String json = JsonUtils.objectToJson(bean);
        String requestStr = CreditServiceTool.encrypt(json,CreditConfig.easyPublicKey);
        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "https://www.esicash.com/easy/repayment/repaymentApp", requestStr, "UTF-8");
        logger.info("===============================开始测试=====================");
        return content;
    }




    private BankResult checkResult(String content) throws Exception{
        BankResult bankResult = new BankResult();
        if(StringUtils.isNotBlank(content)) {
            HashMap<String,String> responseMap = CreditServiceTool.parseResponse(content, CreditConfig.resSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd,CreditConfig.easyPublicKey);
            logger.info("============绑卡银行返回====responseMap:"+responseMap);
            String responseCode = responseMap.get("responseCode");
            String responseRemark = responseMap.get("responseRemark");
            String sequenceNo = responseMap.get("sequenceNo");
            switch(responseCode){
                case "0000":
                    bankResult.setBankOrderId(sequenceNo);
                    bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                    bankResult.setBankTime(new Date());
                    bankResult.setBankResult("绑卡提交成功");
                    bankResult.setParam(responseMap.toString());
                    bankResult.setBankData(responseMap.get("transInfo"));
                    break;
                default:
                    bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                    bankResult.setBankResult("绑卡失败:" + responseRemark);
                    bankResult.setParam(responseMap.toString());
                    break;
            }
        }else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("绑卡失败：绑卡返回结果为空！");
            bankResult.setParam(content);
        }
        logger.info("绑卡请求返回payment："+ JsonUtils.objectToJsonNonNull(bankResult));
        return bankResult;
    }

    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/getTradeParam")
    private LinkedHashMap<String,String> getTradeParam(SquareTrade trade) throws Exception {
        LinkedHashMap<String,String> bean = new LinkedHashMap<String,String>();
        // MerchantRegisterCollect merchantRegisterCollect = trade.getMerchantRegisterCollect();
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        JSONObject others = JSON.parseObject(extraChannelInfo.getData());
        // PayOrder payOrder = trade.getPayOrder();
        MerchantCard merchantCard = trade.getMerchantCard();

        bean.put("serviceCode", "0800");
        bean.put("processCode", "890001");
        bean.put("merchantNo", others.get("merchantNo").toString());
        bean.put("sequenceNo",  merchantCard.getId());
        bean.put("bindingId",  merchantCard.getIdentityNum());
        bean.put("accountNo",  merchantCard.getCardNum());
        bean.put("mobileNo", merchantCard.getPhone());
        bean.put("idcardNo", merchantCard.getIdentityNum());
        bean.put("name", merchantCard.getName());
        bean.put("asynResUrl", others.get("asynResUrl").toString());
        bean.put("synResUrl", trade.getTradeObjectSquare().getReturnUrl());
        bean.put("area", others.get("area").toString());
        bean.put("signMsg", CreditServiceTool.sign(bean,CreditConfig.reqSignFields, paymentInfo.getFpxKeyPath(), CreditConfig.merchantPrivateKeyPwd));



        // String mobileNo = "13599543918";
        // String bindingId = mobileNo;
        // // String accountNo = "4391880500000006";
        // String accountNo = "6227001935600377518";
        // String name = "钱川";
        // String idcardNo = "500234199211038433";
        // String area = "福建省-厦门市";
        // String sequenceNo = "2019072920560003";
        // bean.put("serviceCode", "0800");
        // bean.put("processCode", "890001");
        // bean.put("merchantNo", "102700000025");
        // bean.put("sequenceNo",  sequenceNo);
        // //bean.put("bindingId",  bindingId);
        // bean.put("accountNo",  accountNo);
        // bean.put("mobileNo", mobileNo);
        // bean.put("idcardNo", idcardNo);
        // bean.put("name", name);
        // bean.put("asynResUrl", "http://192.168.1.67:8040/cross/creditPayBindCard/creditPayBindCardNotifyurl");
        // bean.put("synResUrl", "http://192.168.1.67:8040/cross/creditPayBindCard/creditPayBindCardNotifyurl");
        // bean.put("area", area);
        // bean.put("signMsg", CreditServiceTool.sign(bean,CreditConfig.reqSignFields, paymentInfo.getFpxKeyPath(), CreditConfig.merchantPrivateKeyPwd));

        return bean;
    }
    /**
     * 绑卡异步返回结果
     */
    @RequestMapping("/creditPayBindCardNotifyurl")
    @ResponseBody
    public String creditPayBindCardNotifyurl(HttpServletRequest request) throws  Exception {
        request.setCharacterEncoding("UTF-8");
        String resStr = CreditHttper.read(request);
        resStr = URLDecoder.decode(resStr,"UTF-8").replaceAll(" ", "+");
        if(StringUtils.isNotBlank(resStr)) {
            HashMap<String,String> responseMap = CreditServiceTool.parseResponse(resStr, CreditConfig.resSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd,CreditConfig.easyPublicKey);
            logger.info("绑卡(异步回调)返回参数"+responseMap);
            String responseCode = responseMap.get("responseCode");
            String responseRemark = responseMap.get("responseRemark");
            String sequenceNo = responseMap.get("sequenceNo");
            BankResult bankResult = new BankResult();
            bankResult.setBankOrderId((sequenceNo));// 商户订单号
            bankResult.setParam(responseMap.toString());
            bankResult.setBankTime(new Date());
            // bankResult.setOrderAmount(new BigDecimal(responseMap.get("amount")).setScale(2,BigDecimal.ROUND_HALF_UP));// 订单金额
            if ("0000".equals(responseCode)) {// 消费成功
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("绑卡成功！");
            } else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                try {
                    bankResult.setBankCode(BankResultInfoCode.CommunicateCodeTwo.valueOf("A" + responseCode).getStatusMsg());
                } catch (Exception e) {
                    logger.info("CreditPay未定义的错误信息,错误编码为：" + responseCode);
                    bankResult.setBankCode("error.5000");
                }
                bankResult.setBankResult("绑卡失败：" + responseRemark);
            }
            String msg = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), paymentInfo.getBankCardNotifyUrl(), JsonUtils.objectToJson(bankResult));
            Result paymentResult = JsonUtils.jsonToPojo(msg, Result.class);
            if (paymentResult == null) {
                return "FAIL";
            }
            if (paymentResult.getCode() == Result.SUCCESS) {
                return "";
            } else {
                return JSONObject.toJSONString(bankResult);
            }
        } else {
            logger.info("非法请求：绑卡失败");
            return "绑卡失败";
        }
    }


}
