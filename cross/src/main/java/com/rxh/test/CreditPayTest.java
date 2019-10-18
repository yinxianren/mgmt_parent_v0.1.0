package com.rxh.test;

import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

public class CreditPayTest {

    public void BondCard(){
        try {

            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("bizType","6");
            map.put("merId","M201002");
            map.put("merOrderId","20190731800111");
            map.put("userName","谭大东");
            map.put("identityType","1");
            map.put("bankCardType","1");
            map.put("identityNum","350525199101131577");
            map.put("bankCardNum","6227001935166667777");
            map.put("bankCardPhone","13959592888");
            map.put("bankCode","CCB");
            map.put("validDate","202011");
            map.put("securityCode","123");
            map.put("terminalMerId","M201002-008");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"lXN7z498"));
            String tradeInfo = JsonUtils.objectToJson(map);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8081/nvkuaijie/bondCard", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

        public void pay() {
            try {
                Map<String, Object> map = new TreeMap<>();
                map.put("charset","utf-8");
                map.put("signType","MD5");
                map.put("merId","M201002");
                map.put("merOrderId","20190801189907");
                map.put("currency","CNY");
                map.put("amount","10");
                map.put("identityType","1");
                map.put("identityNum","350525199101131577");
                map.put("payFee","5");
                map.put("bankCardNum","6227001935166667777");
                map.put("terminalMerId","M201002-008");
                map.put("terminalMerName","test");
                map.put("returnUrl","http://...");
                map.put("noticeUrl","http://...");
                map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"lXN7z498"));
                String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
                tradeInfo = URLEncoder.encode(tradeInfo, "UTF-8");
                String postJson = new String(Base64.getEncoder().encode(tradeInfo.getBytes()), "UTF-8");
                String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8081/nvkuaijie/pay", postJson);
                System.out.println(content);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        public static void main(String[] args) {
        CreditPayTest pt=new CreditPayTest();
        pt.daifu();


    }

    public void daifu(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201002");
            map.put("merOrderId","20190731333320");
            map.put("originalMerOrderId","201908011899289");
            map.put("currency","CNY");
            map.put("amount","9");
            map.put("identityType","1");
            map.put("bankCardType","1");
            map.put("identityNum","350525199101131577");
            map.put("bankCardNum","6227001935166667777");
            map.put("bankCardPhone","13959592888");
            map.put("bankCode","CCB");
            map.put("backFee","0.5");
            map.put("terminalMerId","M201002-008");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://192.168.1.67:8040/creditPay/creditRepayNotifyUrl");
            map.put("noticeUrl","http://192.168.1.67:8040/creditPay/creditRepayNotifyUrl");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"lXN7z498"));
            String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8082/nonVerifyKuaijie/inwardForAnother", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void balance(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201002");
            map.put("merOrderId","20190731800111");
            map.put("bankCardNum","6227001935166667777");
            map.put("terminalMerId","M201002-008");
            map.put("terminalMerName","test");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"lXN7z498"));
            String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8081/nvkuaijie/queryBalance", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

}

