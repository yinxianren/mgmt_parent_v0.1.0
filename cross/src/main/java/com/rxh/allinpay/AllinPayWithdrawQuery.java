package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.TransOrderInfoTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.PayException;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
    private final static Logger logger = LoggerFactory.getLogger(AllinPayWithdrawQuery.class);

    @RequestMapping("/withdrawQuery")
    public CrossResponseMsgDTO trade(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException, PayException, ParseException {
        TreeMap<String, Object> params = getTradeParam(trade);
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);
        logger.info("提现(付款)交易查询参数"+ JsonUtils.objectToJsonNonNull(params));
//        logger.info("提现(付款)交易查询请求地址"+json.getString("payWithdrawQueryUrl"));
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("payWithdrawQueryUrl"), params);
        logger.info("提现(付款)交易查询返回参数"+content);
        return checkResult(content);
    }

    private CrossResponseMsgDTO checkResult(String content) throws ParseException {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        bankResult.setChannelResponseTime(new Date());
        if(StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if(retcode.equals("SUCCESS")) {
                // 判断交易状态
                if (json.get("trxstatus") == null){
                    bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                    bankResult.setCrossResponseMsg("交易状态未知，请查询交易");
                    bankResult.setChannelResponseMsg(content);
                    return bankResult;
                }
                String trxstatus = json.get("trxstatus").toString();
                TreeMap<String,Object> resultTreeMap = JSON.parseObject(content,TreeMap.class);
                switch(trxstatus){
                    case "0000":
                        String trxid = resultTreeMap.get("trxid").toString();
                        String orderid = resultTreeMap.get("orderid").toString();
                        bankResult.setChannelResponseTime(new Date());
                        bankResult.setChannelOrderId(trxid);
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易成功");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossResponseMsg("交易已受理");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    case "3035":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易不存在");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00008.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00008.getMsg());
                        break;
                    case "0003":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    case "3054":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:" + json.get("errmsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }

            }else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易失败:" + json.get("retmsg"));
                bankResult.setChannelResponseMsg(content);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败：支付返回结果为空！");
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
//        bankResult.setCrossStatusCode(SystemConstant.BANK_STATUS_FAIL);
//        bankResult.setCrossResponseMsg("交易成功");
//        bankResult.setChannelResponseMsg(content);
        return bankResult;
    }

    private TreeMap<String, Object> getTradeParam(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        TransOrderInfoTable transOrder = trade.getTransOrderInfoTable();

        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);

        TreeMap<String, Object> treeMap = new TreeMap<>();
        String orgid = json.get("orgid").toString();//机构号
        String appid = json.get("appid").toString();//APPID
        String key = json.get("key").toString();
        String cusid = registerInfo.getString("cusid"); //商户号
        Date bankTime = transOrder.getUpdateTime();
        String date = dateFormat1.format(bankTime);
        String orderid = transOrder.getPlatformOrderId();
        String version = "11";

        treeMap.put("orgid",orgid);
        treeMap.put("appid",appid);
        treeMap.put("version",version);
        treeMap.put("cusid",cusid);
        treeMap.put("date",date);
        treeMap.put("orderid",orderid);
        treeMap.put("randomstr", AlinPayUtils.getRandomSecretkey());
        if (!StringUtils.isBlank(transOrder.getChannelOrderId()))
            treeMap.put("trxid",transOrder.getChannelOrderId());
        treeMap.put("key",key);
        treeMap.put("sign",AlinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");
        return treeMap;
    }


}
