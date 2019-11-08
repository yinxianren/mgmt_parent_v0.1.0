package com.rxh.yacolpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.pojo.merchant.MerchantBasicInformationRegistration;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.ExtraChannelInfo;
import com.rxh.square.pojo.MerchantRegisterCollect;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import com.rxh.yacolpay.utils.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.*;



@RestController
@RequestMapping("/min_mer")
public class YaColPayAuthentication {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(YaColPayAuthentication.class);

    @PostMapping("/register_merchant")
    public CrossResponseMsgDTO validate(@RequestBody RequestCrossMsgDTO squareTrade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        ChannelExtraInfoTable exChannelInfo = squareTrade.getChannelExtraInfoTable();
        JSONObject others = JSONObject.parseObject(exChannelInfo.getChannelParam());
        String publicCheckKey = others.getString("publicCheckKey");
        Map<String, String> map = getValidate(squareTrade);
        String params = YaColIPayTools.createLinkString(map, true);
        logger.info("雅酷支付小微商户注册请求参数: "+params);
        String result = URLDecoder.decode(CallServiceUtil.sendPost(others.getString("url"),params), "UTF-8");
//        String result = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "http://test.gate.yacolpay.com/mgs/gateway.do", new TreeMap<>(map));
        logger.info("雅酷支付小微商户注册返回参数: "+result);
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
            String responseMessage = content.get("response_message").toString();
            if (responseCode.equals("APPLY_SUCCESS")){
                bankResult.setChannelResponseMsg("进件成功");
                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
            }else {
                bankResult.setChannelResponseMsg("进件失败："+responseMessage);
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            }
            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setChannelResponseMsg("进件失败,验签不通过");
            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
        }
        return bankResult;
    }

    @PostMapping("/bank_card")
    public CrossResponseMsgDTO validateBankCard(@RequestBody RequestCrossMsgDTO squareTrade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        ChannelExtraInfoTable exChannelInfo = squareTrade.getChannelExtraInfoTable();
        JSONObject others = JSONObject.parseObject(exChannelInfo.getChannelParam());
        String publicCheckKey = others.getString("publicCheckKey");
        Map<String, String> map = getValidateBankCard(squareTrade);
        logger.info("雅酷支付小微商户绑卡请求参数: "+map);
        String params = YaColIPayTools.createLinkString(map, true);
        logger.info("雅酷支付小微商户绑卡请求参数: "+params);
        String result = URLDecoder.decode(CallServiceUtil.sendPost(others.getString("url"), params), "UTF-8");
//        String result = URLDecoder.decode(HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(),"http://test.gate.yacolpay.com/mgs/gateway.do", new TreeMap<>(map)),"UTF-8");
        logger.info("雅酷支付小微商户绑卡返回参数: "+ result);
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
            String responseMessage = content.get("response_message").toString();
            if (responseCode.equals("APPLY_SUCCESS")){
                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
            }else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            }
            bankResult.setChannelResponseMsg(responseMessage);
            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setChannelResponseMsg("交易失败,验签不通过");
            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
        }
        return bankResult;
    }

    private Map<String,String> getValidate(RequestCrossMsgDTO squareTrade) throws Exception {
//        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        ChannelExtraInfoTable extraChannelInfo = squareTrade.getChannelExtraInfoTable();
        JSONObject others = JSONObject.parseObject(extraChannelInfo.getChannelParam());
        String publicKey = others.getString("publicKey");
        String privateKey = others.getString("privateKey");
        RegisterCollectTable merchantRegisterCollect = squareTrade.getRegisterCollectTable();
        RegisterInfoTable merchantRegisterInfo = squareTrade.getRegisterInfoTable();
        String merName = merchantRegisterInfo.getUserName();
        String merCert = merchantRegisterInfo.getIdentityNum();
        String merPhone = merchantRegisterCollect.getBankCardPhone();
        String mimer_cert_pic1 = new String(merchantRegisterCollect.getMiMerCertPic1());
        String mimer_cert_pic2 = new String(merchantRegisterCollect.getMiMerCertPic2());
        byte[] mimer_name_byte = null;
        byte[] mimer_cert_no_byte = null;
        byte[] mimer_phone_byte = null;
        try {
            mimer_name_byte = YaColPayRSAUtil.encryptByPublicKey(merName.getBytes("utf-8"), publicKey);
            mimer_cert_no_byte = YaColPayRSAUtil.encryptByPublicKey(merCert.getBytes("utf-8"), publicKey);
            mimer_phone_byte = YaColPayRSAUtil.encryptByPublicKey(merPhone.getBytes("utf-8"), publicKey);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String mimer_name_encrypt = YaColIPayBase64.encode(mimer_name_byte);
        String mimer_cert_no_encrypt = YaColIPayBase64.encode(mimer_cert_no_byte);
        String mimer_phone_encrypt = YaColIPayBase64.encode(mimer_phone_byte);

        Map<String,String> params = new HashMap<>();
        params.put("service", "mimer_register_merchant");
        params.put("version", "1.0");
        params.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        params.put("partner_id", others.getString("partner_id"));
        params.put("_input_charset","UTF-8");

        params.put("request_no", merchantRegisterCollect.getMerOrderId());
        params.put("identity_id", merchantRegisterCollect.getTerminalMerId());
        params.put("mimer_name",mimer_name_encrypt);
        params.put("mimer_cert_no",mimer_cert_no_encrypt);
        params.put("mimer_phone", mimer_phone_encrypt);
        params.put("mimer_cert_pic1", mimer_cert_pic1);
        params.put("mimer_cert_pic2", mimer_cert_pic2);

        String content = YaColIPayTools.createLinkString(params, false);//排序后的待签名字符串
//        params.put("sign", YacolPayUtil.getSign(params,privateKey));
        String sign = YaColIPaySignUtil.sign(content, "RSA", privateKey,
                "UTF-8");
//        sign =  URLEncoder.encode(sign,"UTF-8");
        params.put("sign",sign);
        params.put("sign_type","RSA");
        return params;
    }




    private Map<String,String> getValidateBankCard(RequestCrossMsgDTO squareTrade) throws Exception {
        RegisterCollectTable merchantRegisterCollect = squareTrade.getRegisterCollectTable();
        JSONObject bankDate = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
//        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        ChannelExtraInfoTable extraChannelInfo= squareTrade.getChannelExtraInfoTable();
        JSONObject others = JSONObject.parseObject(extraChannelInfo.getChannelParam());
        String publicKey = others.getString("publicKey");
        String privateKey = others.getString("privateKey");
        byte[] settle_bank_card_byte = null;
        byte[] mimer_bind_phone_byte = null;
        try {
            settle_bank_card_byte = YaColPayRSAUtil.encryptByPublicKey(merchantRegisterCollect.getBankCardNum().getBytes("utf-8"), publicKey);
            mimer_bind_phone_byte = YaColPayRSAUtil.encryptByPublicKey(merchantRegisterCollect.getBankCardPhone().getBytes("utf-8"), publicKey);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String settle_bank_card_encrypt = YaColIPayBase64.encode(settle_bank_card_byte);
        String mimer_bind_phone_encrypt = YaColIPayBase64.encode(mimer_bind_phone_byte);
        Map<String,String> params = new HashMap<>();
        params.put("service", "mimer_bind_fundout_card");
        params.put("version", "1.0");
        params.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        params.put("partner_id", others.getString("partner_id"));
        params.put("_input_charset", "UTF-8");

        params.put("request_no", merchantRegisterCollect.getMerOrderId());
        params.put("mimer_member_id",bankDate.getString("bankData"));
        params.put("settle_bank_card",settle_bank_card_encrypt);
        params.put("mimer_bind_phone", mimer_bind_phone_encrypt);
        params.put("is_real_time", "1");
        params.put("bank_code", merchantRegisterCollect.getBankCode());
        String content = YaColIPayTools.createLinkString(params, false);//排序后的待签名字符串
        String sign = YaColIPaySignUtil.sign(content, "RSA", privateKey,
                "UTF-8");
        params.put("sign",sign);
        params.put("sign_type","RSA");
        return params;
    }




    private Map<String,Object> getValidateBankCardAdvance(SquareTrade squareTrade){
        Map<String,Object> treeMap = new TreeMap<>();
        treeMap.put("service", "validate_bank_card_advance");
        treeMap.put("version", "PAS_1.0");
        treeMap.put("request_time",DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        treeMap.put("partner_id", "");
        treeMap.put("_input_charset", "UTF-8");
        treeMap.put("sign_type","RSA");
        treeMap.put("sign","");

        treeMap.put("ticket", "");
        treeMap.put("valid_code", "");
        return null;
    }


    private Map<String,String> balanceQuery(SquareTrade squareTrade) throws Exception {
        MerchantRegisterCollect merchantRegisterCollect = squareTrade.getMerchantRegisterCollect();
        JSONObject bankDate = JSON.parseObject(merchantRegisterCollect.getResult());
//        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        ExtraChannelInfo extraChannelInfo= squareTrade.getExtraChannelInfo();
        JSONObject others = JSONObject.parseObject(extraChannelInfo.getData());
        String publicKey = others.getString("publicKey");
        String privateKey = others.getString("privateKey");

        Map<String,String> params = new HashMap<>();
        params.put("service", "mimer_bind_fundout_card");
        params.put("version", "1.0");
        params.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        params.put("partner_id", others.getString("partner_id"));
        params.put("_input_charset", "UTF-8");


        params.put("mimer_member_id","200006573563s");
        params.put("balance_type","1");
        String content = YaColIPayTools.createLinkString(params, false);//排序后的待签名字符串
        String sign = YaColIPaySignUtil.sign(content, "RSA", privateKey,
                "UTF-8");
        params.put("sign",sign);
        params.put("sign_type","RSA");
        return params;
    }
}


