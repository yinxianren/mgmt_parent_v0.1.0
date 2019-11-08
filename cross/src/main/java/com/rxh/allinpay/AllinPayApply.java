package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.PayException;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * 快捷交易支付申请 /apply
 * @author admin
 *
 */
@Controller
@RequestMapping("/allinPay")
public class AllinPayApply {

    @Autowired
    PaymentInfo paymentInfo;

    @Value("${AllinPay.payNotifyUrl}")
    private String payNotifyUrl;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
    private final static Logger logger = LoggerFactory.getLogger(AllinPayApply.class);

    @RequestMapping("/apply")
    @ResponseBody
    public CrossResponseMsgDTO trade(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException, PayException, ParseException, InterruptedException {
    	TreeMap<String, Object> params = getTradeParam(trade);
    	String other = trade.getChannelInfoTable().getChannelParam();
    	logger.info("收单请求参数："+ JsonUtils.objectToJsonNonNull(params));
    	JSONObject json = JSON.parseObject(other);
		logger.info("请求URL："+ json.getString("payApplyUrl"));
		String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("payApplyUrl"), params);
//		String content ="{\"appid\":\"6666678\",\"fintime\":\"20190628100547\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"B64DFBAD0AFEEC07EBF5F88D7AF04677\",\"trxid\":\"19060000001375\",\"trxstatus\":\"1999\"}";
		logger.info("收单请求银行返回："+ content);
		return checkResult(content, trade);
    }

