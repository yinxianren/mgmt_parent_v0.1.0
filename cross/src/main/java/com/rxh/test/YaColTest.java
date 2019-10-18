package com.rxh.test;

import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

public class YaColTest {

    public void addCus(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201003");
            map.put("merOrderId","20190823004");
            map.put("merchantType","01");
            map.put("terminalMerId","M201001111");
            map.put("terminalMerName","钱川");
            map.put("userShortName","钱川");
            map.put("identityType","1");
            map.put("identityNum","500234199211038433");
            map.put("phone","13599543918");
            map.put("province","113");
            map.put("city","3930");
            map.put("address","厦门湖里");
            map.put("bankCode","CIB");
            map.put("bankCardType","1");
            map.put("category","5499");
            map.put("cardHolderName","钱川");
            map.put("bankCardNum","6225751110150467");
            map.put("bankCardPhone","13599543918");
            map.put("payFee","2");
            map.put("backFee","1.5");
            map.put("validDate","0821");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/addCusInfo", postJson);
//            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://pay.xmfusepay.com/kuaijie/addCusInfo", postJson);

            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void bankCardBind(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201003");
            map.put("merOrderId","20190823004");
            map.put("bankaccProp","0");
            map.put("identityType","1");
            map.put("identityNum","500234199211038433");
            map.put("bankCardType","1");
            map.put("cardHolderName","钱川");
            map.put("bankCardNum","6225751110150467");
            map.put("bankCardPhone","13599543918");
            map.put("province","福建");
            map.put("city","厦门");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));

            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/bankCardBind", postJson);
//            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://pay.xmfusepay.com/kuaijie/bankCardBind", postJson);

            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void serviceFul(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201003");
            map.put("merOrderId","20190823004");
            map.put("mimerCertPic1","kjskj/545");
            map.put("mimerCertPic2","lsdjf/56464");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));

            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/serviceFulfillment", postJson);
//            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://pay.xmfusepay.com/kuaijie/serviceFulfillment", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void BondCard(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201003");
            map.put("merOrderId","201908161150002");
            map.put("userName","钱川");
            map.put("identityType","1");
            map.put("bankCardType","2");
            map.put("identityNum","500234199211038433");
            map.put("bankCardNum","6225751110150467");
            map.put("bankCardPhone","13599543918");
            map.put("bankCode","CMB");
            map.put("validDate","0821");
            map.put("securityCode","893");
            map.put("terminalMerId","M201001111");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
         String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/bondCard", postJson);
//            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://pay.xmfusepay.com/kuaijie/bondCard", postJson);
            System.out.println(content);


//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void confirmBond(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201003");
            map.put("merOrderId","201908161150002");
            map.put("identityType","1");
            map.put("bankCardType","2");
            map.put("userName","钱川");
            map.put("identityNum","500234199211038433");
            map.put("bankCardNum","6225751110150467");
            map.put("bankCode","CMB");
            map.put("bankCardPhone","13599543918");
            map.put("validDate","0821");
            map.put("securityCode","893");
            map.put("payFee","2");
            map.put("backFee","1.5");
            map.put("backCardNum","6225751110150467");
            map.put("backBankCode","CCB");
            map.put("backCardPhone","13599543918");
            map.put("smsCode","859585");
            map.put("terminalMerId","M201001111");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/confirmBond", postJson);
//            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://pay.xmfusepay.com/kuaijie/confirmBond", postJson);

            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void BondCardSms(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201007");
            map.put("merOrderId","2019081416370005");
            map.put("userName","钱川");
            map.put("identityType","1");
            map.put("bankCardType","1");
            map.put("identityNum","500234199211038433");
            map.put("bankCardNum","6227001935600377518");
            map.put("bankCardPhone","13599543918");
            map.put("bankCode","CCB");
            map.put("validDate","202011");
            map.put("securityCode","123");
            map.put("terminalMerId","M201007001");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"Eu4j3Zq8"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/reGetBondCode", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void payApply(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("bizType","5");
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201003");
            map.put("merOrderId","201908161150021");
            map.put("currency","CNY");
            map.put("amount","1");
            map.put("identityType","1");
            map.put("bankCardType","2");
            map.put("identityNum","500234199211038433");
            map.put("bankCardNum","6225751110150467");
            map.put("bankCode","CMB");
            map.put("validDate","0821");
            map.put("securityCode","893");
            map.put("bankCardPhone","13599543918");
            map.put("payFee","2");
            map.put("terminalMerId","M201001111");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("deviceType","1");
            map.put("deviceId","fd3123");
            map.put("macAddress","fa:s21:fef");

            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/pay/payApply", postJson);
            //   String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.109:8085/nvkuaijie/pay", postJson);

            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void confirmPay(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("bizType","5");
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201003");
            map.put("merOrderId","201908161150021");
            map.put("smsCode","075185");
            map.put("terminalMerId","M201001111");
            map.put("terminalMerName","test");
            map.put("payFee","5");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));
            String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/pay/confirmPay", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void confirmPaySms(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("bizType","5");
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201003");
            map.put("merOrderId","201908161150021");
            map.put("terminalMerId","M201001111");
            map.put("terminalMerName","test");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));
            String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/pay/confirmSMS", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void daifu(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("bizType","5");
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201003");
            map.put("merOrderId","201907291550041");
            map.put("originalMerOrderId","201908161150021");
            map.put("currency","CNY");
            map.put("amount","1");
            map.put("identityType","1");
            map.put("bankCardType","2");
            map.put("identityNum","500234199211038433");
            map.put("bankCardNum","6225751110150467");
            map.put("bankCardPhone","13599543918");
            map.put("bankCode","CMB");
            map.put("backFee","0.5");
            map.put("terminalMerId","M201001111");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));
            String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8050/kuaijie/getTransOrder", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public static void main(String[] args) {
        YaColTest test = new YaColTest();
        test.daifu();
    }
}
