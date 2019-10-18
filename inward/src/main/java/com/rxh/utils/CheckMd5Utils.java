package com.rxh.utils;


import com.rxh.exception.PayException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class CheckMd5Utils {

    private static Logger logger = LoggerFactory.getLogger(CheckMd5Utils.class);

    public static String getMd5Sign(Map<String,Object> param)  {
        StringBuffer sb = new StringBuffer();
        for (String key : param.keySet()){
            String value = param.get(key) != null ? param.get(key).toString() : null;
            if(!StringUtils.isEmpty(value)){
                sb.append(key+"="+value+"&");
            }
        }
        String sign = sb.substring(0,sb.lastIndexOf("&"));
        String md5Sign=  DigestUtils.md5Hex(sign.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return md5Sign;
    }
    public static String getMd5SignWithKey(Map<String,Object> param,String md5Key)  {
        StringBuffer sb = new StringBuffer();
        for (String key : param.keySet()){
            String value = param.get(key) != null ? param.get(key).toString() : null;
            if(!StringUtils.isEmpty(value)){
                sb.append(key+"="+value+"&");
            }
        }
        String sign = sb.substring(0,sb.lastIndexOf("&"))+md5Key;
        String md5Sign=  DigestUtils.md5Hex(sign.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return md5Sign;
    }


    public static void checkMd5(String tradeInfo,String key ) throws PayException {
        Map<String, Object> param = JsonUtils.jsonToMap(tradeInfo);
        String signMsg = param.get("signMsg").toString();
        param.remove("signMsg");
        Map<String, Object> map = new TreeMap<>();
        map.putAll(param);
        String md5Info = getMd5SignWithKey(map,key).toUpperCase();
        if (!md5Info.equals(signMsg.toUpperCase())){
            logger.info("");
            throw new PayException("签名验证不正确",1023);
        }
    };


}