    /**
     * 获取请求参数
     *  trade
     *  merchantRegisterCollect
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getTradeParam(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
    	RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
		JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
    	PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
    	String other = trade.getChannelInfoTable().getChannelParam();
    	JSONObject json = JSON.parseObject(other);

    	//公共参数
    	String orgid = json.get("orgid").toString();//机构号
    	String appid = json.get("appid").toString();//APPID
    	String version = "11";//版本号
    	String randomstr = AlinPayUtils.getRandomSecretkey();//随机字符串
    	String key = json.get("key").toString();
    	//私有参数
    	String orderid = payOrder.getPlatformOrderId();//商户订单号
    	String cusid = registerInfo.get("cusid").toString();//商户号
    	String agreeid = trade.getMerchantCardTable().getCrossRespResult();//协议编号
    	String trxcode = json.getString("trxcode");//交易类型
//		[{'trxcode':'QUICKPAY_OF_HP','feerate':'0.50'},  产品1	<=1000元/笔
//		{'trxcode':'QUICKPAY_OF_NP','feerate':'0.45'},   产品2	<=50000元/笔	city、mccid不可上送
//		{'trxcode':'TRX_PAY','feerate':'2'}   代付	未定	上传不是费率，按笔收费
    	String amount = payOrder.getAmount().toString();//金额单位元
    	String currency = "CNY";//币种
    	String subject = "支付订单";//订单内容
    	String  notifyurl = json.get("notifyurl").toString();//回调地址
    	TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
    	treeMap.put("orgid", orgid);
    	treeMap.put("appid", appid);
    	treeMap.put("version", version);
    	treeMap.put("randomstr", randomstr);
    	treeMap.put("cusid", cusid);
    	treeMap.put("orderid", orderid);
    	treeMap.put("agreeid", agreeid);
    	treeMap.put("trxcode", trxcode);

		String amountStr=new BigDecimal(amount).multiply(new BigDecimal(100)).toString();
		treeMap.put("amount",amountStr.contains(".") ? amountStr.substring(0,amountStr.indexOf(".")): amountStr);
//    	treeMap.put("amount", amount.multiply(new BigDecimal(100)));//元转分

    	treeMap.put("currency", currency);
    	treeMap.put("subject", subject);
        if(trade.getRegisterInfoTable()!=null){
            RegisterInfoTable tradeObjectSquare = trade.getRegisterInfoTable();
            if(StringUtils.isNotBlank(tradeObjectSquare.getCity()) && payOrder.getAmount().compareTo(new BigDecimal(1000)) == -1){
                treeMap.put("city", tradeObjectSquare.getCity());
            }
        }
    	treeMap.put("notifyurl", notifyurl);
    	treeMap.put("key", key);
    	treeMap.put("sign", AlinPayUtils.getMd5Sign(treeMap));
		treeMap.remove("key");
    	return treeMap;
    }

    /**
     * 交易结果处理
     * @param content
     * @param trade
     * @return
     * @throws PayException
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
	private CrossResponseMsgDTO checkResult(String content, RequestCrossMsgDTO trade) throws PayException, UnsupportedEncodingException, ParseException {
    	CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
    	if(StringUtils.isNotBlank(content)) {
    		JSONObject json = JSON.parseObject(content);
    		String retcode = json.getString("retcode");
    		if(retcode.equals("SUCCESS")) {
    			// 判断交易状态
    			String trxstatus = json.get("trxstatus").toString();
				TreeMap<String,Object> result = JSON.parseObject(content, TreeMap.class);
                switch(trxstatus){
                    case "0000":
            			String resultSign = result.get("sign").toString();
            			result.remove("sign");
            			String other = trade.getChannelInfoTable().getChannelParam();
            	    	JSONObject param = JSON.parseObject(other);
            	    	result.put("key", param.getString("key"));
            			String sign = AlinPayUtils.getMd5Sign(result);
            			if(sign.equalsIgnoreCase(resultSign)) {
							String trxid = result.get("trxid").toString();
							String fintime = result.get("fintime").toString();
							Date banktime = dateFormat.parse(fintime);
							bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                            bankResult.setCrossResponseMsg("交易成功");
							bankResult.setChannelResponseTime(banktime);
							bankResult.setChannelOrderId(trxid);
                            bankResult.setChannelResponseMsg(content);
            			}else {
            				bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                            bankResult.setCrossResponseMsg("交易异常:签名验证不一致");
                            bankResult.setChannelResponseMsg(content);
                            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            			}
                        break;
                    case "1999":
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("需获取短信验证码,进行下一步确认操作");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易已受理");
                        bankResult.setChannelResponseMsg(content);
                        break;
					case "3059":
						bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
						bankResult.setCrossResponseMsg("短信验证码发送失败");
						bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
						break;
					case "3057":
						bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
						bankResult.setCrossResponseMsg("请重新获取验证码");
						bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00003.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00003.getMsg());
						break;
					case "3058":
						bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
						bankResult.setCrossResponseMsg("短信验证码错误");
						bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00004.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00004.getMsg());
						break;
                    default:
						bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:" + json.get("errmsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
    		}else {
    			 bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                 bankResult.setCrossResponseMsg("交易失败:" + json.get("retmsg"));
                 bankResult.setChannelResponseMsg(content);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
    		}
    	}else {
    		bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败：支付返回结果为空！");
            bankResult.setChannelResponseMsg(content);
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
    	}
		logger.info("收单请求返回payment："+ JsonUtils.objectToJsonNonNull(bankResult));
    	return bankResult;
    }


    @RequestMapping("/queryOrder")
    @ResponseBody
    public CrossResponseMsgDTO queryOrder(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException, ParseException {
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);
        String tradeResult = payOrder.getChannelRespResult();
        JSONObject bankInfo = JSON.parseObject(tradeResult);
        //公共参数
        String orgid = json.get("orgid").toString();//机构号
        String appid = json.get("appid").toString();//APPID
        String key = json.get("key").toString();
        String cusid = registerInfo.get("cusid").toString();//商户号
        String orderid = payOrder.getPlatformOrderId();//商户订单号
        Date bankTime = new Date();
        if (payOrder.getUpdateTime() != null){
            bankTime = payOrder.getUpdateTime();
        }
        String date = dateFormat2.format(bankTime);
        String trxid = bankInfo.get("trxid").toString();//银行流水号
        String version = "11";//版本号
        String randomstr = AlinPayUtils.getRandomSecretkey();//随机字符串

//        String orgid = "201004809979";//机构号
//        String appid = "0000924";//APPID
//        String key = "cccf15f597543e390a7e4c26bbad8ebf";
//        String cusid ="101005129021";//商户号
//        String orderid = "1907251532020670066";//商户订单号
//        String trxid = "19070023437674";//商户订单号
//        String date = "20190725";


        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.put("orgid", orgid);
        treeMap.put("appid", appid);
        treeMap.put("version", version);
        treeMap.put("randomstr", randomstr);

        treeMap.put("cusid", cusid);
        treeMap.put("orderid", orderid);
        treeMap.put("trxid", trxid);
        treeMap.put("date", date);
        treeMap.put("key", key);
        treeMap.put("sign", AlinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");


        logger.info("快捷交易查询请求参数：" + JsonUtils.objectToJsonNonNull(treeMap));
//        logger.info("请求URL：" + json.getString("payQueryUrl"));
//        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://ipay.allinpay.com/apiweb/qpay/query", treeMap);
//        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://test.allinpaygd.com/ipayapiweb/qpay/query", treeMap);
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("queryUrl"), treeMap);
        logger.info("快捷交易查询银行返回：" + content);


        return checkQueryResult(content,trade);
    }

    private CrossResponseMsgDTO checkQueryResult(String content, RequestCrossMsgDTO trade) throws ParseException {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        if (StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if (retcode.equals("SUCCESS")) {
                // 判断交易状态
                String trxstatus = json.get("trxstatus").toString();
                TreeMap<String, Object> result = JSON.parseObject(content, TreeMap.class);
                switch (trxstatus) {
                    case "0000":
                        String trxid = result.get("trxid").toString();
                        String orderid = result.get("orderid").toString();
                        String trxamt = result.get("trxamt").toString();
                        BigDecimal amount= new BigDecimal(trxamt).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
                        String fintime = result.get("fintime").toString();
                        Date banktime = dateFormat.parse(fintime);
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易成功,交易流程完成");
                        bankResult.setChannelResponseTime(banktime);
                        bankResult.setChannelOrderId(trxid);
                        bankResult.setChannelResponseMsg(content);
                        break;

                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    case "0003":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    case "3054":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易结果未知:" + json.get("errmsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("快捷交易查询失败:" + json.get("retmsg"));
                bankResult.setChannelResponseMsg(content);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        } else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("快捷交易查询失败：支付返回结果为空！");
            bankResult.setChannelResponseMsg(content);
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        logger.info("快捷交易查询返回payment：" + JsonUtils.objectToJsonNonNull(bankResult));
        return bankResult;
    }

}
