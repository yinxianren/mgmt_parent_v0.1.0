package com.rxh.creditPay;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author phoenix
 * @date 20190727
 * 信用卡代偿代付订单/提现查询接口
 */

@RestController
@RequestMapping("/creditPayQuery")
public class CreditPayQuery {

    @Autowired
    private PaymentInfo paymentInfo;

    private final static Logger logger = LoggerFactory.getLogger(CreditPayQuery.class);
    private static SimpleDateFormat dateFormat =new SimpleDateFormat("yyyyMMdd");
    /**
     * 代付订单/提现查询
     * @param squareTrade
     * @return
     */
    @RequestMapping("/payQuery")
    public BankResult payment(@RequestBody SquareTrade squareTrade) throws Exception{
        BankResult bankResult = new BankResult();
        TransOrder transOrder = squareTrade.getTransOrder();
        LinkedHashMap<String,String> param = new LinkedHashMap<String,String>();
        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        JSONObject others = JSON.parseObject(channelInfo.getOthers());

        param.put("serviceCode","0300");
        param.put("processCode","290002");
        param.put("merchantNo",others.getString("merchantNo"));
        param.put("merchantOrderNo",transOrder.getTransId());
        param.put("sequenceNo",String.valueOf(UUID.createKey("trans_order")));
        param.put("orderNo",squareTrade.getTransOrder().getOrgOrderId());
        Date bankTime = transOrder.getBankTime();
        String date = dateFormat.format(bankTime);
        param.put("orderDate", date);
        param.put("signMsg", CreditServiceTool.sign(param,CreditConfig.commReqSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd));

        String jsons = JsonUtils.objectToJson(param);
        String requestStr = null;

        requestStr = CreditServiceTool.encrypt(jsons, CreditConfig.easyPublicKey);

        logger.info("代付查询请求参数param："+JSONObject.toJSONString(param));
        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), others.getString("payQueryUrl"), requestStr, "UTF-8");
        // String result = "{\"msg\":\"成功\",\"code\":\"0000\",\"data\":{\"orderNumber\":\"2019070300003\",\"bussFlowNo\":\"20190703140405520521\"}}";
        logger.info("代付查询返回结果result："+content);

        if(StringUtils.isNotBlank(content)) {
            HashMap<String,String> responseMap = CreditServiceTool.parseResponse(content, CreditConfig.resSignFields,paymentInfo.getFpxKeyPath(),CreditConfig.merchantPrivateKeyPwd,CreditConfig.easyPublicKey);
            logger.info("===============代付查询返回结果=================responseMap："+responseMap);
            content = JSONObject.toJSONString(responseMap);
            String resultCode = responseMap.get("responseCode");
            if ("S001".equals(resultCode)) {
                String orderState = responseMap.get("orderState");
                bankResult.setBankTime(new Date());
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
                        bankResult.setBankResult("付款失败");
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
                        bankResult.setBankResult("付款处理中，查询结果未知");
                        bankResult.setBankOrderId(responseMap.get("orderNo"));
                        bankResult.setParam(content);
                        break;
                }
            }else if("0000".equals(resultCode)){
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("付款成功");
                bankResult.setBankOrderId(responseMap.get("orderNo"));
                bankResult.setParam(content);
            }
            else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                bankResult.setBankResult("查询失败；"+CreditServiceTool.getResult(resultCode));
                bankResult.setParam(content);
            }
        }
         else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                bankResult.setBankResult("查询失败：交易查询返回结果为空！");
                bankResult.setParam(content);
            }
        logger.info("返回参数："+JSONObject.toJSONString(bankResult));
        return bankResult;
    }

    @RequestMapping("/testPayQuery")
    @ResponseBody
    public BankResult testPayQuery(@RequestBody Object object) {
        logger.info("===============================开始测试=====================");
        SquareTrade trade = new SquareTrade();
        TransOrder transOrder = new TransOrder();
        ChannelInfo chl = new ChannelInfo();
        chl.setOthers("{\n" +
                "  \"merchantNo\": \"102700000025\",\n" +
                "  \"payApplyUrl\": \"http://183.129.219.202:9021/repayment/repayment\",\n" +
                "  \"asynResUrl1\": \"http://boc.cmtbuy.com/cross/creditPay/creditRepayNotifyUrl\",\n" +
                "  \"payQueryUrl\": \"http://183.129.219.202:9021/repayment/agentPay\",\n" +
                "  \"queryUrl\": \"http://183.129.219.202:9021/repayment/query\"\n" +
                "}");
        transOrder.setBankTime(new Date());
        transOrder.setTransId("2019073016040004");
        transOrder.setMerOrderId("2019073016040004");
        trade.setTransOrder(transOrder);
        trade.setChannelInfo(chl);

        try {
            return payment(trade);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
