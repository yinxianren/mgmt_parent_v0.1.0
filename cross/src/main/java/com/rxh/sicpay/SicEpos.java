package com.rxh.sicpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.*;
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
 * 无卡支付
 * @author qc
 */
@Controller
@RequestMapping("/sicPay")
public class SicEpos {


    @RequestMapping("/ePOS")
    @ResponseBody
    public BankResult ePOS(@RequestBody SquareTrade trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        PayOrder payOrder = trade.getPayOrder();


        String agencyId = payOrder.getMerId();
        String terminalId = payOrder.getTerminalMerId();
        String bankCardNo= trade.getBankCode();
        String accountName = trade.getTradeObjectSquare().getBenefitName();
        String bankCardType =trade.getTradeObjectSquare().getBankCardType()+"";
        String certificateType =trade.getTradeObjectSquare().getIdentityType()+"";
        String  certificateNo  = trade.getTradeObjectSquare().getIdentityNum();
        Long mobilePhone = Long.parseLong(trade.getTradeObjectSquare().getCardholderPhone());
        String mon =trade.getTradeObjectSquare().getExpireMonth();
        String year = trade.getTradeObjectSquare().getExpireYear();
        String cvn2 = trade.getTradeObjectSquare().getCvv();
        BigDecimal amount =trade.getTradeObjectSquare().getAmount();
        String productCategory =trade.getPayProductDetail().getProductTypeId()+"";
        String productName = trade.getPayProductDetail().getProductName();
        String productDesc = trade.getPayProductDetail().getProductDescribe();






        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");

        sBuilder.append("<head>");
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>"+agencyId+"</agencyId>");// 商户标识
        sBuilder.append("<msgType>01</msgType>");  //报文类型
        sBuilder.append("<tranCode>IFP00B</tranCode>");
        sBuilder.append("<reqMsgId>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqMsgId>");// 请求交易流水号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");// 请求日期时间
        sBuilder.append("</head>");

        sBuilder.append("<body>");
        sBuilder.append("<userId>"+agencyId+"</userId>");  //M 商户用户 标识
        sBuilder.append("<terminalId>"+terminalId+"</terminalId>"); //M 商户终端号
        sBuilder.append("<childMerchantId>00003351</childMerchantId>");//C  子商户号，如果为一户一码模式则必填
        sBuilder.append("<bankCardNo>"+bankCardNo+"</bankCardNo>"); //M 银行卡号
        sBuilder.append("<accountName>"+accountName+"</accountName>");  //C 持卡人姓名  银行卡类型为01 借记卡时必输；为 02 信用卡时选输，但是其与证件类型、证 件号码共生
        sBuilder.append("<bankCardType>"+bankCardType+"</bankCardType>"); //M 银行卡类型 01：借记卡 02：信用卡
        sBuilder.append("<certificateType>"+certificateType+"</certificateType>");//C 证件类型
        sBuilder.append("<certificateNo>"+certificateNo+"</certificateNo>"); //C 证件号码
        sBuilder.append("<mobilePhone>"+mobilePhone+"</mobilePhone>"); // M 手机号码
        sBuilder.append("<valid>"+mon+year.substring(year.length() -2,year.length())+"</valid>"); //C 有效期 银行卡类型为02信用卡时必输，为01借 记卡时选输，但是其与CVN2共生 表示信用卡的有效期，格式为 MMYY （月月年年），如 0715
        sBuilder.append("<cvn2>"+cvn2+"</cvn2>"); //C 银行卡类型为02信用卡时必输，为01借 记卡时选输，但是其与有效期共生 表示信用卡背面后3位数字
        sBuilder.append("<amount>"+amount+"</amount>"); //M 交易金额
        sBuilder.append("<productCategory>"+productCategory+"</productCategory>");//M 商品类别
        sBuilder.append("<productName>"+productName+"</productName>"); // M 商品名称
        sBuilder.append("<productDesc>"+productDesc+"</productDesc>"); // M 商品描述
        sBuilder.append("<notify_url>dd</notify_url>");//M异步通知地址
        sBuilder.append("<fcCardNo>13599543918</fcCardNo>"); //C 入金卡号
        sBuilder.append("<userFee>11</userFee>");//C用户手续费
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
        nvps.add(new BasicNameValuePair("agencyId", "574034451110001"));
        nvps.add(new BasicNameValuePair("tranCode", "IFP00B"));
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
        SicEpos s = new SicEpos();
        SquareTrade trade = new SquareTrade();
        PayOrder payOrder = new PayOrder();
        TerminalMerchantsDetails terminalMerchantsDetails =new TerminalMerchantsDetails();
        PayCardholderInfo payCardholderInfo = new PayCardholderInfo();
        PayProductDetail payProductDetail = new PayProductDetail();
        payOrder.setMerId("574034451110001");
        payOrder.setTerminalMerId("20003971");
        terminalMerchantsDetails.setTerminalMerId("20003971");
        payCardholderInfo.setBankcardNum("6227001936500377518");
        payCardholderInfo.setCardholderName("钱川");
        payCardholderInfo.setBankcardType(01);
        payCardholderInfo.setIdentityNum("500234199211038433");
        payCardholderInfo.setCardholderPhone("13599543918");
        payOrder.setAmount(BigDecimal.valueOf(9527));
        payProductDetail.setProductTypeId(17);
        payProductDetail.setProductName("机票");
        payProductDetail.setProductDescribe("Jijipiaopiao");
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        systemOrderTrack.setNoticeUrl("www.baidu..com");
        trade.setPayOrder(payOrder);
        trade.setTerminalMerchantsDetails(terminalMerchantsDetails);
        trade.setPayCardholderInfo(payCardholderInfo);
        trade.setPayProductDetail(payProductDetail);
        try {
            s.ePOS(trade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
