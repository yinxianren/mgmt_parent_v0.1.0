package com.rxh.yacolpay;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantBankCardBinding;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.BatchRepayInfo;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.RepayInfo;
import com.rxh.utils.*;
import com.rxh.yacolpay.utils.CallServiceUtil;
import com.rxh.yacolpay.utils.YaColIPaySignUtil;
import com.rxh.yacolpay.utils.YaColIPayTools;
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
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * @author phoenix
 * @data 20190718
 * 雅酷批量代付
 */
@Controller
@RequestMapping("/yaColBatchPayToBank")
public class YaColBatchPayToBank {

    private static SimpleDateFormat dateFormat =new SimpleDateFormat("yyyyMMddHHmmss");
    private final  static Logger logger = LoggerFactory.getLogger(YaColBatchPayToBank.class);
    @Autowired
    private PaymentInfo paymentInfo;

    @RequestMapping("/batchPayMoney")
    @ResponseBody
    public BankResult  batchPayMoney(@RequestBody BatchRepayInfo batchRepayInfo) throws UnsupportedEncodingException{
        logger.info("===============================进入batchPayMoney=====================");
        ChannelInfo channelInfo = batchRepayInfo.getChannelInfo();
        JSONObject others = JSONObject.parseObject(channelInfo.getOthers());
        String publicKey =others.getString("publicKey").trim();
        String signKey = others.getString("privateKeyRSA").trim();
        String publicCheckKey =others.getString("publicCheckKey".trim());
        String masRequestUrl=others.getString("masRequestUrl").trim();

        BankResult bankResult = new BankResult();

        //基本参数
        String service = "create_batch_pay2bank";//接口
        String version = "1.0";//接口版本
        String request_time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");//请求时间
        String  partner_id = others.getString("partner_id"); //合作者Id
        String _input_charset = "UTF-8";//字符编码集
        String sign_type="RSA";//签名类型
        String notify_url =others.getString("notify_url");


        //请求参数
        String batch_no= batchRepayInfo.getBatchNo();
        String detail_list="" ;
        Boolean flag=false;
        List<RepayInfo> list = batchRepayInfo.getDetailList();
         for(RepayInfo repayInfo :list){
        detail_list=detail_list
                +(StringUtils.isBlank(repayInfo.getTransId())?"^^":repayInfo.getTransId()+"^")
                +(StringUtils.isBlank(repayInfo.getInAcctName())?"^^":repayInfo.getInAcctName()+"^")
                +(StringUtils.isBlank(repayInfo.getIdentityNum())?"^^":repayInfo.getIdentityNum()+"^")
                +(StringUtils.isBlank(repayInfo.getInAcctNo())?"^^":repayInfo.getInAcctNo()+"^")
                +(StringUtils.isBlank(repayInfo.getBankName())?"^^":repayInfo.getBankName()+"^")
                +(StringUtils.isBlank(repayInfo.getBankCode())?"^^":repayInfo.getBankCode()+"^")
                +(StringUtils.isBlank(repayInfo.getProvince())?"^^":repayInfo.getProvince()+"^")
                +(StringUtils.isBlank(repayInfo.getCity())?"^^":repayInfo.getCity()+"^")
                +(StringUtils.isBlank(repayInfo.getBankBranchName())?"^^":repayInfo.getBankBranchName()+"^")
                +(StringUtils.isBlank(String.valueOf(repayInfo.getAmount()))?"^^":String.valueOf(repayInfo.getAmount())+"^")
                +(StringUtils.isBlank(repayInfo.getBankAccProp())?"^^":repayInfo.getBankAccProp()+"^")
                +(StringUtils.isBlank(repayInfo.getBankCardType())?"^^":repayInfo.getBankCardType()+"^")
                +(StringUtils.isBlank(repayInfo.getRemark())?"^^":repayInfo.getRemark());
             flag=!flag;
             if (flag){
                 detail_list= detail_list+"|";
             }
         }
        detail_list =detail_list.replace("^^^","^^");
        detail_list=detail_list.replace("^^^^^","^^^^");
        logger.info("===============================detail_list=====================:"+detail_list);
        String detail_list1="";
        try {
            detail_list1= YaColIPayTools.create_detail_list_like(detail_list,publicKey);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Map datail_array= new TreeMap();
        datail_array.put("service",service);
        datail_array.put("version",version);
        datail_array.put("request_time",request_time);
        datail_array.put("partner_id",partner_id);
        datail_array.put("_input_charset",_input_charset);
        datail_array.put("notify_url",notify_url);
        datail_array.put("batch_no",batch_no);
        datail_array.put("detail_list", detail_list1);


        //签名
        String content1 = YaColIPayTools.createLinkString(datail_array,false);
        try {
            String sign = YaColIPaySignUtil.sign(content1, sign_type,signKey, _input_charset);
            datail_array.put("sign", sign);
            datail_array.put("sign_type", "RSA");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String   param = YaColIPayTools.createLinkString(datail_array,true);
        logger.info("===============================发送请求url=====================:"+masRequestUrl);
        logger.info("===============================发送请求参数=====================:"+param);


        try {
            String result = URLDecoder.decode(CallServiceUtil.sendPost(masRequestUrl, param), _input_charset);
            logger.info("===============================result=====================:"+result);
            Map<String, String> content = JSON.parseObject(result,Map.class);

            String sign_result = content.get("sign").toString();
            String sign_type_result = content.get("sign_type").toString();
            String _input_charset_result = content.get("_input_charset")
                    .toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");
            String like_result = YaColIPayTools.createLinkString(content,false);
            logger.info("===============================like_result=====================:"+like_result);

            if (YaColIPaySignUtil.Check_sign(like_result.toString(), sign_result,
                    sign_type_result, publicCheckKey, _input_charset_result)) {

                String responseCode = content.get("response_code").toString();
                logger.info("===============================雅酷批量代付申请返回===============================:"+responseCode);
                switch (responseCode){
                    case "APPLY_SUCCESS" :
                        String tradeStatus = content.get("batch_status").toString();
                        switch (tradeStatus){
                            case "APPLY_SUCCESS" :
                                bankResult.setBankResult("提交成功");
                                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                                bankResult.setParam(JSONObject.toJSONString(content));
                                break;
                            case "WAIT_PROCESS" :
                                bankResult.setBankResult("待处理");
                                bankResult.setStatus(SystemConstant.BANK_STATUS_UNPAID);
                                bankResult.setParam(JSONObject.toJSONString(content));
                                break;
                            case "PROCESSING" :
                                bankResult.setBankResult("处理中");
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setParam(JSONObject.toJSONString(content));
                                break;
                        }

                        bankResult.setBankOrderId(content.get("batch_no"));
                        bankResult.setParam(JSONObject.toJSONString(content));
                        break;
                    case "PARTNER_ID_NOT_EXIST" :
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("合作方Id不存在");
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
     * @param yaColBatchPayToBankNotifyObject
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/batchPayNotifyUrl")
    @ResponseBody
    public String payMoneyNotifyurl(@RequestBody String yaColBatchPayToBankNotifyObject) throws Exception {
        logger.info("===============================异步回调=====================:");
        logger.info("===============================异步回调值=====================:"+yaColBatchPayToBankNotifyObject);
        String publicCheckKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoRYRUfQsGc7drqfg2K1C3gWQLLiFjYyB1clTmdandjpjh4wiJo9dfIDgGsNAR+Mx4EqL4plqJD1mLnWkTu9KmI1ud1FQ2xvepL1XnuxRa01WiPA7YM5WsNiUqXIZItdFFzpnE6r2hpct9bmwvOa/4Pjlg26REfSZEtA21cjPFxxI06pBr4Y8isP2aoHVBLBWy7yJ79OQPchMBfXPOF5rbAMzkqViNZp9tw2RJ9erwZwOuUC+nGDGaH2D9Sr9S3H3HLLV0ibJ7gq8ht5D/BXA/z1uRdCScruEtLwbmzevoP5FgUKUyXgj9dR8xjjG9ljrKdZxwW4/De29Zp+02roiIwIDAQAB";
        String result =URLDecoder.decode(yaColBatchPayToBankNotifyObject,"UTF-8");
        Map map = new HashMap();
        Arrays.stream(result.split("&"))
                .map(str -> str.split("="))
                .map(arry->map.put(arry[0],arry[1]))
                .count();
//        Map map = JSONObject.parseObject(yaColBatchPayToBankNotifyObject.toString(),Map.class);

        String sign_result = map.get("sign").toString();
        String sign_type_result = map.get("sign_type").toString();
        String _input_charset_result = map.get("_input_charset").toString();
        map.remove("sign");
        map.remove("sign_type");
        map.remove("sign_version");
        String like_result = YaColIPayTools.createLinkString(map, false);
        logger.info("===============================异步回调结果=====================:"+like_result);
        if (YaColIPaySignUtil.Check_sign(like_result.toString(),sign_result,sign_type_result,publicCheckKey,_input_charset_result)) {
            /**
             支付结果处理
             */
            BankResult bankResult = new BankResult();
            bankResult.setOrderId(Long.valueOf(map.get("batch_no").toString()));// 商户订单号
            bankResult.setParam(JsonUtils.objectToJson(like_result));
            Date date = DateUtils.dateFormat(dateFormat, map.get("gmt_finished").toString());
            bankResult.setBankTime(date == null ? new Date() : date);
            if ("FINISHED".equals(map.get("batch_status"))) {// 支付成功
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("批次处理成功");
                bankResult.setParam(JsonUtils.objectToJson(like_result));
                return "  Status: "+bankResult.getStatus()+"  BankResult: "+bankResult.getBankResult()+"  Param: "+bankResult.getParam();
            } else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                logger.info("批量代付错误信息,错误编码为：" + map.get("batch_status"));
                bankResult.setBankCode("error.5000");
                bankResult.setBankResult("批量代付处理失败");
                bankResult.setParam(JsonUtils.objectToJson(like_result));
                return "FAIL";
            }
           /* String msg = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), paymentInfo.getBankNotifyUrl(), JsonUtils.objectToJson(bankResult));
            Result paymentResult = JsonUtils.jsonToPojo(msg, Result.class);
            if (paymentResult == null) {
                return "FAIL";
            }
            if (paymentResult.getCode() == Result.SUCCESS) {
                return "success";
            } else {
                return paymentResult.getMsg();
            }*/
        }else{
            logger.info("非法请求：签名信息验证失败");
            return "签名信息验证失败";
        }
    }
}
