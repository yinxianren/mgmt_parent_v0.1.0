package com.rxh.test;

import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.AlinTradeObject;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
public class PayTest {



    public void addCus(){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201010");
            map.put("merOrderId","201908213211351");
            map.put("merchantType","01");
            map.put("terminalMerId","M2010010011");
            map.put("terminalMerName","谭廷东");
            map.put("userShortName","谭廷东");
            map.put("identityType","1");
            map.put("identityNum","500240199101131111");
            map.put("phone","18649624575");
            map.put("province","113");
            map.put("city","3930");
            map.put("address","厦门湖里");
            map.put("bankCode","CIB");
            map.put("bankCardType","1");
            map.put("category","5499");
            map.put("cardHolderName","谭廷东");
            map.put("bankCardNum","6227001935140663111");
            map.put("bankCardPhone","18649624575");
            map.put("payFee","5");
            map.put("backFee","1.5");
//            map.put("validDate","0821");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"4b61M50P"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
//            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.109:8085/kuaijie/addCusInfo", postJson);
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/addCusInfo", postJson);

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
            map.put("merId","M201010");
            map.put("merOrderId","201908213211351");
            map.put("bankaccProp","0");
            map.put("identityType","1");
            map.put("identityNum","500240199101131111");
            map.put("bankCardType","1");
            map.put("cardHolderName","谭廷东");
            map.put("bankCardNum","6227001935140663111");
            map.put("bankCardPhone","18649624575");
            map.put("province","530000");
            map.put("city","530900");
            map.put("bankCode","CIB");
            map.put("terminalMerId","M2010010011");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"4b61M50P"));

            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
//            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.109:8085/kuaijie/bankCardBind", postJson);
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/bankCardBind", postJson);

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
            map.put("merId","M201010");
            map.put("merOrderId","201908213211351");
            map.put("mimerCertPic1","kjskj/545");
            map.put("mimerCertPic2","lsdjf/56464");
            map.put("terminalMerId","M2010010011");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"4b61M50P"));

            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/serviceFulfillment", postJson);
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
            map.put("merId","M201010");
            map.put("merOrderId","20190816115000302");
            map.put("userName","谭廷东");
            map.put("identityType","1");
            map.put("bankCardType","1");
            map.put("identityNum","500240199101131111");
            map.put("bankCardNum","6227001935140663112");
            map.put("bankCardPhone","18649624575");
            map.put("bankCode","CMB");
            map.put("validDate","0821");
            map.put("securityCode","893");
            map.put("terminalMerId","M2010010011");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"4b61M50P"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
//         String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.109:8085/kuaijie/bondCard", postJson);
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/bondCard", postJson);
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
            map.put("merId","M201010");
            map.put("merOrderId","20190816115000302");
            map.put("identityType","1");
            map.put("bankCardType","1");
            map.put("userName","谭廷东");
            map.put("identityNum","500240199101131111");
            map.put("bankCardNum","6227001935140663112");
            map.put("bankCode","CMB");
            map.put("bankCardPhone","18649624575");
            map.put("validDate","0821");
            map.put("securityCode","893");
            map.put("payFee","2");
            map.put("backFee","1.5");
            map.put("backCardNum","6227001935140663559");
            map.put("backBankCode","CMB");
            map.put("backCardPhone","18649624575");
            map.put("smsCode","111111");
            map.put("terminalMerId","M2010010011");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"4b61M50P"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
