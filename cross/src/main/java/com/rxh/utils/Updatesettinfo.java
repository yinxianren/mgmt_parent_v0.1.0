package com.rxh.utils;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

public class Updatesettinfo {

    public static void main(String[] args) throws UnsupportedEncodingException {
    /*
    "appid": "6666678",
  "key": "549935538f05ceaef400cbdb2dda4ebf",
  "orgid": "201000003530",
  "cusid": "101000003623",
  "payApply": "apply",
  "payConfirm": "confirm",
     */
        TreeMap<String,Object> map = new TreeMap<String, Object>();
        map.put("cusid","101000003639");//cusid -> 101000003631 cusid -> 101000003639
        map.put("orgid","201000003530");
        map.put("appid","6666678");
        map.put("version","1");
        map.put("randomstr","qaz123789456");//随机字符串	商户自行生成的随机字符串
//        map.put("reqip","127.0.0.1");
//        map.put("clearmode","1");
        map.put("prodlist","[{'trxcode':'QUICKPAY_OF_HP','feerate':'0.50'},{'trxcode':'QUICKPAY_OF_NP','feerate':'0.45'},{'trxcode':'TRX_PAY','feerate':'2'}]");
        map.put("key","549935538f05ceaef400cbdb2dda4ebf");
        map.put("sign", AlinPayUtils.getMd5Sign(map));
        map.remove("key");
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(),"https://test.allinpaygd.com/ipayapiweb/org/updatesettinfo", map);
        System.out.println(content);

        /*
        {"appid":"6666678","retcode":"SUCCESS","retmsg":"处理成功","sign":"2078A7C847C24782D9A66B6692A76F3E"}
        {"appid":"6666678","retcode":"SUCCESS","retmsg":"处理成功","sign":"2078A7C847C24782D9A66B6692A76F3E"}
        {"appid":"6666678","retcode":"SUCCESS","retmsg":"处理成功","sign":"2078A7C847C24782D9A66B6692A76F3E"}
        {"appid":"6666678","retcode":"SUCCESS","retmsg":"处理成功","sign":"2078A7C847C24782D9A66B6692A76F3E"}
         */

    }
}
