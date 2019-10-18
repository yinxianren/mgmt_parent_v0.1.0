package com.rxh.yacolpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantBankCardBinding;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.*;
import com.rxh.yacolpay.utils.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;



/**
 * @author phoenix
 * @data 20190703
 * 雅酷单笔代付
 */
@Controller
@RequestMapping("/yacolSinglePayToBank")
public class YacolSinglePayToBank {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(YacolSinglePayToBank.class);
    @Autowired
    private PaymentInfo paymentInfo;


    /**
     * 雅酷单笔代付申请
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/payMoney")
    @ResponseBody
    public BankResult payMoney(@RequestBody SquareTrade trade) throws UnsupportedEncodingException {
        TradeObjectSquare tradeObjectSquare = trade.getTradeObjectSquare();
        ChannelInfo channelInfo = trade.getChannelInfo();
        BankResult bankResult = new BankResult();
        TransOrder transOrder = trade.getTransOrder();
        JSONObject others = JSONObject.parseObject(channelInfo.getOthers());
        String publicKey = others.getString("publicKey").trim();;
        String signKey = others.getString("privateKey").trim();
        String publicCheckKey =others.getString("publicCheckKey".trim());

        //基本参数
        String service = "mimer_single_pay2bank";//接口
        String version = "1.0";//接口版本
        String request_time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");//请求时间
        String  partner_id = others.getString("partner_id"); //合作者Id
        String _input_charset = "UTF-8";//字符编码集
        String sign_type="RSA";//签名类型
        String notify_url =others.getString("notify_url"); ;

        //请求参数
        String out_trade_no = transOrder.getTransId();//商户订单号
        String identity_id = transOrder.getTerminalMerId();//收款方用户标识
        String bank_account_num = tradeObjectSquare.getBankCardNum();//收款方银行账号
        String phone_no = tradeObjectSquare.getBankCardPhone();//银行预留手机号
        String bank_name = tradeObjectSquare.getBankName();//收款方开户银行
        String bank_code = tradeObjectSquare.getBankCode();//银行代码
//        String province  = merchantBankCardBinding.getProvince();//省份
//        String city =merchantBankCardBinding.getCity();//城市
//        String bank_branch = tradeObjectSquare.getBankBranchName();//支行名称
        String amount = transOrder.getAmount().setScale(2).toString();
        String backFee = tradeObjectSquare.getBackFee().setScale(2).toString();

        Map postData = new TreeMap();
        String split_type = "1";//分账类型1-按固定金额；2-按固定比率；
        if (trade.getMerchantRegisterInfo().getMerchantType().equals("00")){
            postData.put("card_attribute","B");//卡属性
        }else {
            postData.put("card_attribute","C");//卡属性
        }
        if (tradeObjectSquare.getBankCardType()==1){
            postData.put("card_type","DEBIT");//卡类型 借记
        }else{
            postData.put("card_type","CREDIT");//卡类型 贷记
        }
        String account_type = "BXT_D0_SETTLE";//账户类型

        byte[] bank_account_num_byte = null;
        byte[] phone_no_byte = null;
        try {
            bank_account_num_byte = YaColPayRSAUtil.encryptByPublicKey(bank_account_num.getBytes("UTF-8"), publicKey);
            phone_no_byte = YaColPayRSAUtil.encryptByPublicKey(phone_no.getBytes("UTF-8"), publicKey);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String bank_account_num_encrypt = YaColIPayBase64.encode(bank_account_num_byte);
        String phone_no_encrypt = YaColIPayBase64.encode(phone_no_byte);
        postData.put("service",service);
        postData.put("version",version);
        postData.put("request_time", request_time);
        postData.put("partner_id",partner_id);
        postData.put("_input_charset",_input_charset);
        postData.put("notify_url",notify_url);
        postData.put("out_trade_no",out_trade_no);  //商户订单号
        postData.put("identity_id",identity_id); //收款方用户标识
        postData.put("bank_name",bank_name);//收款方开户银行
        postData.put("bank_code",bank_code);//银行代码
        postData.put("amount",amount);//金额
        postData.put("split_amount",backFee);    //分账金额
        postData.put("split_type",split_type); //分账类型
        postData.put("account_type",account_type);//账户类型
//        postData.put("province",province);//省份
//        postData.put("city",city);//城市
//        postData.put("bank_branch",bank_branch);//支行名称
        postData.put("bank_account_num",bank_account_num_encrypt);//收款方银行账号
        postData.put("phone_no",phone_no_encrypt);//银行预留手机号

        String contents = YaColIPayTools.createLinkString(postData,false);
        logger.info("雅酷代付申请签名前postDatas:"+postData.toString());
        //签名
        try {
            postData.put("sign", YaColPayRSAUtil.sign(contents,signKey,"utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        postData.put("sign_type",sign_type);

        String postDatas = YaColIPayTools.createLinkString(postData, true);
        logger.info("雅酷代付申请postDatas:"+postDatas);
        try {
            String result =URLDecoder.decode(
                              CallServiceUtil.sendPost(others.getString("masRequestUrl"), postDatas),"UTF-8");
            logger.info("雅酷代付申请result:"+result);
            Map<String, String> content = JSON.parseObject(result,Map.class);
            String sign_result = content.get("sign");
            String sign_type_result = content.get("sign_type");
            String _input_charset_result = content.get("_input_charset")
                    .toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");
            String like_result = YaColIPayTools.createLinkString(content, false);
            if (YaColIPaySignUtil.Check_sign(like_result, sign_result,
                    sign_type_result, publicCheckKey, _input_charset_result)) {

                String responseCode = content.get("response_code");
                switch (responseCode){
                    case "APPLY_SUCCESS" :
                        String tradeStatus = content.get("withdraw_status");
                        switch (tradeStatus){
                            case "SUCCESS" :
                                bankResult.setBankResult("成功");
                                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                                bankResult.setParam(JSONObject.toJSONString(content));
                                break;
                            case "FAILED" :
                                bankResult.setBankResult("失败");
                                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                                bankResult.setParam(JSONObject.toJSONString(content));
                                break;
                            case "PROCESSING" :
                                bankResult.setBankResult("处理中");
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setParam(JSONObject.toJSONString(content));
                                break;
                            case "RETURNT_TICKET" :
                                bankResult.setBankResult("银行退票");
                                bankResult.setParam(JSONObject.toJSONString(content));
                                break;
                        }

                        bankResult.setBankOrderId(content.get("out_trade_no"));
                        bankResult.setParam(JSONObject.toJSONString(content));
                        break;
                    case "ADVANCE_FAILED" :
                        bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                        bankResult.setBankResult("短信发送失败");
                        bankResult.setParam(JSONObject.toJSONString(content));
                        break;
                    default:
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("交易失败"+content.get("response_message"));
                        bankResult.setParam(JSONObject.toJSONString(content));
                        break;
                }
            } else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("交易失败,验签不通过");
                bankResult.setParam(JSONObject.toJSONString(content));
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("交易失败，系统错误");
            bankResult.setParam(e.getMessage());
        }
        return bankResult;


    }

    /**
     * 成功交易结果通知
     * @param yaColPayToBankObject
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/payMoneyNotifyurl")
    @ResponseBody
    public String payMoneyNotifyurl(@RequestBody YaColPayToBankObject yaColPayToBankObject) throws UnsupportedEncodingException {
        logger.info("======雅酷代付异步回调====："+JSONObject.toJSONString(yaColPayToBankObject));
        if (!(null == yaColPayToBankObject)){
            logger.info("======雅酷代付异步回调成功====");
            return "success";
        }
        TreeMap<String,Object> map = new TreeMap<>();
        map.put("notify_type",yaColPayToBankObject.getNotify_type());
        map.put("notify_id",yaColPayToBankObject.getNotify_id());
        map.put("_input_charset",yaColPayToBankObject.get_input_charset());
        map.put("notify_time",yaColPayToBankObject.getNotify_time());
        map.put("sign",yaColPayToBankObject.getSign());
        map.put("sign_type",yaColPayToBankObject.getSign_type());
        map.put("version",yaColPayToBankObject.getVersion());
        map.put("outer_trade_no",yaColPayToBankObject.getOuter_trade_no());
        map.put("inner_trade_no",yaColPayToBankObject.getInner_trade_no());
        map.put("withdraw_status",yaColPayToBankObject.getWithdraw_status());
        map.put("withdraw_amount",yaColPayToBankObject.getWithdraw_amount());
        String md5Sign=  AlinPayUtils.getMd5Sign(map);
        if (StringUtils.equals(md5Sign,yaColPayToBankObject.getSign())) {
            /**
             支付结果处理
             */
            BankResult bankResult = new BankResult();
            bankResult.setOrderId(Long.valueOf(yaColPayToBankObject.getOuter_trade_no()));// 商户订单号
            bankResult.setBankOrderId(yaColPayToBankObject.getInner_trade_no());// 第三方app交易号(商户交易流水号)
            bankResult.setParam(JsonUtils.objectToJson(yaColPayToBankObject));
            Date date = DateUtils.dateFormat(dateFormat, yaColPayToBankObject.getNotify_time());
            bankResult.setBankTime(date == null ? new Date() : date);
            if ("SUCCESS".equals(yaColPayToBankObject.getWithdraw_status())) {// 支付成功
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("付款成功");
                bankResult.setParam(JsonUtils.objectToJson(yaColPayToBankObject));
            } else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                try {
                    bankResult.setBankCode(BankResultInfoCode.CommunicateCodeTwo.valueOf("A" + yaColPayToBankObject.getWithdraw_status()).getStatusMsg());
                } catch (Exception e) {
                    logger.info("YacolPay未定义的错误信息,错误编码为：" + yaColPayToBankObject.getWithdraw_status());
                    bankResult.setBankCode("error.5000");
                }
                bankResult.setBankResult("付款失败：" + yaColPayToBankObject.getWithdraw_status());
                bankResult.setParam(JsonUtils.objectToJson(yaColPayToBankObject));
            }
            String msg = CallServiceUtil.sendPost(paymentInfo.getBankNotifyUrl(), JsonUtils.objectToJson(bankResult));
            Result paymentResult = JsonUtils.jsonToPojo(msg, Result.class);
            if (paymentResult == null) {
                return "FAIL";
            }
            if (paymentResult.getCode() == Result.SUCCESS) {
                return "";
            } else {
                return paymentResult.getMsg();
            }
        }
            else{
                logger.info("非法请求：签名信息验证失败");
                return "签名信息验证失败";
        }

        }


    /**
     * 雅酷单笔代付查询
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/repayQuery")
    @ResponseBody
    public BankResult repayQuery(@RequestBody SquareTrade trade)  {
        ChannelInfo channelInfo = trade.getChannelInfo();
        BankResult bankResult = new BankResult();
        TransOrder transOrder = trade.getTransOrder();
        JSONObject others = JSONObject.parseObject(channelInfo.getOthers());
        String signKey = others.getString("privateKey").trim();
        String publicCheckKey =others.getString("publicCheckKey".trim());

        //基本参数
        String service = "mimer_query_single_pay2bank";//接口
        String version = "1.0";//接口版本
        String request_time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");//请求时间
        String  partner_id = others.getString("partner_id"); //合作者Id
        String _input_charset = "UTF-8";//字符编码集
        String sign_type="RSA";//签名类型
        String notify_url =others.getString("notify_url"); ;

        //请求参数
        String out_trade_no = transOrder.getTransId();//商户代付的原订单号

        Map postData = new TreeMap();
        postData.put("service",service);
        postData.put("version",version);
        postData.put("request_time", request_time);
        postData.put("partner_id",partner_id);
        postData.put("_input_charset",_input_charset);
        postData.put("notify_url",notify_url);
        postData.put("out_trade_no",out_trade_no);  //商户订单号

        String contents = YaColIPayTools.createLinkString(postData,false);
        logger.info("雅酷代付查询申请签名前postDatas:"+postData.toString());
        //签名
        try {
            postData.put("sign", YaColPayRSAUtil.sign(contents,signKey,"utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        postData.put("sign_type",sign_type);
        String postDatas = YaColIPayTools.createLinkString(postData, true);
        logger.info("雅酷代付查询申请postDatas:"+postDatas);
        try {
            String result =URLDecoder.decode(
                    CallServiceUtil.sendPost(others.getString("masRequestUrl"), postDatas),"UTF-8");
            logger.info("雅酷代付查询申请result:"+result);
            Map<String, String> content = JSON.parseObject(result,Map.class);
            String sign_result = content.get("sign");
            String sign_type_result = content.get("sign_type");
            String _input_charset_result = content.get("_input_charset")
                    .toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");
            String like_result = YaColIPayTools.createLinkString(content, false);
            if (YaColIPaySignUtil.Check_sign(like_result, sign_result,
                    sign_type_result, publicCheckKey, _input_charset_result)) {
                String responseCode = content.get("response_code");
                if (responseCode.equals("APPLY_SUCCESS")){
                    String withdrawStatus = content.get("withdraw_status");
                    switch (withdrawStatus){
                        case "SUCCESS" :
                            bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                            bankResult.setBankResult("代付成功");
                            bankResult.setBankOrderId(content.get("out_trade_no"));
                            bankResult.setParam(JSONObject.toJSONString(content));
                            break;
                        case "FAILED" :
                            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                            bankResult.setBankResult("代付失败");
                            bankResult.setBankOrderId(content.get("out_trade_no"));
                            bankResult.setParam(JSONObject.toJSONString(content));
                            break;
                        case "PROCESSING" :
                            bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                            bankResult.setBankResult("代付处理中");
                            bankResult.setBankOrderId(content.get("out_trade_no"));
                            bankResult.setParam(JSONObject.toJSONString(content));
                            break;
                        case "RETURNT_TICKET" :
                            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                            bankResult.setBankResult("银行退票");
                            bankResult.setBankOrderId(content.get("out_trade_no"));
                            bankResult.setParam(JSONObject.toJSONString(content));
                            break;
                        default:
                            bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                            bankResult.setBankResult("代付结果未知"+content.get("response_message"));
                            bankResult.setParam(JSONObject.toJSONString(content));
                            break;
                    }
                }else {
                    bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                    bankResult.setBankResult("代付失败:"+content.get("response_message"));
                    bankResult.setParam(JSONObject.toJSONString(content));
                }
            } else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                bankResult.setBankResult("代付查询失败,验签不通过");
                bankResult.setParam(JSONObject.toJSONString(content));
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
            bankResult.setBankResult("代付查询失败，系统错误");
            bankResult.setParam(e.getMessage());
        }
        return bankResult;
    }
}
