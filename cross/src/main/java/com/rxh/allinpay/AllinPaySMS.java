package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.PayException;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

@Controller
@RequestMapping("/allinPay")
public class AllinPaySMS {

    private final static Logger logger = LoggerFactory.getLogger(AllinPaySMS.class);

    @RequestMapping("/sendSMS")
    public CrossResponseMsgDTO trade(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException, PayException {
    	TreeMap<String, Object> params = getTradeParam(trade);
		String other = trade.getChannelInfoTable().getChannelParam();
		logger.info("收单短信请求参数："+ JsonUtils.objectToJsonNonNull(params));
		JSONObject json = JSON.parseObject(other);
		logger.info("请求URL："+ json.getString("paySms"));
		String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("paySms"), params);
//		String content = "{\"agreeid\":\"201906111653355078\",\"appid\":\"6666678\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"0FED3B1A33F2DB99A89EE4938C84854F\",\"trxstatus\":\"0000\"}";
		logger.info("收单短信请求银行返回："+ content);
		return checkResult(content);
    }

    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getTradeParam(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
		PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
    	String other = trade.getChannelInfoTable().getChannelParam();
    	JSONObject json = JSON.parseObject(other);
    	RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
		JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
		JSONObject trxidJson = JSON.parseObject(payOrder.getChannelRespResult());
		//公共参数
    	String orgid = json.get("orgid").toString();//机构号
    	String appid = json.get("appid").toString();//APPID
    	String version = "11";//版本号
    	String randomstr = AlinPayUtils.getRandomSecretkey();//随机字符串
    	String key = json.get("key").toString();
    	//私有参数
		String cusid = registerInfo.get("cusid").toString();//商户号
    	String trxid = trxidJson.getString("trxid");//平台交易流水号
		String agreeid = trade.getMerchantCardTable().getCrossRespResult();//协议编号
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
     * @return
     * @throws PayException
     * @throws UnsupportedEncodingException
     */
	private CrossResponseMsgDTO checkResult(String content) throws PayException, UnsupportedEncodingException {
    	CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
    	if(StringUtils.isNotBlank(content)) {
    		JSONObject json = JSON.parseObject(content);
    		String retcode = json.getString("retcode");
    		if(retcode .equals("SUCCESS")) {
    			bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                bankResult.setCrossResponseMsg("短信发送成功");
                bankResult.setChannelResponseMsg(content);
    		}else {
    			bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("短信发送失败！"+json.get("retmsg"));
                bankResult.setChannelResponseMsg(content);
				bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
				bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
    		}

    	}else {
    		bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("短信发送请求失败：请求返回结果为空！");
            bankResult.setChannelResponseMsg(content);
			bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
			bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
    	}
    	return bankResult;
    }
}
