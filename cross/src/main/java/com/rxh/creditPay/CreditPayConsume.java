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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 消费
 */
@Controller
@RequestMapping("/creditPayConsume")
public class CreditPayConsume {

    @Autowired
    private PaymentInfo paymentInfo;

    private final static Logger logger = LoggerFactory.getLogger(CreditPayConsume.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    public BankResult trade(@RequestBody SquareTrade trade)  throws Exception {
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        JSONObject param = JSON.parseObject(extraChannelInfo.getData());
        LinkedHashMap<String,String> bondParam = getTradeParam(trade);
        String json = JsonUtils.objectToJson(bondParam);
        logger.info("消费请求参数"+json);
        String requestStr = CreditServiceTool.encrypt(json, CreditConfig.easyPublicKey);
        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), param.getString("payConsumeUrl"), requestStr, "UTF-8");
        // String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://183.129.219.202:9021/repayment/repayment", requestStr, "UTF-8");// 测试环境
        // logger.info("消费银行返回"+content);
        return checkResult(content);
    }
    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws Exception
     */
    private LinkedHashMap<String,String> getTradeParam(SquareTrade trade) throws Exception {
        LinkedHashMap<String,String> bean = new LinkedHashMap<String,String>();
//        MerchantRegisterCollect merchantRegisterCollect = trade.getMerchantRegisterCollect();
        // JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getBackData());
        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject others = JSON.parseObject(channelInfo.getOthers());
        PayOrder payOrder = trade.getPayOrder();
        // String sequenceNo =  String.valueOf(UUID.createKey("pay_order"));
        String sequenceNo =  payOrder.getPayId();
        MerchantCard  merchantCard = trade.getMerchantCard();
        bean.put("serviceCode", "0700");
        bean.put("processCode", "790041");
        bean.put("merchantNo", others.get("cusid").toString()); //商户号
        bean.put("merchantOrderNo",  sequenceNo);
        bean.put("sequenceNo", sequenceNo);
        bean.put("amount", payOrder.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        bean.put("payRate", trade.getTradeObjectSquare().getPayFee().divide(new BigDecimal(100)).setScale(6,BigDecimal.ROUND_HALF_UP).toString());
        bean.put("payAccountNo", merchantCard.getCardNum());
        bean.put("bindingId", merchantCard.getIdentityNum());
        bean.put("asynResUrl1", others.get("asynResUrl1").toString());
//        bean.put("asynResUrl2", others.get("asynResUrl1").toString());
        bean.put("signMsg", CreditServiceTool.sign(bean,CreditConfig.reqSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd));

        // String merchantOrderNo = "2019072920560005";//
        // String bindingId = "13599543918";
        // String payAccountNo = "6227001935600377518";
        // bean.put("serviceCode", "0700");
        // bean.put("processCode", "790041");
        // bean.put("merchantNo", "102700000025");
        // bean.put("merchantOrderNo",  merchantOrderNo);
        // bean.put("sequenceNo",  merchantOrderNo);
        // bean.put("amount", "1");
        // bean.put("payRate", "0.005000");
        // bean.put("payAccountNo", payAccountNo);
        // bean.put("bindingId", bindingId);
        // bean.put("asynResUrl1", "http://192.168.1.67:8040/cross/creditPayConsume/asynResCallBack");
        // bean.put("asynResUrl2", "http://192.168.1.67:8040/cross/creditPayConsume/asynResCallBack");
        // bean.put("signMsg", CreditServiceTool.sign(bean,CreditConfig.reqSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd));

        return bean;
    }

    private BankResult checkResult(String content) throws Exception{
        BankResult bankResult = new BankResult();
        bankResult.setBankTime(new Date());
        if(StringUtils.isNotBlank(content)) {
            HashMap<String,String> responseMap = CreditServiceTool.parseResponse(content, CreditConfig.resSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd,CreditConfig.easyPublicKey);
            logger.info("=========消费银行返回========responseMap:"+responseMap);
            String responseCode = responseMap.get("responseCode");
            String responseRemark = responseMap.get("responseRemark");
            String sequenceNo = responseMap.get("sequenceNo");
            String merchantOrderNo = responseMap.get("merchantOrderNo");//商户订单编号
            String orderNo = responseMap.get("orderNo");//对账流水号
            String amount = responseMap.get("amount");//金额
            String settleDate = responseMap.get("settleDate");//settleDate
            switch(responseCode){
                case "0000":
                    bankResult.setOrderId(Long.valueOf(sequenceNo));
                    bankResult.setMerchantOrderId(merchantOrderNo);
                    bankResult.setBankOrderId(orderNo);
                    bankResult.setOrderAmount(new BigDecimal(amount).setScale(2,BigDecimal.ROUND_HALF_UP));
                    bankResult.setParam(responseMap.toString());
                    bankResult.setBankTime(dateFormat.parse(settleDate)); // 清算时间
                    String orderState = responseMap.get("orderState");
                    switch (orderState) {
                        case "01":
                            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                            bankResult.setBankResult("未支付");
                            break;
                        case "02":
                            bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                            bankResult.setBankResult("支付中");
                            break;
                        case "03":
                            bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                            bankResult.setBankResult("支付成功");
                            break;
                        case "04":
                            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                            bankResult.setBankResult("已作废");
                            break;
                        case "05":
                            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                            bankResult.setBankResult("退款中");
                            break;
                        case "06":
                            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                            bankResult.setBankResult("已撤销");
                            break;
                        case "07":
                            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                            bankResult.setBankResult("已退货");
                            break;
                        default:
                            bankResult = new BankResult(SystemConstant.BANK_STATUS_TIME_OUT, "error.5001");
                            bankResult.setBankResult("支付失败");
                            break;
                    }
                    break;
                case "S001":
                    bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                    bankResult.setBankResult("消费请求成功:" + responseRemark);
                    bankResult.setBankOrderId(orderNo);
                    bankResult.setParam(responseMap.toString());
                    break;
                default:
                    bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                    bankResult.setBankResult("消费请求失败:" + responseRemark);
                    bankResult.setParam(responseMap.toString());
                    break;
            }
        }else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("消费请求失败：消费返回结果为空！");
            bankResult.setParam(content);
        }
        logger.info("消费请求返回payment："+ JsonUtils.objectToJsonNonNull(bankResult));
        return bankResult;
    }

    /**
     * 消费异步返回结果
     */
    @RequestMapping("/asynResNotifyurl")
    @ResponseBody
    public String payMoneyNotifyurl(HttpServletRequest request) throws  Exception {
        request.setCharacterEncoding("UTF-8");
        String resStr = CreditHttper.read(request);
        resStr = URLDecoder.decode(resStr,"UTF-8").replaceAll(" ", "+");
        logger.info("消费(异步回调)返回参数"+resStr);
        if(StringUtils.isNotBlank(resStr)) {
            HashMap<String,String> responseMap = CreditServiceTool.parseResponse(resStr, CreditConfig.resSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd,CreditConfig.easyPublicKey);
            String responseCode = responseMap.get("responseCode");
            String responseRemark = responseMap.get("responseRemark");
            String sequenceNo = responseMap.get("sequenceNo");
            BankResult bankResult = new BankResult();
            bankResult.setOrderId(Long.valueOf(sequenceNo));// 商户订单号
            bankResult.setParam(responseMap.toString());
            bankResult.setBankTime(dateFormat.parse(responseMap.get("settleDate")) !=null ? dateFormat.parse(responseMap.get("settleDate")) : new Date()); // 清算时间
            bankResult.setOrderAmount(new BigDecimal(responseMap.get("amount")).setScale(2,BigDecimal.ROUND_HALF_UP));// 订单金额
            if ("0000".equals(responseCode)) {// 消费成功
                String orderState = responseMap.get("orderState");
                switch (orderState) {
                    case "01":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_UNPAID);
                        bankResult.setBankResult("未支付");
                        break;
                    case "02":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("支付中");
                        break;
                    case "03":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                        bankResult.setBankResult("支付成功");
                        break;
                    case "04":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("已作废");
                        break;
                    case "05":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("退款中");
                        break;
                    case "06":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("已撤销");
                        break;
                    case "07":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("已退货");
                        break;
                    default:
                        bankResult = new BankResult(SystemConstant.BANK_STATUS_TIME_OUT, "error.5001");
                        bankResult.setBankResult("支付失败");
                        break;
                }
            } else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                try {
                    bankResult.setBankCode(BankResultInfoCode.CommunicateCodeTwo.valueOf("A" + responseCode).getStatusMsg());
                } catch (Exception e) {
                    logger.info("CreditPay未定义的错误信息,错误编码为：" + responseCode);
                    bankResult.setBankCode("error.5000");
                }
                bankResult.setBankResult("消费失败：" + responseRemark);
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
        }else {
            logger.info("非法请求：消费失败");
            return "消费失败";
        }
    }

    @RequestMapping("/testPayConsume")
    @ResponseBody
    public BankResult testPayConsume(@RequestBody Object object) {
        logger.info("===============================开始测试消费=====================");
        SquareTrade trade = new SquareTrade();
        PayOrder payOrder = new PayOrder();
        TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
        ExtraChannelInfo extraChannelInfo = new ExtraChannelInfo();
        ChannelInfo chl = new ChannelInfo();
        chl.setOthers("{\"asynResUrl1\":\"http://boc.cmtbuy.com/cross/creditPay/creditRepayNotifyUrl\"}");

        tradeObjectSquare.setMerOrderId("M201001");
        payOrder.setPayId("201907271447001");
        tradeObjectSquare.setCardholderPhone("13599543918");
        tradeObjectSquare.setInAcctNo("6227001935600377518");
        tradeObjectSquare.setAmount(new BigDecimal("20"));
        tradeObjectSquare.setBankCardPhone("13599543918");
        extraChannelInfo.setData("{\"asynResUrl1\":\"http://boc.cmtbuy.com/cross/creditPay/creditRepayNotifyUrl\"}");

        trade.setChannelInfo(chl);
        trade.setTradeObjectSquare(tradeObjectSquare);
        trade.setPayOrder(payOrder);
        trade.setExtraChannelInfo(extraChannelInfo);

        try {
            return trade(trade);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
