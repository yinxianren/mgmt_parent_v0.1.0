package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.*;
import com.rxh.utils.AlinPayUtils;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/allinPay")
public class AllinPayBindCard {
    private final static Logger logger = LoggerFactory.getLogger(AllinPayBindCard.class);
    //绑卡 /bondCard
    @RequestMapping("/bondCard")
    @ResponseBody
    public BankResult bondCard(@RequestBody RequestCrossMsgDTO trade) throws PayException, UnsupportedEncodingException {
        BankResult bankResult = new BankResult();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        Map<String, Object> bondParam = getBondParam(trade);

        logger.info("绑卡请求参数"+bondParam);

        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), param.getString("cardApplyUrl"), bondParam);
//        String content ="{\"appid\":\"6666678\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"B5B5947BE8639146BC77CCA4BC7BE236\",\"trxstatus\":\"1999\"}";
        logger.info("绑卡银行返回"+content);
//        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://test.allinpaygd.com/ipayapiweb/org/bindcard", bondParam);
        JSONObject result = (JSONObject) JSON.parse(content);
        String resultCode = result.getString("retcode");
        String eroMsg = result.getString("retmsg");
        if ("SUCCESS".equals(resultCode)) {
            String trxstatus = result.getString("trxstatus");

            switch (trxstatus) {
                case "0000":
                    String agreeid = result.getString("agreeid");
                    bankResult.setStatus(StatusEnum._0.getStatus());
                    bankResult.setBankResult("绑卡成功");
                    bankResult.setParam(content);
                    bankResult.setBankData(agreeid);
                    break;
                case "3051":
                    bankResult.setStatus(StatusEnum._0.getStatus());
                    bankResult.setBankResult("协议已存在,请勿重复签约");
                    bankResult.setParam(content);
                    break;
                case "1999":
                    String thpinfo = result.getString("thpinfo");
                    bankResult.setBankData(thpinfo);
                    bankResult.setStatus(StatusEnum._3.getStatus());
                    bankResult.setBankResult("短信验证码已发送,请继续调用签约确认接口完成绑卡操作");
                    bankResult.setParam(content);
                    break;
                default:
                    String eroMsg1 = result.getString("retmsg");
                    bankResult = new BankResult(StatusEnum._1.getStatus(), "error.5000");
                    bankResult.setBankResult("绑卡失败: "+eroMsg1);
                    bankResult.setParam(content);
                    break;
            }
        } else {
            bankResult = new BankResult(StatusEnum._1.getStatus(), "error.5000");
            bankResult.setBankResult("绑卡失败"+eroMsg);
            bankResult.setParam(content);
        }
        logger.info("绑卡返回结果"+ JsonUtils.objectToJsonNonNull(bankResult));
        return bankResult;
    }

    //绑卡 /reGetCode
    @RequestMapping("/reGetCode")
    @ResponseBody
    public BankResult reGetCode(@RequestBody RequestCrossMsgDTO trade) throws PayException, UnsupportedEncodingException {
        BankResult bankResult = new BankResult();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        Map<String, Object> getCodeParam = reGetCodeParam(trade);

        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), param.getString("cardSmsUrl"), getCodeParam);
//        String content = "{\"agreeid\":\"201906111653355078\",\"appid\":\"6666678\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"0FED3B1A33F2DB99A89EE4938C84854F\",\"trxstatus\":\"0000\"}";
        JSONObject result = (JSONObject) JSON.parse(content);
        String resultCode = result.getString("retcode");

        if ("SUCCESS".equals(resultCode)) {
            bankResult.setStatus(StatusEnum._0.getStatus());
            bankResult.setBankResult("短信验证码已发送,请继续调用签约确认接口完成绑卡操作");
            bankResult.setParam(content);
        } else {
            String eroMsg = result.getString("retmsg");

            bankResult = new BankResult(StatusEnum._1.getStatus(), "error.5000");
            bankResult.setBankResult("短信验证码发送失败: "+eroMsg);
            bankResult.setParam(content);
        }
        return bankResult;
    }

    //绑卡确认 /confirmBond
    @RequestMapping("/confirmBond")
    @ResponseBody
    public BankResult confirmBond(@RequestBody RequestCrossMsgDTO trade) throws PayException, UnsupportedEncodingException {
        BankResult bankResult = new BankResult();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        Map<String, Object> confirmParam = getConfirmParam(trade);
        logger.info("绑卡确认银行参数"+ confirmParam);
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), param.getString("cardConfirmUrl"), confirmParam);
//        String content = "{\"agreeid\":\"201906111653355078\",\"appid\":\"6666678\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"0FED3B1A33F2DB99A89EE4938C84854F\",\"trxstatus\":\"0000\"}";
        logger.info("绑卡确认返回结果"+ content);
