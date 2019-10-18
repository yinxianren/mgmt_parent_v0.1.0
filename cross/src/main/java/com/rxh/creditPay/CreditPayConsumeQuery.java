package com.rxh.creditPay;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.PayOrder;
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
 * 信用卡消费接口查询
 */

@RestController
@RequestMapping("/creditPayConsumeQuery")
public class CreditPayConsumeQuery {

    @Autowired
    private PaymentInfo paymentInfo;

    private final static Logger logger = LoggerFactory.getLogger(CreditPayConsumeQuery.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 消费订单查询
     *
     * @param trade
     * @return
     */
    @RequestMapping("/trade")
    public BankResult trade(@RequestBody SquareTrade trade) throws Exception {
        BankResult bankResult = new BankResult();
        PayOrder payOrder = trade.getPayOrder();
        LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject others = JSON.parseObject(channelInfo.getOthers());

        param.put("serviceCode", "0200");
        param.put("processCode", "190002");
        param.put("merchantNo", others.getString("merchantNo"));
        param.put("merchantOrderNo", payOrder.getPayId());
        param.put("sequenceNo", String.valueOf(UUID.createKey("trans_order")));
        param.put("orderNo", payOrder.getOrgOrderId());

        Date bankTime = payOrder.getBankTime();
        String date = dateFormat.format(bankTime);

        param.put("orderDate", date);

        param.put("signMsg", CreditServiceTool.sign(param, CreditConfig.commReqSignFields, paymentInfo.getFpxKeyPath(), CreditConfig.merchantPrivateKeyPwd));

        String jsons = JsonUtils.objectToJson(param);
        String requestStr = null;
        requestStr = CreditServiceTool.encrypt(jsons, CreditConfig.easyPublicKey);

        logger.info("消费订单查询请求参数param：" + JSONObject.toJSONString(param));
        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), others.getString("creditPayConsumeQueryUrl"), requestStr, "UTF-8");
        // String result = "{\"msg\":\"成功\",\"code\":\"0000\",\"data\":{\"orderNumber\":\"2019070300003\",\"bussFlowNo\":\"20190703140405520521\"}}";
        // logger.info("查询返回结果result：" + content);
        if (StringUtils.isNotBlank(content)) {
            HashMap<String, String> responseMap = CreditServiceTool.parseResponse(content, CreditConfig.resSignFields, paymentInfo.getFpxKeyPath(), CreditConfig.merchantPrivateKeyPwd, CreditConfig.easyPublicKey);
            logger.info("===============消费订单查询返回结果=================responseMap：" + responseMap);
            String responseCode = responseMap.get("responseCode");
            String responseRemark = responseMap.get("responseRemark");
            String sequenceNo = responseMap.get("sequenceNo");
            String merchantOrderNo = responseMap.get("merchantOrderNo");//商户订单编号
            String reckonSeqNo = responseMap.get("reckonSeqNo");//对账流水号
            String amount = responseMap.get("amount");//金额
            String settleDate = responseMap.get("settleDate");//settleDate
            switch(responseCode){
                case "0000":
                    bankResult.setOrderId(Long.valueOf(sequenceNo));
                    bankResult.setMerchantOrderId(merchantOrderNo);
                    bankResult.setBankOrderId(reckonSeqNo);
                    bankResult.setOrderAmount(new BigDecimal(amount).setScale(2,BigDecimal.ROUND_HALF_UP));
                    bankResult.setParam(responseMap.toString());
                    bankResult.setBankTime(dateFormat.parse(settleDate)); // 清算时间
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
                    break;
                case "S001":
                    bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                    bankResult.setBankResult("消费请求成功:" + responseRemark);
                    bankResult.setParam(responseMap.toString());
                    break;
                default:
                    bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                    bankResult.setBankResult("消费请求失败:" + responseRemark);
                    bankResult.setParam(responseMap.toString());
                    break;
            }
        } else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
            bankResult.setBankResult("失败：消费订单查询返回结果为空！");
            bankResult.setParam(content);
        }
        logger.info("消费订单返回payment参数：" + JSONObject.toJSONString(bankResult));
        return bankResult;
    }

}
