package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.PayException;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TreeMap;

/**
 * 快捷交易支付确认 allinPay confirm
 * @author admin
 *
 */
@Controller
@RequestMapping("/allinPay")
public class AllinPayConfirmPay {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(AllinPayConfirmPay.class);

    @RequestMapping("/confirm")
    @ResponseBody
    public CrossResponseMsgDTO trade(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException, PayException, ParseException, InterruptedException {
        TreeMap<String, Object> params = getTradeParam(trade);
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);
        logger.info("交易确认请求参数"+JsonUtils.objectToJsonNonNull(params));
        logger.info("交易确认请求地址"+json.getString("payConfirmUrl"));
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("payConfirmUrl"), params);
//        String content = "{\"appid\":\"6666678\",\"fintime\":\"20190628100547\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"B64DFBAD0AFEEC07EBF5F88D7AF04677\",\"trxid\":\"19060000001375\",\"trxstatus\":\"0000\"}";
        logger.info("交易确认银行返回参数"+content);
        return checkResult(content);
    }

    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getTradeParam(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
        logger.info("通联快捷支付交易确认param", JsonUtils.objectToJsonNonNull(trade));
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        String tradeResult = payOrder.getChannelRespResult();
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject date = JSON.parseObject(other);
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        //公共参数
        String orgid = date.get("orgid").toString();//机构号
        String appid = date.get("appid").toString();//APPID
        String cusid = registerInfo.get("cusid").toString();//商户号
        String key =   date.get("key").toString();
        String randomstr = AlinPayUtils.getRandomSecretkey();//随机字符串

        //私有参数
        JSONObject agreeid = JSONObject.parseObject(merchantCard.getChannelRespResult());//协议编号
        //非必须参数
        String smscode = merchantCard.getSmsCode();//短信验证码
        JSONObject bankParam = JSON.parseObject(tradeResult);
        String trxid = bankParam.getString("trxid");//平台交易流水号
        String thpinfo = bankParam.getString("thpinfo");//交易透传信息
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("cusid", cusid);
        treeMap.put("trxid", trxid);
        treeMap.put("agreeid", agreeid.getString("agreeid"));
        treeMap.put("smscode", smscode);
        if(StringUtils.isNotBlank(thpinfo)) {
            treeMap.put("thpinfo", thpinfo);
        }
        treeMap.put("orgid", orgid);
        treeMap.put("appid", appid);
        treeMap.put("randomstr", randomstr);
        treeMap.put("key", key);
        treeMap.put("sign", AlinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");
        return treeMap;
    }

    /**
     * 交易结果处理
     * @param content
     * @return
     * @throws PayException
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    private CrossResponseMsgDTO checkResult(String content) throws PayException, UnsupportedEncodingException, ParseException {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        if(StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if(retcode .equals("SUCCESS")) {
                // 判断交易状态
                String trxstatus = json.get("trxstatus").toString();
                switch(trxstatus){
                    case "0000":
                        String fintime = json.getString("fintime");
                        String trxid = json.getString("trxid");
                        bankResult.setChannelResponseTime(dateFormat.parse(fintime));
                        bankResult.setChannelOrderId(trxid);
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易成功");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "1999":
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("需获取短信验证码,进行下一步确认操作");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易已受理");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "3059":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信验证码发送失败");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    case "3057":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("请重新获取验证码");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00003.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00003.getMsg());
                        break;
                    case "3058":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信验证码错误");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00004.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00004.getMsg());
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
        return bankResult;
    }

}