//        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://test.allinpaygd.com/ipayapiweb/org/bindcardconfirm", confirmParam);
        JSONObject result = (JSONObject) JSON.parse(content);
        String resultCode = result.getString("retcode");

        if ("SUCCESS".equals(resultCode)) {
            String trxstatus = result.getString("trxstatus");
            switch (trxstatus) {
                case "0000":
                    String agreeid = result.getString("agreeid");
                    bankResult.setStatus(StatusEnum._0.getStatus());
                    bankResult.setBankResult("绑卡成功");
                    bankResult.setParam(content);
                    bankResult.setBankData(agreeid);
                    break;
                case "3051":
                    bankResult.setStatus(StatusEnum._0.getStatus());
                    bankResult.setBankResult("协议已存在,请勿重复签约");
                    bankResult.setParam(content);
                    break;
                case "3058":
                    bankResult.setStatus(StatusEnum._3.getStatus());
                    bankResult.setBankResult("绑卡失败：短信验证码错误");
                    bankResult.setParam(content);
                    bankResult.setBankCode("300");
                    break;
                case "3057":
                    bankResult.setStatus(StatusEnum._3.getStatus());
                    bankResult.setBankResult("请重新获取验证码");
                    bankResult.setParam(content);
                    bankResult.setBankCode("301");
                    break;
                case "3059":
                    bankResult.setStatus(StatusEnum._3.getStatus());
                    bankResult.setBankResult("短信验证码发送失败");
                    bankResult.setParam(content);
                    bankResult.setBankCode("302");
                    break;
                default:
                    String status = result.getString("retmsg");
                    if (status.equals("请重新获取验证码")){
                        bankResult.setStatus(StatusEnum._3.getStatus());
                        bankResult.setBankResult("请重新获取验证码 ");
                        bankResult.setParam(content);
                        bankResult.setBankCode("301");
                    }else {
                        bankResult = new BankResult(StatusEnum._1.getStatus(), "error.5000");
                        bankResult.setBankResult("绑卡失败："+status);
                        bankResult.setParam(content);
                    }
                    break;
            }


        } else {
            String status = result.getString("retmsg");
            if (status.contains("验证码不符")){
                bankResult.setStatus(StatusEnum._3.getStatus());
                bankResult.setBankResult("短信验证码错误 ");
                bankResult.setParam(content);
                bankResult.setBankCode("300");
            }else if (status.contains("短信验证码发送失败")){
                bankResult.setStatus(StatusEnum._3.getStatus());
                bankResult.setBankResult("短信验证码发送失败");
                bankResult.setParam(content);
                bankResult.setBankCode("302");
            }
            else if (status.contains("请重新获取验证码")){
                bankResult.setStatus(StatusEnum._3.getStatus());
                bankResult.setBankResult("请重新获取验证码");
                bankResult.setParam(content);
                bankResult.setBankCode("301");
            }else if(status.contains("持卡人身份信息")){
                bankResult.setStatus(StatusEnum._3.getStatus());
                bankResult.setBankResult("持卡人身份信息、手机号或CVV2输入不正确");
                bankResult.setParam(content);
            }
            else
            {
                bankResult = new BankResult(StatusEnum._3.getStatus(), "error.5000");
                bankResult.setBankResult("绑卡失败：请核对卡片信息是否有误");
                bankResult.setParam(content);
            }
        }
        logger.info("绑卡确认返回结果"+ JsonUtils.objectToJsonNonNull(bankResult));
        return bankResult;
    }


    private Map<String, Object> getBondParam(RequestCrossMsgDTO trade) throws PayException, UnsupportedEncodingException {
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject channel = JSON.parseObject(extraChannelInfo.getChannelParam());
//        MerchantRegisterInfo merchantRegisterInfo = trade.getMerchantRegisterInfo();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        Integer bankCardType = Integer.valueOf(merchantCard.getBankCardType());
        Map<String, Object> postData = new TreeMap <>();
        String acctname = merchantCard.getCardHolderName();
        String accttype = bankCardType == 2 ? "02" : "00";
        String cardno = merchantCard.getBankCardNum();
        String cusid = registerInfo.getString("cusid");
        String idno = merchantCard.getIdentityNum();
        String bankKey = channel.getString("key");
        String meruserid = merchantCard.getMerchantId() + merchantCard.getTerminalMerId();
        String tel = merchantCard.getBankCardPhone();
        String orgid = channel.getString("orgid");
        String appid = channel.getString("appid");
        String randomstr = AlinPayUtils.getRandomSecretkey();
        String cvv2 = merchantCard.getSecurityCode();
        String validdate = merchantCard.getValidDate();
        postData.put("cusid", cusid);
        postData.put("meruserid", meruserid);
        postData.put("cardno", cardno);
        postData.put("acctname", acctname);
        postData.put("accttype", accttype);
        if (bankCardType == 2) {
            postData.put("validdate", validdate);
        }
        if (bankCardType == 2) {
            postData.put("cvv2", cvv2);
        }
        postData.put("idno", idno);
        postData.put("tel", tel);

        postData.put("orgid", orgid);
        postData.put("appid", appid);
        postData.put("key", bankKey);
        postData.put("randomstr", randomstr);
        postData.put("sign", AlinPayUtils.getMd5Sign(postData));
        postData.remove("key");
        return postData;
    }


    private Map<String, Object> reGetCodeParam(RequestCrossMsgDTO trade) throws PayException, UnsupportedEncodingException {
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        Integer bankCardType = Integer.valueOf(merchantCard.getBankCardType());

        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());

        Map<String, Object> postData = new TreeMap<>();
        String acctname = merchantCard.getCardHolderName();
        String accttype =  bankCardType == 2?"02":"00";
        String cardno = merchantCard. getBankCardNum();
        String cusid = registerInfo.getString("cusid");
        String idno = merchantCard.getIdentityNum();
        String bankKey = param.getString("key");
        String meruserid = merchantCard.getMerchantId() + merchantCard.getTerminalMerId();
        String tel = merchantCard.getBankCardPhone();
        String orgid = param.getString("orgid");
        String appid = param.getString("appid");
        String randomstr = AlinPayUtils.getRandomSecretkey();
        String thpinfo = merchantCard.getCrossRespResult();
        String cvv2 = merchantCard.getSecurityCode();
        String validdate = merchantCard.getValidDate();
        postData.put("acctname", acctname);
        postData.put("accttype", accttype);
        postData.put("appid", appid);
        postData.put("cardno", cardno);
        postData.put("cusid", cusid);
        if (bankCardType == 2) {
            postData.put("cvv2", cvv2);
        }
        postData.put("idno", idno);
        postData.put("key", bankKey);
        postData.put("meruserid", meruserid);
        postData.put("orgid", orgid);
        postData.put("randomstr", randomstr);
        postData.put("tel", tel);
        if (thpinfo != null) {
            postData.put("thpinfo", thpinfo);
        }
        if (bankCardType == 2) {
            postData.put("validdate", validdate);
        }

        postData.put("sign", AlinPayUtils.getMd5Sign(postData));
        postData.remove("key");
        return postData;

    }


    private Map<String, Object> getConfirmParam(RequestCrossMsgDTO trade) throws PayException, UnsupportedEncodingException {
//        TradeObjectSquare tradeObjectSquare = trade.getTradeObjectSquare();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        Integer bankCardType = Integer.valueOf(merchantCard.getBankCardType());
        Map<String, Object> postData = new TreeMap<>();
        String acctname = merchantCard.getCardHolderName();
        String accttype = bankCardType == 2 ? "02" : "00";
        String cardno = merchantCard. getBankCardNum();
        String cusid = registerInfo.getString("cusid");
        String idno = merchantCard.getIdentityNum();
        String bankKey = param.getString("key");
        String meruserid = merchantCard.getMerchantId() + merchantCard.getTerminalMerId();
        String smscode = merchantCard.getSmsCode();
        String tel = merchantCard.getBankCardPhone();
        String orgid = param.getString("orgid");
        String appid = param.getString("appid");
        String randomstr = AlinPayUtils.getRandomSecretkey();
        String thpinfo = merchantCard.getCrossRespResult();
        String cvv2 = merchantCard.getSecurityCode();
        String validdate = merchantCard.getValidDate();
        postData.put("acctname", acctname);
        postData.put("accttype", accttype);
        postData.put("appid", appid);
        postData.put("cardno", cardno);
        postData.put("cusid", cusid);
        if (bankCardType == 2) {
            postData.put("cvv2", cvv2);
        }
        postData.put("idno", idno);
        postData.put("key", bankKey);
        postData.put("meruserid", meruserid);
        postData.put("orgid", orgid);
        postData.put("smscode", smscode);
        postData.put("tel", tel);
        postData.put("randomstr", randomstr);
        if (thpinfo != null) {
            postData.put("thpinfo", thpinfo);
        }
        if (bankCardType == 2) {
            postData.put("validdate", validdate);
        }
        postData.put("sign", AlinPayUtils.getMd5Sign(postData));
        return postData;
    }

    public BankResult unBondCard(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
        BankResult bankResult = new BankResult();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        Map<String, Object> getCodeParam = getUnBondCardParam(trade);
        logger.info(String.format("解除绑卡请求参数：%s",getCodeParam));
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), param.getString("unBondCardUrl"), getCodeParam);
//        String content = "{\"agreeid\":\"201906111653355078\",\"appid\":\"6666678\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"0FED3B1A33F2DB99A89EE4938C84854F\",\"trxstatus\":\"0000\"}";
        logger.info(String.format("解除绑卡请求返回结果：%s",content));
        JSONObject result = (JSONObject) JSON.parse(content);
        String resultCode = result.getString("retcode");

        if ("SUCCESS".equals(resultCode)) {
            bankResult.setStatus(StatusEnum._0.getStatus());
            bankResult.setBankResult("解绑成功");
            bankResult.setParam(content);
            bankResult.setBankTime(new Date());
        } else {
            String eroMsg = result.getString("retmsg");
            bankResult.setStatus(StatusEnum._1.getStatus());
            bankResult.setBankResult("解绑失败");
            bankResult.setParam(content);
        }
        logger.info("解除绑卡返回结果"+ JsonUtils.objectToJsonNonNull(bankResult));
        return bankResult;
    }

    private Map<String, Object> getUnBondCardParam(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        TreeMap<String, Object> postData = new TreeMap<>();
        String cardno = merchantCard.getBankCardNum();
        String cusid = registerInfo.getString("cusid");
        String bankKey = param.getString("key");
        String orgid = param.getString("orgid");
        String appid = param.getString("appid");
        String randomstr = AlinPayUtils.getRandomSecretkey();
        postData.put("appid", appid);
        postData.put("cardno", cardno);
        postData.put("cusid", cusid);
        postData.put("key", bankKey);
        postData.put("orgid", orgid);
        postData.put("randomstr", randomstr);
        postData.put("version", 11);
        postData.put("sign", AlinPayUtils.getMd5Sign(postData));
        postData.remove("key");
        return postData;
    }



}