//            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.109:8085/kuaijie/bondCard", postJson);
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/confirmBond", postJson);

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
            map.put("merId","M201009");
            map.put("merOrderId","2019081611500026");
            map.put("userName","谭廷东");
            map.put("identityType","1");
            map.put("bankCardType","1");
            map.put("identityNum","500240199101131555");
            map.put("bankCardNum","6227001935140663111");
            map.put("bankCardPhone","18649624575");
            map.put("bankCode","CMB");
            map.put("validDate","202011");
            map.put("securityCode","123");
            map.put("terminalMerId","M201001001");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"vvK2424b"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8081/kuaijie/reGetBondCode", "JTdiJTBkJTBhKyslMjJwcm92aW5jZSUyMiUzYSslMjI0MzAwMDAlMjIlMmMlMGQlMGErKyUyMmNpdHklMjIlM2ErJTIyNDMwNzAwJTIyJTJjJTBkJTBhKyslMjJwcm9kdWN0Q2F0ZWdvcnklMjIlM2ErNTU0MiUyYyUwZCUwYSsrJTIybWVyT3JkZXJJZCUyMiUzYSslMjIyMDE5MDkxODE5MjQwNzEzMjU0MzAxNTEwMTY2MDI2MyUyMiUyYyUwZCUwYSsrJTIyY3VycmVuY3klMjIlM2ErJTIyQ05ZJTIyJTJjJTBkJTBhKyslMjJhbW91bnQlMjIlM2ErMTE1LjU1JTJjJTBkJTBhKyslMjJpZGVudGl0eVR5cGUlMjIlM2ErMSUyYyUwZCUwYSsrJTIyaWRlbnRpdHlOdW0lMjIlM2ErJTIyNDMwNzAzMTk5NTA4MjU5NTczJTIyJTJjJTBkJTBhKyslMjJiYW5rQ29kZSUyMiUzYSslMjJDTUIlMjIlMmMlMGQlMGErKyUyMmJhbmtDYXJkVHlwZSUyMiUzYSsyJTJjJTBkJTBhKyslMjJiYW5rQ2FyZE51bSUyMiUzYSslMjI2MjI1NzY3NzMxODEyNDc1JTIyJTJjJTBkJTBhKyslMjJiYW5rQ2FyZFBob25lJTIyJTNhKyUyMjE1NTc0OTI4NzU5JTIyJTJjJTBkJTBhKyslMjJ2YWxpZERhdGUlMjIlM2ErJTIyMjAwNiUyMiUyYyUwZCUwYSsrJTIyc2VjdXJpdHlDb2RlJTIyJTNhKyUyMjEyMyUyMiUyYyUwZCUwYSsrJTIycGF5RmVlJTIyJTNhKzAuNTUlMmMlMGQlMGErKyUyMnRlcm1pbmFsTWVySWQlMjIlM2ErJTIyZWY3NDE0YTc2ZTIzNDcxYWE1MzNmNmM1MWEzNzg1MDclMjIlMmMlMGQlMGErKyUyMnRlcm1pbmFsTWVyTmFtZSUyMiUzYSslMjJjZDQzODk4ZWU2ZTQ0ZTc3OWM0NjJjNTA2NzY0MjlhYiUyMiUyYyUwZCUwYSsrJTIybWVySWQlMjIlM2ErJTIyTTIwMTAwOSUyMiUyYyUwZCUwYSsrJTIyYml6VHlwZSUyMiUzYSslMjI1JTIyJTJjJTBkJTBhKyslMjJjaGFyc2V0JTIyJTNhKyUyMlVURi04JTIyJTJjJTBkJTBhKyslMjJzaWduVHlwZSUyMiUzYSslMjJNRDUlMjIlMmMlMGQlMGErKyUyMm5vdGljZVVybCUyMiUzYSslMjJodHRwJTI1M2ElMjUyZiUyNTJmY3JlZGl0Y2FyZC50dWxlcWlwYWkuY29tJTI1MmZhcGklMjUyZlVzZXIlMjUyZkNhbGxiYWNrQ29uZmlybUZlZVBheSUyMiUyYyUwZCUwYSsrJTIyc2lnbk1zZyUyMiUzYSslMjI3ODQ5RTJDMzUxMThGODA2NTg4Q0U1Q0QwMjg0NEU4NyUyMiUwZCUwYSU3ZA==");
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void payApply(int i){

        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("bizType","5");
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201010");
            map.put("merOrderId",("201909186620"+i)+1);
            map.put("currency","CNY");
            map.put("amount",500+i);
            map.put("identityType","1");
            map.put("bankCardType","1");
            map.put("identityNum","500240199101131111");
            map.put("bankCardNum","6227001935140663112");
            map.put("bankCode","CMB");
            map.put("validDate","0821");
            map.put("securityCode","893");
            map.put("bankCardPhone","15574928759");
            map.put("payFee","2");
            map.put("terminalMerId","M2010010011");
            map.put("terminalMerName","cd43898ee6e44e779c462c50676429ab");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("deviceType","1");
            map.put("deviceId","fd3123");
            map.put("macAddress","fa:s21:fef");

            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"4b61M50P"));
            String tradeInfo = JsonUtils.objectToJson(map);
