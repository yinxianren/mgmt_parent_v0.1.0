package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.*;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/allinPay")
public class AllinPaySMS {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(AllinPaySMS.class);

    @RequestMapping("/sendSMS")
    public BankResult trade(@RequestBody SquareTrade trade) throws UnsupportedEncodingException, PayException {
    	TreeMap<String, Object> params = getTradeParam(trade);
		String other = trade.getChannelInfo().getOthers();
		logger.info("收单短信请求参数："+ JsonUtils.objectToJsonNonNull(params));
		JSONObject json = JSON.parseObject(other);
		logger.info("请求URL："+ json.getString("paySms"));
		String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("paySms"), params);
//		String content = "{\"agreeid\":\"201906111653355078\",\"appid\":\"6666678\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"0FED3B1A33F2DB99A89EE4938C84854F\",\"trxstatus\":\"0000\"}";
		logger.info("收单短信请求银行返回："+ content);
		return checkResult(content, trade);
    }

    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getTradeParam(SquareTrade trade) throws UnsupportedEncodingException {
//    	logger.info(String.format("通联快捷支付交易短信发送param=%s", JSONObject.toJSONString(trade)));
		PayOrder payOrder = trade.getPayOrder();
    	String other = trade.getChannelInfo().getOthers();
    	JSONObject json = JSON.parseObject(other);
    	MerchantRegisterCollect merchantRegisterCollect = trade.getMerchantRegisterCollect();
		JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getBackData());
		JSONObject trxidJson = JSON.parseObject(payOrder.getTradeResult());
		//公共参数
    	String orgid = json.get("orgid").toString();//机构号
    	String appid = json.get("appid").toString();//APPID
    	String version = "11";//版本号
    	String randomstr = AlinPayUtils.getRandomSecretkey();//随机字符串
    	String key = json.get("key").toString();
    	//私有参数
		String cusid = registerInfo.get("cusid").toString();//商户号
    	String trxid = trxidJson.getString("trxid");//平台交易流水号
		String agreeid = trade.getMerchantCard().getResult();//协议编号
    	//非必须参数
		String thpinfo = "";
		if (trxidJson.get("thpinfo") != null){
			thpinfo = trxidJson.get("thpinfo").toString();//交易透传信息
		}
    	TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
    	treeMap.put("orgid", orgid);
    	treeMap.put("appid", appid);
    	treeMap.put("version", version);
    	treeMap.put("randomstr", randomstr);
    	treeMap.put("cusid", cusid);
    	treeMap.put("trxid", trxid);
    	treeMap.put("agreeid", agreeid);
    	treeMap.put("key", key);
    	if(StringUtils.isNotBlank(thpinfo)) {
    		treeMap.put("thpinfo", thpinfo);
    	}
    	treeMap.put("sign", AlinPayUtils.getMd5Sign(treeMap));
		treeMap.remove("key");
    	return treeMap;
    }

    /**
     * 请求结果处理
     * @param content
     * @param trade
     * @return
     * @throws PayException
     * @throws UnsupportedEncodingException
     */
	private BankResult checkResult(String content, SquareTrade trade) throws PayException, UnsupportedEncodingException {
    	BankResult bankResult = new BankResult();
    	if(StringUtils.isNotBlank(content)) {
    		JSONObject json = JSON.parseObject(content);
    		String retcode = json.getString("retcode");
    		if(retcode .equals("SUCCESS")) {
    			bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("短信发送成功");
                bankResult.setParam(content);
    		}else {
    			bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("短信发送失败！"+json.get("retmsg"));
                bankResult.setParam(content);
    		}

    	}else {
    		bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("短信发送请求失败：请求返回结果为空！");
            bankResult.setParam(content);
    	}
    	return bankResult;
    }
}
