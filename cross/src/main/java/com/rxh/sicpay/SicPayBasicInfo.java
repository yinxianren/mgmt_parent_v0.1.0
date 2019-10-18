package com.rxh.sicpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantAddCusInfo;
import com.rxh.pojo.merchant.MerchantBasicInformationRegistration;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.ExtraChannelInfo;
import com.rxh.square.pojo.MerchantRegisterCollect;
import com.rxh.square.pojo.PayOrder;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@RestController
@RequestMapping("/sicPay")
public class SicPayBasicInfo {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(SicPayBasicInfo.class);

    /**
     * 高汇通进件入口
     * @param trade
     * @return
     */
    @RequestMapping("/basic")
    public BankResult basic (@RequestBody SquareTrade trade) throws Exception {

        String type = trade.getTradeObjectSquare().getInnerType();
        BankResult bankResult = new BankResult();
        //根据不同状态执行相应接口
        switch(type){
            case SystemConstant.INFORMATION_REGISTRATION:
                bankResult = basicInfo(trade);
                break;
            case SystemConstant.BANKCARD_REGISTRATION:
                bankResult = bankInfo(trade);
                break;
            case SystemConstant.SERVICE_FULFILLMENT:
                bankResult = busiInfo(trade);
                break;
            default:
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("进件失败，接口编码错误");
                break;
        }
        return  bankResult;
    }

