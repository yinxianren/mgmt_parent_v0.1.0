package com.rxh.sicpay;

import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.*;
import com.rxh.utils.UUID;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/sicPay")
public class SicPayQuery {

    private final Logger logger = LoggerFactory.getLogger(SicPayQuery.class);

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @RequestMapping("/queryPay")
    public BankResult queryPay(@RequestBody SquareTrade trade) throws Exception {

        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject param = JSONObject.parseObject(channelInfo.getOthers());
        String basicInfoBackData = trade.getMerchantRegisterCollect().getResult();
        String childMerchantId = JSONObject.parseObject(basicInfoBackData).getString("bankData");
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP006</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createKey("pay_order") + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<oriReqMsgId>" + trade.getPayOrder().getPayId() + "</oriReqMsgId>");//原商户订单号
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 子商户号
//        sBuilder.append("<oriPayMsgId>" +  + "</oriPayMsgId>");//原平台流水号
        sBuilder.append("</body>");//商户终端号
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        logger.info("高汇通支付查询请求报文："+plainXML);

        byte[] plainBytes = plainXML.getBytes("UTF-8");
        String keyStr = getRandomSecretkey();
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
        nvps.add(new BasicNameValuePair("agencyId", param.get("agencyId").toString()));
        nvps.add(new BasicNameValuePair("signData", signData));
        nvps.add(new BasicNameValuePair("tranCode", "IFP006"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));
        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("queryUrl").toString(), null, nvps, null);

        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        logger.info("明文结果: " + t.respDecryption(response));

        return checkResult(t.respDecryption(response),trade);
    };

    @RequestMapping("/queryTrans")
    public BankResult queryTrans(@RequestBody SquareTrade trade) throws Exception {

        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject param = JSONObject.parseObject(channelInfo.getOthers());
        String basicInfoBackData = trade.getMerchantRegisterCollect().getResult();
        String childMerchantId = JSONObject.parseObject(basicInfoBackData).getString("bankData");
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP022</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createKey("trans_order")  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<oriReqMsgId>" + trade.getTransOrder().getTransId() + "</oriReqMsgId>");//原商户订单号
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 子商户号
//        sBuilder.append("<oriPayMsgId>" +  + "</oriPayMsgId>");//原平台流水号
        sBuilder.append("</body>");//商户终端号
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        logger.info("高汇通代付查询请求报文："+plainXML);

        byte[] plainBytes = plainXML.getBytes("UTF-8");
        String keyStr = getRandomSecretkey();
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
        nvps.add(new BasicNameValuePair("agencyId", param.get("agencyId").toString()));
        nvps.add(new BasicNameValuePair("signData", signData));
        nvps.add(new BasicNameValuePair("tranCode", "IFP022"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));
        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("queryUrl").toString(), null, nvps, null);

        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        logger.info("明文结果: " + t.respDecryption(response));

        return checkResult(t.respDecryption(response),trade);
    };

    private BankResult checkResult(String content, SquareTrade trade) throws PayException, UnsupportedEncodingException, ParseException {
        BankResult bankResult = new BankResult();
        if(StringUtils.isNotBlank(content)) {
            Map<String, String> headMap = new HashMap<String, String>();
            Map<String, String> bodyMap = new HashMap<String, String>();
            try {
                Document doc = DocumentHelper.parseText(content);//将xml转为dom对象
                Element root = doc.getRootElement();//获取根节点
                Element headElement = root.element("head");//获取名称为queryRequest的子节点
                List<Element> headElements = headElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : headElements) {  //遍历子元素
                    headElement = (Element) obj;
                    headMap.put(headElement.getName(), headElement.getTextTrim());//getName
                }
                Element bodyElement = root.element("body");//获取名称为queryRequest的子节点
                List<Element> bodyElements = headElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : bodyElements) {  //遍历子元素
                    bodyElement = (Element) obj;
                    bodyMap.put(bodyElement.getName(), bodyElement.getTextTrim());//getName
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String respType = headMap.get("respType");
            if(respType.equals("S")) {
                // 判断交易状态
                String respCode = headMap.get("respCode");
                switch(respCode){
                    case "000000":
                        String oriRespType = bodyMap.get("oriRespType");
                        switch (oriRespType){
                            case "S" :
                                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                                bankResult.setBankResult("交易成功");
                                break;
                            case "E" :
                                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                                bankResult.setBankResult("交易失败");
                                break;
                            case "R" :
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("交易处理中");
                                break;
                            default:
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("交易处理中");
                                break;
                        }
                        String respDate = headMap.get("respDate");
                        String payMsgId = headMap.get("payMsgId");
                        bankResult.setBankTime(dateFormat.parse(respDate));
                        bankResult.setBankOrderId(payMsgId);
                        bankResult.setParam(content);
                        break;
                    case "100001":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("查询失败");
                        bankResult.setParam(content);
                        break;
                    case "100003":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("查询失败");
                        bankResult.setParam(content);
                        break;
                    default:
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("查询失败:" + headMap.get("respMsg"));
                        bankResult.setParam(content);
                        break;
                }

            }else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("交易失败：" + headMap.get("respMsg"));
                bankResult.setParam(content);
            }
        }else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
            bankResult.setBankResult("查询失败：支付返回结果为空！");
            bankResult.setBankCode("error.5000");
        }
        return bankResult;
    }

    public static String getRandomSecretkey() {
        String val = "";
        Random random = new Random();
        int length = 16; //length为几位密码
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    public static void main(String[] args) throws Exception {
        SicPayQuery sicPayQuery = new SicPayQuery();
        TransOrder transOrder = new TransOrder();
        transOrder.setTransId("1235464646");
        ChannelInfo channelInfo = new ChannelInfo();
        Map map = new HashMap();
        map.put("agencyId","574034451110001");
        map.put("queryUrl","https://testpay.sicpay.com/quickInter/channel/commonSyncInter.do");
        channelInfo.setOthers(JSONObject.toJSONString(map));
        SquareTrade trade = new SquareTrade();
        trade.setChannelInfo(channelInfo);
        trade.setTransOrder(transOrder);
        sicPayQuery.queryTrans(trade);
    }
}
