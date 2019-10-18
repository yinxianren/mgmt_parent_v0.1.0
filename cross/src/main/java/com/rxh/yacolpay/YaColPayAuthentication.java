package com.rxh.yacolpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantBankCardBinding;
import com.rxh.pojo.merchant.MerchantBasicInformationRegistration;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.ExtraChannelInfo;
import com.rxh.square.pojo.MerchantCard;
import com.rxh.square.pojo.MerchantRegisterCollect;
import com.rxh.utils.HttpClientUtils;
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
import java.net.URLEncoder;
import java.util.*;



@RestController
@RequestMapping("/min_mer")
public class YaColPayAuthentication {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(YaColPayAuthentication.class);

    @PostMapping("/register_merchant")
    public BankResult validate(@RequestBody SquareTrade squareTrade) throws Exception {
        BankResult bankResult = new BankResult();
        ExtraChannelInfo exChannelInfo = squareTrade.getExtraChannelInfo();
        JSONObject others = JSONObject.parseObject(exChannelInfo.getData());
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
                bankResult.setBankResult("进件成功");
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
            }else {
                bankResult.setBankResult("进件失败："+responseMessage);
                bankResult.setStatus(SystemConstant.BANK_STATUS_UNPAID);
            }
            bankResult.setBankCode(responseCode);
            bankResult.setBankData(content.get("mimer_member_id").toString());
            bankResult.setParam(JSONObject.toJSONString(content));
        }else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("进件失败,验签不通过");
            bankResult.setParam(JSONObject.toJSONString(content));
        }
        return bankResult;
    }

    @PostMapping("/bank_card")
    public BankResult validateBankCard(@RequestBody SquareTrade squareTrade) throws Exception {
        BankResult bankResult = new BankResult();
        ExtraChannelInfo exChannelInfo = squareTrade.getExtraChannelInfo();
        JSONObject others = JSONObject.parseObject(exChannelInfo.getData());
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
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
            }else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_UNPAID);
            }
            bankResult.setBankData(content.get("mimer_member_id").toString());
            bankResult.setBankCode(responseCode);
            bankResult.setBankResult(responseMessage);
            bankResult.setParam(JSONObject.toJSONString(content));
        }else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("交易失败,验签不通过");
            bankResult.setParam(JSONObject.toJSONString(content));
            bankResult.setBankData(content.get("mimer_member_id").toString());
        }
        return bankResult;
    }


    @PostMapping("/bank_card_advance")
    public BankResult validateBankCardAdvance(@RequestBody SquareTrade squareTrade){
        Map<String,Object> map = getValidateBankCardAdvance(squareTrade);
        return null;
    }


    private Map<String,String> getValidate(SquareTrade squareTrade) throws Exception {
//        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        ExtraChannelInfo extraChannelInfo = squareTrade.getExtraChannelInfo();
        JSONObject others = JSONObject.parseObject(extraChannelInfo.getData());
        String publicKey = others.getString("publicKey");
        String privateKey = others.getString("privateKey");
        MerchantRegisterCollect merchantRegisterCollect = squareTrade.getMerchantRegisterCollect();
        MerchantRegisterInfo merchantRegisterInfo = squareTrade.getMerchantRegisterInfo();
        String merName = merchantRegisterInfo.getUserName();
        String merCert = merchantRegisterInfo.getIdentityNum();
        String merPhone = merchantRegisterCollect.getBankCardPhone();
        String mimer_cert_pic1 = squareTrade.getTradeObjectSquare().getMimerCertPic1();
        String mimer_cert_pic2 = squareTrade.getTradeObjectSquare().getMimerCertPic2();
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
        params.put("identity_id", squareTrade.getMerchantRegisterCollect().getTerminalMerId());
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




    private Map<String,String> getValidateBankCard(SquareTrade squareTrade) throws Exception {
        MerchantRegisterCollect merchantRegisterCollect = squareTrade.getMerchantRegisterCollect();
        JSONObject bankDate = JSON.parseObject(merchantRegisterCollect.getResult());
//        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        ExtraChannelInfo extraChannelInfo= squareTrade.getExtraChannelInfo();
        JSONObject others = JSONObject.parseObject(extraChannelInfo.getData());
        String publicKey = others.getString("publicKey");
        String privateKey = others.getString("privateKey");
        byte[] settle_bank_card_byte = null;
        byte[] mimer_bind_phone_byte = null;
        try {
            settle_bank_card_byte = YaColPayRSAUtil.encryptByPublicKey(merchantRegisterCollect.getCardNum().getBytes("utf-8"), publicKey);
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



    @PostMapping("/test_min")
    public BankResult test() throws Exception {
        YaColPayAuthentication yaColPayAuthentication = new YaColPayAuthentication();
        SquareTrade squareTrade = new SquareTrade();
        MerchantBasicInformationRegistration merchantBasicInformationRegistration = new MerchantBasicInformationRegistration();
        ChannelInfo channelInfo = new ChannelInfo();
        merchantBasicInformationRegistration.setMerOrderId(UUID.createId());
        merchantBasicInformationRegistration.setMerId("444");
        merchantBasicInformationRegistration.setCardHolderName("test");
        merchantBasicInformationRegistration.setIdentityNum("464654199312286469");
        merchantBasicInformationRegistration.setPhone("15637262525");
        Map<String,String> map = new HashMap<>();
        map.put("partnerId","200006503094");
        map.put("publicCheckKey","MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoRYRUfQsGc7drqfg2K1C3gWQLLiFjYyB1clTmdandjpjh4wiJo9dfIDgGsNAR+Mx4EqL4plqJD1mLnWkTu9KmI1ud1FQ2xvepL1XnuxRa01WiPA7YM5WsNiUqXIZItdFFzpnE6r2hpct9bmwvOa/4Pjlg26REfSZEtA21cjPFxxI06pBr4Y8isP2aoHVBLBWy7yJ79OQPchMBfXPOF5rbAMzkqViNZp9tw2RJ9erwZwOuUC+nGDGaH2D9Sr9S3H3HLLV0ibJ7gq8ht5D/BXA/z1uRdCScruEtLwbmzevoP5FgUKUyXgj9dR8xjjG9ljrKdZxwW4/De29Zp+02roiIwIDAQAB");
        map.put("privateKey","MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCiBgy+gpS7C1t8La2470sxb3E9XiE+Z9MVRbgOwCjxFu3lY/AQfNA7WXhsdijA5+v3rGChQt311jkjREaXUsqNAk+PxtF8F5HcpSqx96H9bI2MEu4SttPDoxBQVgMYnDaRTfkvpxj7mZlvrDnMYIBjn5qsJP5E6dgN+HnV/UVq7FxWwJ6rQA50fyezGez0ZRvKcSxe8ANFzbcL+BzPbua/hEVyrjG9nXdtvMHQpIh60xUCLwj0ZaInIhOeDe78O/c0q17VHjBWclabc556Y8sF44ldvnD/STdRrUwKEDyRAoWb9N/M7XPIpHNl0kBuA0jXLJKVAQE6S0JfgcTiKkeLAgMBAAECggEAURHl3o1IDi8on4HbouVZIms4phQrXiZlIAe6iObtlXR7pIPU4usQ5iFmeB7HVX62Oz8tOoNSvGdsP5EyIRVz9Apr9OzudMD2YwjhzBq0GzHtwWDXbtW8L++vggMHmZDQXPQ+8vERNxMsCwyJ/xFqLG733ZrE/4ZibNsfW0tXKKA/vsXt2LWArIKiD0msFEJCPgL100pQkvYIpoJZ6sHnaOJofqACTbdCj2iokSsprlGB0FvdtUzMSoIpKpYo3284ZtIzkwMVjRbRxJfpEFfyXXrJrOAE3tcA/d/wOZVIwb4mZ/l3kvcgzWah7Qdsc/OjTryfbNIpKSRr+V4PtyC3AQKBgQDThqPWGuelFYs5cIwWTH/cT/CdMpfNYTfdPr12/SP9URhf1/yS00G0r2uugsdEsjA9BJcqZbmgmKx1LUIhDn/7j7um7dPq4KFctMejBJgCeBD6AYMyVGoWoieKBSVPO1dBWnHUecFIv3mtFrRHkDhzEmS1wDd8f+RWQhETGJ3O2wKBgQDEFvQm8V+FRbINeNwxOwz7BsvZYe3iGb6q7O6zNZCoBRwB01X+PQkhry16BvMZ/YKNx8aZqUICP7d6vf6teKHA4962TSnaaYqJZPDzNtWRTVRYtybPAhcGiqrO33buZ2ogLAdTbMn16hG8WalLx37VgBTiEOsSeTf6ryFOgckREQKBgB1Myyj/NRMi9tQQCPeVxShJUnUT6v8h9lEJPclbqz6Nmyi7jFryNGnI2sujheK4JAJvvli7GolqXIkmqcBWd9fqwv2OeApS70ceK4EjQ8MjyoY262tvUfqsn3l42QAuohmFY7sg2msvSrV1Lae0DH20EIs0gvsV5BUmtaLFiCZ9AoGAYF7rlWwRRv2O2WIpzaQ45/JaIzcm43VFqNmTIs5TjtAcCKWl4LJ8l2pxzkQ2G/Lkw+uIJqLxxwsrkI5p6TWdQaB8J1pbFHXEWWwbo1yyr5uytXsl/p0HVfa2pb9bwyVeGfupig2wYESufMQQGSctpZ4yJTytW0HqCjEiDGRqvhECgYEAjYT2NQ64GlMeh1k6ahnBgW0TUux1o3uFSk5mTLLmveVOfimwlJyc8WGvB4EEa8PX74/RqqNrTbXhMYHU5UIUh3c+UGOCVfWSe59f+FUvG3bXZPI4Fc13VtzbmipO5taYu+Lb5TCZIyv6v2fDxKrkLO5GwCi+33F6sdHDnU0mErU=");
        map.put("publicKey","MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1lStik4jwa6ZHSguzNawAoCY6st4CR2XwNExXbSje/p9aMQ4cbj/zHV1z3HeNqagdewtNWNHXR8wDf3OnskIeIZwLzAKZy+H5B6qL9mJ7mkGM8hC3OsY3LhJAcmpsOOebRtTKq8/9O2NiwrSjXFbKRBdpOqMi/FIvouIJSERE0eUHo0HpT9ncMnzVHxxzo4Fw/c2YNwQpvhqhaz23dUJ3ZTRz6a4B9ROM9zvXDp1kb+G5QU6ITuRlXEho+wVfiHc/qO2+CAJ4CTdS0O2u/P8BQX/rQlXiEICtQmeZt7lVguMGmkSQrswnyQB9Bkqu6e/L9YQP3y+tzIaYtjGE+AE9wIDAQAB");
        channelInfo.setOthers(JSONObject.toJSONString(map));
        squareTrade.setMerchantBasicInformationRegistration(merchantBasicInformationRegistration);
        squareTrade.setChannelInfo(channelInfo);
        return  yaColPayAuthentication.validate(squareTrade);
    }
}