    /**
     * 商户信息登记
     * @param trade
     * @return
     * @throws Exception
     */
    @RequestMapping("/basicInfo")
    public BankResult basicInfo(@RequestBody SquareTrade trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        TradeObjectSquare tradeObjectSquare =trade.getTradeObjectSquare();
        MerchantBasicInformationRegistration merchantRegisterInfo = trade.getMerchantBasicInformationRegistration();
        JSONObject param = JSON.parseObject(extraChannelInfo.getData());

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>100001</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createId() + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<handleType>0</handleType>"); //操作类型 0新增，1修改
        sBuilder.append("<merchantName>"+merchantRegisterInfo.getTerminalMerName()+"</merchantName>"); //商户名称
        sBuilder.append("<shortName>"+merchantRegisterInfo.getUserShortName()+"</shortName>"); //商户简称
        sBuilder.append("<city>"+merchantRegisterInfo.getCity()+"</city>"); //商户城市
        sBuilder.append("<merchantAddress>"+merchantRegisterInfo.getAddress()+"</merchantAddress>"); //商户地址
        sBuilder.append("<servicePhone>"+merchantRegisterInfo.getPhone()+"</servicePhone>"); //客服电话
        sBuilder.append("<merchantType>"+merchantRegisterInfo.getMerchantType()+"</merchantType>"); //商户类型，00公司商户，01个体商户
        sBuilder.append("<category>"+merchantRegisterInfo.getCategory()+"</category>"); //经营类目代码
        sBuilder.append("<corpmanName>"+merchantRegisterInfo.getCardHolderName()+"</corpmanName>"); //法人姓名
        sBuilder.append("<corpmanId>"+merchantRegisterInfo.getIdentityNum()+"</corpmanId>"); //法人身份证
        sBuilder.append("<corpmanMobile>"+merchantRegisterInfo.getPhone()+"</corpmanMobile>"); //法人联系手机
        sBuilder.append("<bankCode>"+trade.getBankInfo().getBankThreeCode()+"</bankCode>"); //银行代码
        sBuilder.append("<bankName>"+trade.getBankInfo().getBankName()+"</bankName>"); //开户行全称
        sBuilder.append("<bankaccountNo>"+merchantRegisterInfo.getBankCardNum()+"</bankaccountNo>"); //开户行账号
        sBuilder.append("<bankaccountName>"+merchantRegisterInfo.getCardHolderName()+"</bankaccountName>"); //开户户名
        sBuilder.append("<autoCus>0</autoCus>"); //自动提现
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        logger.info("高汇通商户登记请求报文："+plainXML);

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
        nvps.add(new BasicNameValuePair("tranCode", "100001"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("basicInfoUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        logger.info("明文结果: " + t.respDecryption(response));
        return  checkResult(t.respDecryption(response),trade);
    }

    /**
     * 银行卡信息登记
     * @param trade
     * @return
     * @throws Exception
     */
    @RequestMapping("/bankInfo")
    public BankResult bankInfo(@RequestBody SquareTrade trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        MerchantRegisterInfo merchantRegisterInfo = trade.getMerchantRegisterInfo();
        MerchantRegisterCollect merchantRegisterCollect = trade.getMerchantRegisterCollect();
        StringBuilder sBuilder = new StringBuilder();
        JSONObject param = JSONObject.parseObject(extraChannelInfo.getData());
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>100002</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + trade.getMerchantBankCardBinding().getMerOrderId()  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<merchantId>"+JSONObject.parseObject(merchantRegisterCollect.getResult()).getString("bankData")+"</merchantId>"); //子商户编码
        sBuilder.append("<mobileNo2>"+merchantRegisterInfo.getPhone()+"</mobileNo2>"); //持卡人手机号
        sBuilder.append("<handleType>0</handleType>"); //操作类型 0新增 1删除 2修改
        sBuilder.append("<bankCode>"+trade.getBankInfo().getBankThreeCode()+"</bankCode>"); //银行代码
        if (merchantRegisterInfo.getMerchantType().equals("01")){
            sBuilder.append("<bankaccProp>"+0+"</bankaccProp>"); //账户属性 0私人 1公司
        } else {
            sBuilder.append("<bankaccProp>"+1+"</bankaccProp>"); //账户属性 0私人 1公司
        }

        sBuilder.append("<name>"+merchantRegisterInfo.getUserName()+"</name>"); //持卡人姓名
        sBuilder.append("<bankaccountNo>"+merchantRegisterCollect.getCardNum()+"</bankaccountNo>"); //银行卡号
        sBuilder.append("<bankaccountType>"+merchantRegisterCollect.getCardType()+"</bankaccountType>"); //银行卡类型
        sBuilder.append("<certCode>"+merchantRegisterInfo.getIdentityType()+"</certCode>"); //办卡证件类型
        sBuilder.append("<certNo>"+merchantRegisterInfo.getIdentityNum()+"</certNo>"); //证件号码
//        sBuilder.append("<defaultAcc>0</defaultAcc>"); //是否默认账户 0否
//        sBuilder.append("<province>0</province>"); // 省
//        sBuilder.append("<city>0</city>"); // 市
//        sBuilder.append("<bankBranchName>0</bankBranchName>"); // 支行名称
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        logger.info("高汇通银行卡登记请求报文："+plainXML);

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
        nvps.add(new BasicNameValuePair("tranCode", "100002"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bankInfoUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        logger.info("明文结果: " + t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }

    /**
     * 支付业务开通
     * @param trade
     * @return
     * @throws Exception
     */
    @RequestMapping("/busiInfo")
    public BankResult busiInfo(@RequestBody SquareTrade trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        MerchantRegisterCollect merchantRegisterCollect = trade.getMerchantRegisterCollect();
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        JSONObject param = JSON.parseObject(extraChannelInfo.getData());
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>100003</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + trade.getMerOrderId()  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<merchantId>"+JSONObject.parseObject(merchantRegisterCollect.getResult()).getString("bankData")+"</merchantId>"); // 子商户号
        sBuilder.append("<handleType>0</handleType>"); // 操作类型
        sBuilder.append("<cycleValue>1</cycleValue>"); // 结算周期
        sBuilder.append("<busiList>");
        sBuilder.append("<busiCode>B00108</busiCode>"); // 开通业务
//        sBuilder.append("<rateList>");
        sBuilder.append("<futureRateType>1</futureRateType>"); //费率类型 1百分比 2单笔
        sBuilder.append("<futureRateValue>"+merchantRegisterCollect.getTradeFee()+"</futureRateValue>"); // 费率
//        sBuilder.append("</rateList>");
//        sBuilder.append("<certNo>0</certNo>"); //
//        sBuilder.append("<certNo>0</certNo>"); //
//        sBuilder.append("<certNo>0</certNo>"); //
        sBuilder.append("</busiList>");

        sBuilder.append("<busiList>");
        sBuilder.append("<busiCode>B00302</busiCode>"); // 开通业务
        sBuilder.append("<futureRateType>1</futureRateType>"); //费率类型 1百分比 2单笔
        sBuilder.append("<futureRateValue>"+merchantRegisterCollect.getBackFee()+"</futureRateValue>"); // 费率
        sBuilder.append("</busiList>");


        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        logger.info("高汇通开通业务请求报文："+plainXML);

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
        nvps.add(new BasicNameValuePair("tranCode", "100003"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("busiInfoUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        logger.info("明文结果: " + t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }

    /**
     * 交易结果处理
     * @param content
     * @param trade
     * @return
     * @throws PayException
     * @throws UnsupportedEncodingException
     */

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
                List<Element> bodyElements = bodyElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
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
                        String respDate = headMap.get("respDate");
                        String payMsgId = headMap.get("payMsgId");
                        bankResult.setBankTime(dateFormat.parse(respDate));
                        bankResult.setBankOrderId(payMsgId);
                        bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                        bankResult.setBankResult("交易成功");
                        bankResult.setParam(content);
                        bankResult.setBankData(bodyMap.get("merchantId"));
                        break;
                    case "100001":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_UNPAID);
                        bankResult.setBankResult("报文不合法");
                        bankResult.setParam(content);
                        bankResult.setBankData(bodyMap.get("merchantId"));
                        break;
                    case "100003":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_UNPAID);
                        bankResult.setBankResult("交易失败");
                        bankResult.setParam(content);
                        bankResult.setBankData(bodyMap.get("merchantId"));
                        break;
                    default:
                        bankResult.setStatus(SystemConstant.BANK_STATUS_UNPAID);
                        bankResult.setBankResult("交易失败:" + headMap.get("respMsg"));
                        bankResult.setParam(content);
                        bankResult.setBankData(bodyMap.get("merchantId"));
                        break;
                }

            }else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_UNPAID);
                bankResult.setBankResult("交易失败:" + headMap.get("respMsg"));
                bankResult.setBankData(bodyMap.get("merchantId"));
                bankResult.setParam(content);
            }
        }else {
            bankResult.setStatus(SystemConstant.BANK_STATUS_UNPAID);
            bankResult.setBankResult("交易失败：支付返回结果为空！");
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

    public static void main(String[] args) {
        SicPayBasicInfo s = new SicPayBasicInfo();
        SquareTrade trade = new SquareTrade();
        ChannelInfo channelInfo = new ChannelInfo();
        Map<String,Object> map = new HashMap<>();
        map.put("agencyId","574034451110001");
        map.put("terminalId","20003971");
        map.put("bindId","62262626262626262");
//        map.put("childMerchantId","262626262");
        channelInfo.setOthers(JSON.toJSONString(map));
        PayOrder payOrder = new PayOrder();
        payOrder.setAmount(new BigDecimal(100));
        payOrder.setMerOrderId("12311333131");
        TradeObjectSquare tradeObjectSquare = new TradeObjectSquare();
        tradeObjectSquare.setMerId("M2012005");
        trade.setChannelInfo(channelInfo);
        trade.setPayOrder(payOrder);
        trade.setTradeObjectSquare(tradeObjectSquare);
        try {
            s.basicInfo(trade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
