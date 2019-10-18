package com.rxh.sicpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ExtraChannelInfo;
import com.rxh.square.pojo.MerchantCard;
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

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/sicPay")
public class SicPayBindCard {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(SicPayBindCard.class);

    /**
     * 高汇通绑卡入口
     * @param trade
     * @return
     */


    public BankResult bindCard(@RequestBody SquareTrade trade) throws Exception {
        trade.getMerchantRegisterCollect();
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        JSONObject param = JSON.parseObject(extraChannelInfo.getData());
        StringBuilder sBuilder = new StringBuilder();

        MerchantCard merchantCard = trade.getMerchantCard();

        String bankCardNo = merchantCard.getCardNum();
        Integer bankCardType = Integer.valueOf(merchantCard.getCardType());
        String mobilePhone = merchantCard.getPhone();
        String userId  =  merchantCard.getCardNum();
        String basicInfoBackData = trade.getMerchantRegisterCollect().getBackData();
        Map<String, String> headMap = new HashMap<String, String>();
        Map<String, String> bodyMap = new HashMap<String, String>();
         if(StringUtils.isNotBlank(basicInfoBackData)) {

            try {
                Document doc = DocumentHelper.parseText(basicInfoBackData);//将xml转为dom对象
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
        }
        String childMerchantId = bodyMap.get("merchantId");
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP001</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + merchantCard.getId()  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<bankCardNo>"+ bankCardNo +"</bankCardNo>"); // 银行卡全卡号
            sBuilder.append("<accountName>" + merchantCard.getName() +"</accountName>"); // 持卡人姓名
            sBuilder.append("<bankCardType>"+ "01"  +"</bankCardType>"); // 银行卡类型
            sBuilder.append("<certificateType>ZR01</certificateType>"); // 证件类型
            sBuilder.append("<certificateNo>"+ merchantCard.getIdentityNum()  +"</certificateNo>"); // 证件号码

        sBuilder.append("<mobilePhone>"+ merchantCard.getPhone() +"</mobilePhone>"); // 手机号码
        if (bankCardType == 2) {
            sBuilder.append("<valid>" + merchantCard.getExpireDate() +"</valid>"); // 有效期
            sBuilder.append("<cvn2>"+ merchantCard.getCvv() +"</cvn2>"); // 安全码后三位
            sBuilder.append("<bankCardType>"+ "02"  +"</bankCardType>"); // 银行卡类型
        }
        sBuilder.append("<terminalId>"+ param.get("terminalId") +"</terminalId>"); // 商户终端号
        sBuilder.append("<userId>"+ userId +"</userId>"); // 商户用户标识
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 进件返回的子商户号
        sBuilder.append("<backUrl>"+ "http://boc.cmtbuy.com/cross/sicPay/bondCardNofityUrl" +"</backUrl>"); //
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        logger.info("高汇通鉴权绑卡业务请求报文："+plainXML);
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
        nvps.add(new BasicNameValuePair("tranCode", "IFP001"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));
        logger.info("高汇通鉴权绑卡业务请求地址："+param.get("bindCardUrl").toString());
        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bindCardUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        logger.info("明文结果: " + t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }

    /**
     * 绑卡结果处理
     * @param content
     * @param trade
     * @return
     */
    @SuppressWarnings("unchecked")
    private BankResult checkResult(String content, SquareTrade trade) throws  Exception{
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
            String tranCode = headMap.get("tranCode");
            String reqMsgId = headMap.get("reqMsgId");
            switch (tranCode) {
                case "IFP001":
                    if (respType.equals("S"))
                    {
                        // 判断交易状态
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                bankResult.setBankTime(dateFormat.parse(respDate));
                                bankResult.setBankOrderId(payMsgId);
                                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                                bankResult.setBankResult("绑卡成功");
                                bankResult.setParam(content);
                                Map map = new HashMap();
                                map.put("bindId",bodyMap.get("bindId"));
                                bankResult.setBankData(JSONObject.toJSONString(map));
                                break;
                            case "100001":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                                bankResult.setBankResult("报文不合法");
                                bankResult.setParam(content);
                                break;
                            case "100003":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                                bankResult.setBankResult("验证签名失败");
                                bankResult.setParam(content);
                                break;
                            default:
                                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                                bankResult.setBankResult("请求失败:" + headMap.get("respMsg"));
                                bankResult.setParam(content);
                                break;
                        }

                    } else if(respType.equals("R")){
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                bankResult.setBankTime(dateFormat.parse(respDate));
                                bankResult.setBankOrderId(payMsgId);
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("绑卡请求成功");
                                bankResult.setParam(content);
                                trade.getMerchantCard().setId(reqMsgId);
                                bankResult = smsRequest(trade);
                                break;
                            case "100001":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("报文不合法");
                                bankResult.setParam(content);
                                break;
                            case "100003":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("验证签名失败");
                                bankResult.setParam(content);
                                break;
                            default:
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("请求失败:" + headMap.get("respMsg"));
                                bankResult.setParam(content);
                                break;

                        }


                    }  else{
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("请求绑卡失败:" + headMap.get("respMsg"));
                        bankResult.setParam(content);
                    }
                    break;
                case "IFP010":
                    if (respType.equals("S")) {
                        // 判断交易状态
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                bankResult.setBankTime(dateFormat.parse(respDate));
                                bankResult.setBankOrderId(payMsgId);
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("短信发送成功");
                                bankResult.setParam(content);
                                Map map = new HashMap();
                                map.put("bindOrderNo",bodyMap.get("bindOrderNo"));
                                map.put("reqMsgId",headMap.get("reqMsgId"));
                                bankResult.setBankData(JSONObject.toJSONString(map));
                                break;
                            case "100001":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("报文不合法");
                                bankResult.setParam(content);
                                break;
                            case "100003":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("验证签名失败");
                                bankResult.setParam(content);
                                break;
                            default:
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("短信发送失败:" + headMap.get("respMsg"));
                                bankResult.setParam(content);
                                break;
                        }

                    } else {
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("请求短信发送失败:" + headMap.get("respMsg"));
                        bankResult.setParam(content);
                    }
                    break;
                case "IFP011":
                    if (respType.equals("S")) {
                        // 判断交易状态
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                String bindId = headMap.get("bindId");
                                bankResult.setBankTime(dateFormat.parse(respDate));
                                bankResult.setBankOrderId(payMsgId);
                                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                                bankResult.setBankResult("短信验证成功");
                                bankResult.setParam(content);;
                                bankResult.setBankData(bindId);
                                break;
                            case "100001":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("报文不合法");
                                bankResult.setParam(content);
                                break;
                            case "100003":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("验证签名失败");
                                bankResult.setParam(content);
                                break;
                            default:
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("短信验证失败:" + headMap.get("respMsg"));
                                bankResult.setParam(content);
                                break;
                        }

                    } else if(respType.equals("R")){
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "0000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                bankResult.setBankTime(dateFormat.parse(respDate));
                                bankResult.setBankOrderId(payMsgId);
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("短信验证中");
                                bankResult.setParam(content);
                                bankResult = bindCardQuery(trade);
                                break;
                            case "100001":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("报文不合法");
                                bankResult.setParam(content);
                                break;
                            case "100003":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("验证签名失败");
                                bankResult.setParam(content);
                                break;
                            default:
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("短信验证失败:" + headMap.get("respMsg"));
                                bankResult.setParam(content);
                                break;

                        }


                    }  else  {
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("短信验证失败:" + headMap.get("respMsg"));
                        bankResult.setParam(content);
                    }
                    break;
                case "IFP023":
                    if (respType.equals("S")) {
                        // 判断交易状态
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String oriRespMsg = bodyMap.get("oriRespMsg");
                                String oriRespCode = bodyMap.get("oriRespCode");
                                String respDate = headMap.get("respDate");
                                Map map = new HashMap();
                                if (oriRespCode.equals("<![CDATA[000000]]>")){
                                    bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                                    bankResult.setBankResult("绑卡成功");
                                    bankResult.setBankData(bodyMap.get("bindId"));
                                    map.put("bindId",headMap.get("bindId"));
                                }else {
                                    bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                                    bankResult.setBankResult("绑卡失败："+oriRespMsg);
                                }
                                bankResult.setBankTime(dateFormat.parse(respDate));
                                bankResult.setParam(content);
                                map.put("reqMsgId",headMap.get("reqMsgId"));
                                bankResult.setBankData(JSONObject.toJSONString(map));
                                break;
                            case "100001":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("报文不合法");
                                bankResult.setParam(content);
                                break;
                            case "100003":
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("签名失败");
                                bankResult.setParam(content);
                                break;
                            default:
                                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                                bankResult.setBankResult("绑卡结果处理中:" + headMap.get("respMsg"));
                                bankResult.setParam(content);
                                break;
                        }

                    } else {
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("绑卡查询失败:" + headMap.get("respMsg"));
                        bankResult.setParam(content);
                    }
                    break;
                    default:
            }
        }else{
                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                bankResult.setBankResult("绑卡失败：支付返回结果为空！");
                bankResult.setBankCode("error.5000");
            }


        return bankResult;
    }


    /**
     * 高汇通绑卡短信请求入口
     * @param trade
     * @return
     */
    public BankResult smsRequest(@RequestBody SquareTrade trade) throws Exception {

        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        JSONObject param = JSON.parseObject(extraChannelInfo.getData());
        StringBuilder sBuilder = new StringBuilder();

        MerchantCard merchantCard = trade.getMerchantCard();
        String userId  =  merchantCard.getCardNum();
        String basicInfoBackData = trade.getMerchantRegisterCollect().getResult();
//        Map<String, String> headMap = new HashMap<String, String>();
//        Map<String, String> bodyMap = new HashMap<String, String>();
        String childMerchantId = null;
        if(StringUtils.isNotBlank(basicInfoBackData)) {
            childMerchantId = JSONObject.parseObject(basicInfoBackData).getString("bankData");
//            try {
//                Document doc = DocumentHelper.parseText(basicInfoBackData);//将xml转为dom对象
//                Element root = doc.getRootElement();//获取根节点
//                Element headElement = root.element("head");//获取名称为queryRequest的子节点
//                List<Element> headElements = headElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
//                for (Object obj : headElements) {  //遍历子元素
//                    headElement = (Element) obj;
//                    headMap.put(headElement.getName(), headElement.getTextTrim());//getName
//                }
//                Element bodyElement = root.element("body");//获取名称为queryRequest的子节点
//                List<Element> bodyElements = bodyElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
//                for (Object obj : bodyElements) {  //遍历子元素
//                    bodyElement = (Element) obj;
//                    bodyMap.put(bodyElement.getName(), bodyElement.getTextTrim());//getName
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP010</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createId()  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<userId>"+ userId +"</userId>"); // 商户用户标识
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 子商户号
        sBuilder.append("<terminalId>"+ param.get("terminalId") +"</terminalId>"); // 商户终端号
        sBuilder.append("<oriReqMsgId>"+ merchantCard.getId() +"</oriReqMsgId>"); // 原订单号
        sBuilder.append("<accountName>" + merchantCard.getName() +"</accountName>"); // 持卡人姓名
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        logger.info("高汇通短信发送业务请求报文："+plainXML);
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
        nvps.add(new BasicNameValuePair("tranCode", "IFP010"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bindCardUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        logger.info("明文结果: " + t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }


    /**
     * 高汇通绑卡短信确认入口
     * @param trade
     * @return
     */
    public BankResult smsConfirm(@RequestBody SquareTrade trade) throws Exception {

        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        JSONObject param = JSON.parseObject(extraChannelInfo.getData());
        StringBuilder sBuilder = new StringBuilder();

        MerchantCard merchantCard = trade.getMerchantCard();
        JSONObject result = JSONObject.parseObject(merchantCard.getResult());
        String userId  =  merchantCard.getCardNum();

        String basicInfoBackData = trade.getMerchantRegisterCollect().getBackData();
        Map<String, String> headMap = new HashMap<String, String>();
        Map<String, String> bodyMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(basicInfoBackData)) {

            try {
                Document doc = DocumentHelper.parseText(basicInfoBackData);//将xml转为dom对象
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
        }
        String childMerchantId = bodyMap.get("merchantId");

        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP011</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createKey("merchant_card")  + "</reqMsgId>"); //订单号

        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<bindOrderNo>"+ result.get("bindOrderNo") +"</bindOrderNo>"); // 绑卡订单号
        sBuilder.append("<userId>"+ userId +"</userId>"); // 商户用户标识
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 子商户号
        sBuilder.append("<oriReqMsgId>"+ result.get("reqMsgId") +"</oriReqMsgId>"); // 原订单号
        sBuilder.append("<validateCode>"+ trade.getTradeObjectSquare().getSmsCode() +"</validateCode>"); //短信验证码
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        logger.info("高汇通绑卡确认业务请求报文："+plainXML);
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
        nvps.add(new BasicNameValuePair("tranCode", "IFP011"));
        nvps.add(new BasicNameValuePair("callBack", "http://boc.cmtbuy.com/cross/sicPay/bondCardNofityUrl"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bindCardUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        logger.info("明文结果: " + t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }

    /**
     * 高汇通绑卡结果查询入口
     * @param trade
     * @return
     */
    public BankResult bindCardQuery(@RequestBody SquareTrade trade) throws Exception {

        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
        JSONObject param = JSON.parseObject(extraChannelInfo.getData());
        StringBuilder sBuilder = new StringBuilder();
        MerchantCard merchantCard = trade.getMerchantCard();
        JSONObject result = JSONObject.parseObject(merchantCard.getResult());
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP023</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createKey("merchant_card")  + "</reqMsgId>"); //订单号

        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        String basicInfoBackData = trade.getMerchantRegisterCollect().getResult();
        String childMerchantId = JSONObject.parseObject(basicInfoBackData).getString("bankData");
        sBuilder.append("<body>");
        sBuilder.append("<oriReqMsgId>"+ merchantCard.getId() +"</oriReqMsgId>"); // 原订单号
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 子商户号
        sBuilder.append("<bindOrderNo>"+ result.getString("bindOrderNo") +"</bindOrderNo>"); //
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        logger.info("高汇通绑卡业务查询请求报文："+plainXML);
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
        nvps.add(new BasicNameValuePair("tranCode", "IFP023"));
        nvps.add(new BasicNameValuePair("callBack", param.get("callBackUrl").toString()));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bindCardUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        logger.info("明文结果: " + t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
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

    @RequestMapping("/bondCardNofityUrl")
    public BankResult bondCardNofity(HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String resStr = CreditHttper.read(request);
        resStr = URLDecoder.decode(resStr,"UTF-8").replaceAll(" ", "+");
        logger.info("=============高汇通绑卡异步回调==============result："+resStr);
        if(StringUtils.isNotBlank(resStr)) {
            JSONObject jsonObject = JSONObject.parseObject(resStr);
        }
        return null;
    }



}
