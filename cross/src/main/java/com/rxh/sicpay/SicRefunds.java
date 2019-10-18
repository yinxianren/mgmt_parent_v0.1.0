package com.rxh.sicpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.PayOrder;
import com.rxh.utils.CryptoUtil;
import com.rxh.utils.HttpClient4Util;
import com.rxh.utils.SicEncype;
import com.rxh.utils.SicPayRSAUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 退货退款
 * @author qc
 */
@Controller
@RequestMapping("/sicPay")
public class SicRefunds {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(SicPayApply.class);

    @RequestMapping("/refunds")
    @ResponseBody
    public BankResult refunds(@RequestBody SquareTrade trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        PayOrder payOrder = trade.getPayOrder();

        payOrder.getMerId();
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");

        sBuilder.append("<head>");
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>"+ payOrder.getMerId()+"</agencyId>");// 商户标识
        sBuilder.append("<msgType>01</msgType>");
        sBuilder.append("<tranCode>IFP005</tranCode>");
        sBuilder.append("<reqMsgId>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqMsgId>");// 请求交易流水号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");// 请求日期时间
        sBuilder.append("</head>");

        sBuilder.append("<body>");
        sBuilder.append("<currency>"+payOrder.getCurrency()+"</currency>");  //交易币种
        sBuilder.append("<amount>"+payOrder.getAmount()+"</amount>"); //退款金额
        sBuilder.append("<refundReason>test1</refundReason>"); //退款说明
        sBuilder.append("<oriPayMsgId>"+payOrder.getMerOrderId()+"</oriPayMsgId>");  //原请求交易流水号
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        System.out.println("请求报文："+plainXML);
        byte[] plainBytes = plainXML.getBytes("UTF-8");
        String keyStr = "1122334455667788";
        byte[] keyBytes = keyStr.getBytes("UTF-8");
        byte[] base64EncryptDataBytes = Base64.encodeBase64(CryptoUtil.AESEncrypt(plainBytes, keyBytes, "AES", "AES/ECB/PKCS5Padding", null));
        String encryptData = new String(base64EncryptDataBytes, "UTF-8");
        byte[] base64SingDataBytes = Base64.encodeBase64(CryptoUtil.digitalSign(plainBytes, hzfPriKey, "SHA1WithRSA"));
        String signData = new String(base64SingDataBytes, "UTF-8");
        byte[] base64EncyrptKeyBytes = Base64.encodeBase64(CryptoUtil.RSAEncrypt(keyBytes, yhPubKey, 2048, 11, "RSA/ECB/PKCS1Padding"));
        String encrtptKey = new String(base64EncyrptKeyBytes, "UTF-8");
        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        nvps.add(new BasicNameValuePair("encryptData", encryptData));
        nvps.add(new BasicNameValuePair("encryptKey", encrtptKey));
        nvps.add(new BasicNameValuePair("signData", signData));
        nvps.add(new BasicNameValuePair("agencyId", payOrder.getMerId()));
        nvps.add(new BasicNameValuePair("tranCode", "IFP005"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));
        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost("https://testpay.sicpay.com/quickInter/channel/commonSyncInter.do", null, nvps, null);

        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        System.out.println("明文结果: " + t.respDecryption(response));

        return null;

    }


    public static void main(String[] args) {
        SicRefunds s = new SicRefunds();
        SquareTrade trade = new SquareTrade();
        PayOrder payOrder = new PayOrder();
        payOrder.setAmount(new BigDecimal(9527));
        payOrder.setMerId("574034451110001");
        payOrder.setCurrency("156");
        payOrder.setMerOrderId("20003971");
        trade.setPayOrder(payOrder);
        try {
            s.refunds(trade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

