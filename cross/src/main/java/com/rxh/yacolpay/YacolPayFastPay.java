package com.rxh.yacolpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.pojo.Result;
import com.rxh.utils.*;
import com.rxh.utils.UUID;
import com.rxh.yacolpay.utils.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author monkey
 * @data 20190703
 * 雅酷快捷支付
 */
@RestController
@RequestMapping("/yacolPay")
public class YacolPayFastPay {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(YacolPayFastPay.class);
    @Autowired
    private PaymentInfo paymentInfo;

    /**
     * 卡要素支付申请
     * @param squareTrade
     * @return
     */
    @RequestMapping("/bankCardPay")
    public CrossResponseMsgDTO bankCardPay(@RequestBody RequestCrossMsgDTO squareTrade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        PayOrderInfoTable payOrder = squareTrade.getPayOrderInfoTable();
        RegisterCollectTable merchantRegisterCollect = squareTrade.getRegisterCollectTable();
        RegisterInfoTable merchantRegisterInfo = squareTrade.getRegisterInfoTable();
        String  outTradeNo = payOrder.getPlatformOrderId();
        ChannelInfoTable channelInfo = squareTrade.getChannelInfoTable();
        JSONObject others = JSONObject.parseObject(channelInfo.getChannelParam());
        String publicKey = others.getString("publicKey").trim();
        String signKey = others.getString("privateKey").trim();
        String publicCheckKey =  others.getString("publicCheckKey").trim();
        TreeMap param = new TreeMap();
        //公共参数
        param.put("service","mimer_bank_card_pay");//接口名称
        param.put("version","1.0");//版本号
        param.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//请求时间
        param.put("partner_id",others.getString("partner_id"));//平台商户号
        param.put("_input_charset","utf-8");//字符集
        param.put("notify_url",others.get("notify_url"));
        //业务参数
        param.put("device_id",payOrder.getDeviceId());
        param.put("device_type",payOrder.getDeviceType());
        param.put("mac_address",payOrder.getMacAddr());
        param.put("out_trade_no",outTradeNo);//商户订单号
        param.put("summary","商品交易");//交易内容摘要
        param.put("payer_id",JSONObject.parseObject(merchantRegisterCollect.getChannelRespResult()).getString("mimer_member_id"));//小微商户系统用户ID
        param.put("payee_id",payOrder.getTerminalMerId());//收款方用户标识
        param.put("split_type","2");//分账类型,1-按固定金额；2-按固定比率；
        if (param.get("split_type").equals("1")){
            param.put("split_amount","2.00");//分账金额
        }else {
            param.put("split_ratio",payOrder.getPayFee().toString());//分账比率
        }
//        param.put("split_ratio","2.5");
        param.put("amount",payOrder.getAmount().setScale(2).toString());//金额
        param.put("bank_code",merchantRegisterCollect.getBankCode());//银行编码
        if (merchantRegisterInfo.getMerchantType().equals("00")){
            param.put("card_attribute","B");//卡属性
        }else {
            param.put("card_attribute","C");//卡属性
        }
        if (payOrder.getBankCardType()==1){
            param.put("card_type","DEBIT");//卡类型 借记
        }else{
            param.put("card_type","CREDIT");//卡类型 贷记
            byte[] validity_period_byte = null;
            byte[] verification_value_byte = null;
            validity_period_byte =YaColPayRSAUtil.encryptByPublicKey((payOrder.getValidDate().substring(0,2)+"/"+payOrder.getValidDate().substring(2,4)).getBytes("utf-8"),publicKey);
            verification_value_byte =YaColPayRSAUtil.encryptByPublicKey(payOrder.getSecurityCode().getBytes("utf-8"),publicKey);
            String validity_period_encrypt = YaColIPayBase64.encode(validity_period_byte);
            String verification_value_encrypt=YaColIPayBase64.encode(verification_value_byte);
            param.put("validity_period",validity_period_encrypt);
            param.put("verification_value",verification_value_encrypt);
        }
        param.put("cert_type","IC");//证件类型
        param.put("payer_ip",squareTrade.getIP());//IP地址
        param.put("settle_account_type","BXT_D0_SETTLE");//结算账户类型,BXT_BASIC基本账户，BXT_D0_SETTLE D0账户
        //敏感数据加密
        byte[] bank_card_no = null;
        byte[] account_name = null;
        byte[] cert_no = null;
        byte[] phone_no = null;


        try {
            bank_card_no = YaColPayRSAUtil.encryptByPublicKey(payOrder.getBankCardNum().getBytes("utf-8"), publicKey);
            account_name = YaColPayRSAUtil.encryptByPublicKey(merchantRegisterCollect.getCardHolderName().getBytes("utf-8"), publicKey);
            cert_no = YaColPayRSAUtil.encryptByPublicKey(merchantRegisterInfo.getIdentityNum().getBytes("utf-8"), publicKey);
            phone_no = YaColPayRSAUtil.encryptByPublicKey(payOrder.getBankCardPhone().getBytes("utf-8"), publicKey);
        }catch(Exception e){
            e.printStackTrace();
        }
        String bank_card_no_encrypt = YaColIPayBase64.encode(bank_card_no);
        String account_name_encrypt = YaColIPayBase64.encode(account_name);
        String cert_no_encrypt = YaColIPayBase64.encode(cert_no);
        String phone_no_encrypt = YaColIPayBase64.encode(phone_no);

        param.put("bank_card_no", bank_card_no_encrypt);//银行卡号
        param.put("account_name",account_name_encrypt);//持卡人姓名
        param.put("cert_no",cert_no_encrypt);//证件号码
        param.put("phone_no",phone_no_encrypt);//手机号
        param.put("sign", YacolPayUtil.getSign(param,signKey));
        param.put("sign_type","RSA");//加密方式

        String params = YaColIPayTools.createLinkString(param, true);
        logger.info("雅酷支付申请param:"+param);
        try {
            String result = URLDecoder.decode(
                    CallServiceUtil.sendPost(others.getString("bankCardPayUrl"), params), "UTF-8");
            logger.info("雅酷支付申请result:"+result);
            Map content = JSON.parseObject(result,Map.class);
            String sign_result = content.get("sign").toString();
            String sign_type_result = content.get("sign_type").toString();
            String _input_charset_result = content.get("_input_charset")
                    .toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");
            String like_result = YaColIPayTools.createLinkString(content, false);
            if (YaColIPaySignUtil.Check_sign(like_result, sign_result,
                    sign_type_result, publicCheckKey, _input_charset_result)) {
                String responseCode = content.get("response_code").toString();
                switch (responseCode){
                    case "APPLY_SUCCESS" :
                        String tradeStatus = content.get("trade_status").toString();
                        switch (tradeStatus){
                            case "WAIT_PAY" :
                                bankResult.setCrossResponseMsg("提交成功,等待付款");
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                break;
                            case "PAY_FINISHED" :
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                bankResult.setCrossResponseMsg("提交成功,已付款");
                                break;
                            case "TRADE_FAILED" :
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("交易失败");
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                break;
                            case "TRADE_FINISHED" :
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                bankResult.setCrossResponseMsg("提交成功,交易结束");
                                break;
                            case "TRADE_CLOSED" :
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("交易关闭");
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                break;
                        }
                        bankResult.setChannelOrderId(content.get("out_trade_no").toString());
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        break;
                    case "ADVANCE_FAILED" :
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信发送失败");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:"+content.get("error_message"));
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易失败,验签不通过");
                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败，系统错误");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        logger.info("雅酷支付申请返回结果"+JSONObject.toJSONString(bankResult));
        return bankResult;
    }

//    public CrossResponseMsgDTO bindCardPay(@RequestBody RequestCrossMsgDTO squareTrade) throws Exception {
//        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
//        PayOrder payOrder = squareTrade.getPayOrder();
//        MerchantRegisterCollect merchantRegisterCollect = squareTrade.getMerchantRegisterCollect();
//        MerchantRegisterInfo merchantRegisterInfo = squareTrade.getMerchantRegisterInfo();
//        ChannelInfo channelInfo = squareTrade.getChannelInfo();
//        JSONObject others = JSONObject.parseObject(channelInfo.getOthers());
//        String  outTradeNo = payOrder.getMerOrderId();
//        String signKey = others.getString("privateKey");
//        TreeMap param = new TreeMap();
//        //公共参数
//        param.put("service","fastpay_bind_card_pay");//接口名称
//        param.put("version","1.0");//版本号
//        param.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//请求时间
//        param.put("partner_id",others.getString("partnerId"));//平台商户号
//        param.put("input_charset","UTF-8");//字符集
//        param.put("sign_type","RSA");//加密方式
//        //私有参数
//        param.put("out_trade_no",outTradeNo);//商户订单号
//        param.put("summary","商品交易");//交易内容摘要
//        param.put("payer_id",payOrder.getTerminalMerId());//商户系统用户ID
//        param.put("amount",payOrder.getAmount().multiply(new BigDecimal(100)).setScale(0));//金额
//        param.put("payer_ip","");//IP地址
//        param.put("card_id",squareTrade.getMerchantCard().getResult());//卡ID绑卡返回
//        param.put("sign", YacolPayUtil.getSign(param,signKey));
//        String params = YaColIPayTools.createLinkString(param, true);
//        logger.info("雅酷支付申请param:"+params);
//        try {
//            String result = URLDecoder.decode(
//                    HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), others.getString("cardConfirmUrl"), params), "UTF-8");
//            logger.info("雅酷支付申请result:"+result);
//            Map<String, String> content = JSON.parseObject(result,Map.class);
//            String sign_result = content.get("sign").toString();
//            String sign_type_result = content.get("sign_type").toString();
//            String _input_charset_result = content.get("_input_charset")
//                    .toString();
//            content.remove("sign");
//            content.remove("sign_type");
//            content.remove("sign_version");
//            String like_result = YaColIPayTools.createLinkString(content, false);
//            if (YaColIPaySignUtil.Check_sign(like_result.toString(), sign_result,
//                    sign_type_result, signKey, _input_charset_result)) {
//                String responseCode = content.get("response_code").toString();
//                switch (responseCode){
//                    case "APPLY_SUCCESS" :
//                        bankResult.setCrossStatusCode(SystemConstant.BANK_STATUS_SUCCESS);
//                        bankResult.setCrossResponseMsg("提交成功");
//                        bankResult.setChannelOrderId(content.get("out_pay_no"));
//                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
//                        break;
//                    case "ADVANCE_FAILED" :
//                        bankResult.setCrossStatusCode(SystemConstant.BANK_STATUS_SUCCESS);
//                        bankResult.setCrossResponseMsg("短信发送失败");
//                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
//                        break;
//                    default:
//                        bankResult.setCrossStatusCode(SystemConstant.BANK_STATUS_FAIL);
//                        bankResult.setCrossResponseMsg("交易失败"+content.get("response_message"));
//                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
//                        break;
//                }
//            } else {
//                bankResult.setCrossStatusCode(SystemConstant.BANK_STATUS_FAIL);
//                bankResult.setCrossResponseMsg("交易失败,验签不通过");
//                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            bankResult.setCrossStatusCode(SystemConstant.BANK_STATUS_FAIL);
//            bankResult.setCrossResponseMsg("交易失败，系统错误");
//            bankResult.setChannelResponseMsg(e.getMessage());
//        }
//        return bankResult;
//    }

    /**
     * 支付确认
     * @param squareTrade
     * @return
     * @throws Exception
     */
    @RequestMapping("/advancePay")
    public CrossResponseMsgDTO advancePay(@RequestBody RequestCrossMsgDTO squareTrade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        PayOrderInfoTable payOrder = squareTrade.getPayOrderInfoTable();
        JSONObject trxidJson = JSON.parseObject(payOrder.getChannelRespResult());
        ChannelInfoTable channelInfo = squareTrade.getChannelInfoTable();
        JSONObject others = JSONObject.parseObject(channelInfo.getChannelParam());
        String signKey = others.getString("privateKey");
        String publicCheckKey = others.getString("publicCheckKey");
        TreeMap param = new TreeMap();
        //公共参数
        param.put("service","mimer_advance_pay");//接口名称
        param.put("version","1.0");//版本号
        param.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//请求时间
        param.put("partner_id",others.getString("partner_id"));//平台商户号
        param.put("_input_charset","UTF-8");//字符集
        param.put("notify_url",others.get("notify_url"));
        //私有参数
        param.put("out_advance_no", UUID.createKey("pay_order"));
        param.put("ticket",trxidJson.getString("ticket"));
        param.put("validate_code",payOrder.getSmsCode());
        param.put("user_ip",squareTrade.getIP());
        param.put("sign", URLEncoder.encode(YacolPayUtil.getSign(param,signKey),"utf-8"));
        param.put("sign_type","RSA");//加密方式
        logger.info("雅酷支付确认param:"+param);
        try {
            String result = URLDecoder.decode(
                    HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(),others.getString("bankCardPayUrl"), param), "UTF-8");
            logger.info("雅酷支付确认result:"+result);
            Map content = JSON.parseObject(result,Map.class);
            String sign_result = content.get("sign").toString();
            String sign_type_result = content.get("sign_type").toString();
            String _input_charset_result = content.get("_input_charset")
                    .toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");
            String like_result = YaColIPayTools.createLinkString(content, false);
            if (YaColIPaySignUtil.Check_sign(like_result, sign_result,
                    sign_type_result, publicCheckKey, _input_charset_result)) {
                String responseCode = content.get("response_code").toString();
                switch (responseCode){
                    case "APPLY_SUCCESS" :
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易成功");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        break;
                    case "ADVANCE_FAILED" :
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信发送失败");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:"+content.get("error_message"));
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易失败,验签不通过");
                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败，系统错误");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        return bankResult;
    }

    /**
     * 支付短信重发
     * @param squareTrade
     * @return
     * @throws Exception
     */
    @RequestMapping("/paySMS")
    public CrossResponseMsgDTO paySMS(@RequestBody RequestCrossMsgDTO squareTrade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        PayOrderInfoTable payOrder = squareTrade.getPayOrderInfoTable();
        JSONObject trxidJson = JSON.parseObject(payOrder.getChannelRespResult());
        ChannelInfoTable channelInfo = squareTrade.getChannelInfoTable();
        JSONObject others = JSONObject.parseObject(channelInfo.getChannelParam());
        String signKey = others.getString("privateKey");
        String publicCheckKey = others.getString("publicCheckKey");
        TreeMap param = new TreeMap();
        //公共参数
        param.put("service","mimer_pay_sms");//接口名称
        param.put("version","1.0");//版本号
        param.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//请求时间
        param.put("partner_id",others.getString("partner_id"));//平台商户号
        param.put("_input_charset","UTF-8");//字符集
        param.put("notify_url",others.get("notify_url"));
        //私有参数
        param.put("ticket",trxidJson.getString("ticket"));
        param.put("sign", URLEncoder.encode(YacolPayUtil.getSign(param,signKey),"utf-8"));
        param.put("sign_type","RSA");//加密方式
        logger.info("雅酷支付短信重发param:"+param);
        try {
            String result = URLDecoder.decode(
                    HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(),others.getString("bankCardPayUrl"), param), "UTF-8");
            logger.info("雅酷支付短信重发result:"+result);
            Map content = JSON.parseObject(result,Map.class);
            String sign_result = content.get("sign").toString();
            String sign_type_result = content.get("sign_type").toString();
            String _input_charset_result = content.get("_input_charset")
                    .toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");
            String like_result = YaColIPayTools.createLinkString(content, false);
            if (YaColIPaySignUtil.Check_sign(like_result.toString(), sign_result,
                    sign_type_result, publicCheckKey, _input_charset_result)) {
                String responseCode = content.get("response_code").toString();
                switch (responseCode){
                    case "APPLY_SUCCESS" :
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("短信发送成功");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        break;
                    case "ADVANCE_FAILED" :
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信发送失败");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    case "PAY_SMS_FAILED" :
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信重发失败");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:"+content.get("error_message"));
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易失败,验签不通过");
                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败，系统错误");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        return bankResult;
    }

    /**
     * 成功交易结果通知
     * @param yaColTradeObject
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/fastPayNotifyurl")
    @ResponseBody
    public String payMoneyNotifyurl(@RequestBody String yaColTradeObject) throws Exception {
        logger.info("=============雅酷支付异步回调===============："+yaColTradeObject);
        if (!StringUtils.isBlank(yaColTradeObject)){
            logger.info("======雅酷支付异步回调成功====");
            return "success";
        }
        String publicCheckKey = "";
        Map map = new TreeMap<>();
        map = JSONObject.parseObject(yaColTradeObject,Map.class);
        String sign_result = map.get("sign").toString();
        String sign_type_result = map.get("sign_type").toString();
        String _input_charset_result = map.get("_input_charset").toString();
        map.remove("sign");
        map.remove("sign_type");
        map.remove("sign_version");
        String like_result = YaColIPayTools.createLinkString(map, false);
        if (YaColIPaySignUtil.Check_sign(like_result.toString(), sign_result, sign_type_result, publicCheckKey, _input_charset_result)) {
            /**
             支付结果处理
             */
            CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
            bankResult.setChannelOrderId((map.get("getOuter_trade_no()").toString()));// 商户订单号
            bankResult.setChannelOrderId(map.get("getInner_trade_no()").toString());// (商户交易流水号)
            bankResult.setChannelResponseMsg(JsonUtils.objectToJson(yaColTradeObject));
            Date date = DateUtils.dateFormat(dateFormat, map.get("getNotify_time()").toString());
            bankResult.setChannelResponseTime(date == null ? new Date() : date);
            if ("PAY_FINISHED".equals(map.get("getTrade_status()"))) {// 支付成功
                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                bankResult.setCrossResponseMsg("付款成功");
                bankResult.setChannelResponseMsg(JsonUtils.objectToJson(yaColTradeObject));
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                logger.info("YacolPay错误信息,错误编码为：" + map.get("getTrade_status()"));
                bankResult.setCrossResponseMsg("付款失败：" + map.get("getError_message()"));
                bankResult.setChannelResponseMsg(JsonUtils.objectToJson(yaColTradeObject));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
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
        }else{
            logger.info("非法请求：签名信息验证失败");
            return "签名信息验证失败";
        }
    }

    @RequestMapping("/queryOrder")
    public CrossResponseMsgDTO queryOrder(@RequestBody RequestCrossMsgDTO trade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        ChannelInfoTable channelInfo = trade.getChannelInfoTable();
        JSONObject others = JSONObject.parseObject(channelInfo.getChannelParam());
        String signKey = others.getString("privateKey");
        String publicCheckKey = others.getString("publicCheckKey");
        TreeMap param = new TreeMap();
        //公共参数
        param.put("service","mimer_query_single_pay");//接口名称
        param.put("version","1.0");//版本号
        param.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//请求时间
        param.put("partner_id",others.getString("partner_id"));//平台商户号
        param.put("_input_charset","UTF-8");//字符集
        //私有参数
        param.put("out_trade_no",payOrder.getPlatformOrderId());
        param.put("sign", URLEncoder.encode(YacolPayUtil.getSign(param,signKey),"utf-8"));
        param.put("sign_type","RSA");//加密方式
        logger.info("雅酷支付查询参数param:"+param);
        try {
            String result = URLDecoder.decode(
                    HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), others.getString("bankCardPayUrl"), param), "UTF-8");
            logger.info("雅酷支付查询返回result:" + result);
            Map content = JSON.parseObject(result, Map.class);
            String sign_result = content.get("sign").toString();
            String sign_type_result = content.get("sign_type").toString();
            String _input_charset_result = content.get("_input_charset")
                    .toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");
            String like_result = YaColIPayTools.createLinkString(content, false);
            if (YaColIPaySignUtil.Check_sign(like_result, sign_result,
                    sign_type_result, publicCheckKey, _input_charset_result)) {
                String responseCode = content.get("response_code").toString();
                String trade_status = content.get("trade_status")==null?"":content.get("trade_status").toString();
                if (responseCode.equals("APPLY_SUCCESS")) {
                    switch (trade_status) {
                        case "WAIT_PAY":
                            bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                            bankResult.setCrossResponseMsg("等待付款");
                            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                            break;
                        case "PAY_FINISHED":
                            bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                            bankResult.setCrossResponseMsg("已付款");
                            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                            break;
                        case "TRADE_FAILED":
                            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                            bankResult.setCrossResponseMsg("交易失败");
                            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                            break;
                        case "TRADE_FINISHED":
                            bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                            bankResult.setCrossResponseMsg("交易结束");
                            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                            break;
                        case "TRADE_CLOSED":
                            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                            bankResult.setCrossResponseMsg("交易关闭");
                            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                            break;
                        default:
                            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                            bankResult.setCrossResponseMsg("交易状态未知:" + content.get("error_message"));
                            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                            break;
                    }
                } else {
                    bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                    bankResult.setCrossResponseMsg("交易查询失败:" + content.get("error_message"));
                    bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                    bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                    bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易查询失败,验签不通过");
                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
            return bankResult;
        }catch (Exception e){
            logger.error("交易查询失败:"+e);
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易查询失败，系统内部错误");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            return bankResult;
        }
    }
}