//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
          String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8081/kuaijie/pay/confirmFeePay", postJson);
//            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/pay/payApply", postJson);

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
            map.put("merId","M201009");
            map.put("merOrderId","2019081611500262");
            map.put("smsCode","111111");
            map.put("terminalMerId","ef7414a76e23471aa533f6c51a378507");
            map.put("terminalMerName","test");
            map.put("payFee","2");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"vvK2424b"));
            String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8081/kuaijie/pay/confirmPay", postJson);
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
            map.put("merId","M201009");
            map.put("merOrderId","201908161150021");
            map.put("terminalMerId","M201001001");
            map.put("terminalMerName","test");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"vvK2424b"));
            String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8081/kuaijie/pay/confirmSMS", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void daifu(int i){
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("bizType","4");
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201010");
            map.put("merOrderId","2019091615289"+i);
            map.put("originalMerOrderId","201909186620471");
            map.put("currency","CNY");
            map.put("amount",2);
            map.put("identityType","1");
            map.put("bankCardType","2");
            map.put("identityNum","500240199101131111");
            map.put("bankCardNum","6227001935140663111");
            map.put("bankCardPhone","13599543918");
            map.put("bankCode","CMB");
            map.put("backFee","2.5");
            map.put("terminalMerId","M2010010011");
            map.put("terminalMerName","test");
            map.put("returnUrl","http://...");
            map.put("noticeUrl","http://...");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"4b61M50P"));
            String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8082/kuaijie/getTransOrder", postJson);
            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void balanceQuery(){
        try {
            Map<String, Object> map = new TreeMap<>();
           // map.put("bizType","5");
            map.put("charset","utf-8");
            map.put("signType","MD5");
            map.put("merId","M201004");
            map.put("merOrderId","201908141144001");
            map.put("bankCardNum","6227001935600377518");
            map.put("terminalMerId","M201004-001");
            map.put("terminalMerName","test");
            map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"KX956s4Q"));
            String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
            tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
            String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
            String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.110:8082/nvkuaijie/queryBalance", postJson);
        //   String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.109:8085/nvkuaijie/queryBalance", postJson);

            System.out.println(content);

//            System.out.println(postJson);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }}

    public void queryOrder() throws UnsupportedEncodingException {
        Map<String, Object> map = new TreeMap<>();
        // map.put("bizType","5");
        map.put("charset","utf-8");
        map.put("signType","MD5");
        map.put("merId","M201003");
        map.put("merOrderId","201907291550032");
        map.put("queryType","02");
        map.put("terminalMerId","M201001111");
        map.put("signMsg", CheckMd5Utils.getMd5SignWithKey(map,"357g4Y4v"));
        String tradeInfo = JsonUtils.objectToJson(map);

//            System.out.println(tradeInfo);
        tradeInfo= URLEncoder.encode(tradeInfo,"UTF-8");
        String postJson= new String( Base64.getEncoder().encode(tradeInfo.getBytes()),"UTF-8");
        String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.67:8030/kuaijie/queryOrder", postJson);
        //   String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://192.168.1.109:8085/nvkuaijie/queryBalance", postJson);

        System.out.println(content);
    }





    public static void main(String[] args) throws UnsupportedEncodingException {
        PayTest pt=new PayTest();
//        pt.queryOrder();
//        for (int i = 50;i<0;i--){
            pt.daifu(7);
//        }


    }
}

