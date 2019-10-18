package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.MerchantRegisterCollect;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * 提现(付款)交易查询 /withdrawQuery
 */
@RestController
@RequestMapping("/allinPay")
public class AllinPayWithdrawQuery {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
    private final static Logger logger = LoggerFactory.getLogger(AllinPayWithdrawQuery.class);

    @RequestMapping("/withdrawQuery")
    public BankResult trade(@RequestBody SquareTrade trade) throws UnsupportedEncodingException, PayException, ParseException {
        TreeMap<String, Object> params = getTradeParam(trade);
        String other = trade.getChannelInfo().getOthers();
        JSONObject json = JSON.parseObject(other);
        logger.info("提现(付款)交易查询参数"+ JsonUtils.objectToJsonNonNull(params));
//        logger.info("提现(付款)交易查询请求地址"+json.getString("payWithdrawQueryUrl"));
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("payWithdrawQueryUrl"), params);
        logger.info("提现(付款)交易查询返回参数"+content);
        return checkResult(content);
    }


    @RequestMapping("/testWithdrawQuery")
    @ResponseBody
    public BankResult testWithdrawQuery(@RequestBody SquareTrade trade) throws UnsupportedEncodingException, ParseException {
        String version = "11";//版本号
        String orgid = "201004809979";//机构号
        String appid = "0000924";//APPID
        String key = "cccf15f597543e390a7e4c26bbad8ebf";
        String cusid ="101005129021";//商户号
        String orderid = "1907251723200670006";//商户订单号
        String date = "20190725";
        String trxid = "19070023674235";

        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.put("orgid", orgid);
        treeMap.put("appid", appid);
        treeMap.put("version", version);
        treeMap.put("cusid",cusid);
        treeMap.put("date",date);
        treeMap.put("orderid",orderid);
        treeMap.put("randomstr", AlinPayUtils.getRandomSecretkey());
        treeMap.put("trxid",trxid);
        treeMap.put("key",key);
        treeMap.put("sign",AlinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");

        logger.info("提现(付款)交易参数：" + JsonUtils.objectToJsonNonNull(treeMap));
//         String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://test.allinpaygd.com/ipayapiweb/acct/querypay", treeMap);// 测试地址
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://ipay.allinpay.com/apiweb/acct/querypay", treeMap);// 生产地址
        logger.info("提现(付款)交易返回：" + content);
        return checkResult(content);
    }

    private BankResult checkResult(String content) throws ParseException {
        BankResult bankResult = new BankResult();
        bankResult.setBankTime(new Date());
        if(StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if(retcode.equals("SUCCESS")) {
                // 判断交易状态
                if (json.get("trxstatus") == null){
                    bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                    bankResult.setBankResult("交易状态未知，请查询交易");
                    bankResult.setParam(content);
                    return bankResult;
                }
                String trxstatus = json.get("trxstatus").toString();
                TreeMap<String,Object> resultTreeMap = JSON.parseObject(content,TreeMap.class);
                switch(trxstatus){
                    case "0000":
                        String trxid = resultTreeMap.get("trxid").toString();
                        String orderid = resultTreeMap.get("orderid").toString();
                        bankResult.setBankTime(new Date());
                        bankResult.setBankOrderId(trxid);
                        bankResult.setOrderId(Long.valueOf(orderid));
                        bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                        bankResult.setBankResult("交易成功");
                        bankResult.setParam(content);
                        break;
                    case "2000":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("交易已受理");
                        bankResult.setParam(content);
                        break;
                    case "3035":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("交易不存在");
                        bankResult.setParam(content);
                        break;
                    case "0003":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("交易处理中，请查询交易");
                        bankResult.setParam(content);
                        break;
                    case "3054":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("交易处理中，请查询交易");
                        bankResult.setParam(content);
                        break;
                    default:
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("交易失败:" + json.get("errmsg"));
                        bankResult.setParam(content);
                        break;
                }

            }else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("交易失败:" + json.get("retmsg"));
                bankResult.setParam(content);
            }
        }else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("交易失败：支付返回结果为空！");
            bankResult.setBankCode("error.5000");
        }
//        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
//        bankResult.setBankResult("交易成功");
//        bankResult.setParam(content);
        return bankResult;
    }

    private TreeMap<String, Object> getTradeParam(SquareTrade trade) throws UnsupportedEncodingException {
        MerchantRegisterCollect merchantRegisterCollect = trade.getMerchantRegisterCollect();
        TransOrder transOrder = trade.getTransOrder();

        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getBackData());
        String other = trade.getChannelInfo().getOthers();
        JSONObject json = JSON.parseObject(other);

        TreeMap<String, Object> treeMap = new TreeMap<>();
        String orgid = json.get("orgid").toString();//机构号
        String appid = json.get("appid").toString();//APPID
        String key = json.get("key").toString();
        String cusid = registerInfo.getString("cusid"); //商户号
        Date bankTime = transOrder.getBankTime();
        String date = dateFormat1.format(bankTime);
        String orderid = transOrder.getTransId();
        String version = "11";

        treeMap.put("orgid",orgid);
        treeMap.put("appid",appid);
        treeMap.put("version",version);
        treeMap.put("cusid",cusid);
        treeMap.put("date",date);
        treeMap.put("orderid",orderid);
        treeMap.put("randomstr", AlinPayUtils.getRandomSecretkey());
        if (!StringUtils.isBlank(transOrder.getOrgOrderId()))
            treeMap.put("trxid",transOrder.getOrgOrderId());
        treeMap.put("key",key);
        treeMap.put("sign",AlinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");
        return treeMap;
    }


}
