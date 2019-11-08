package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.TransOrderInfoTable;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.PayException;
import com.rxh.utils.AlinPayUtils;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

/**
 * 快捷交易查询 /query
 * @author admin
 *
 */
@Controller
@RequestMapping("/allinPay")
public class AllinPayQuickQuery {
	
    @RequestMapping("/query")
    public CrossResponseMsgDTO trade(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException, PayException {
    	TreeMap<String, Object> params = getTradeParam(trade);
    	String other = trade.getChannelInfoTable().getChannelParam();
    	JSONObject json = JSON.parseObject(other);
    	String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("queryUrl"), params);
    	return checkResult(content, trade);
    }
    
    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getTradeParam(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
    	TransOrderInfoTable transOrder = trade.getTransOrderInfoTable();
    	String other = trade.getChannelInfoTable().getChannelParam();
    	JSONObject json = JSON.parseObject(other);
		RegisterCollectTable merchantRegisterInfo = trade.getRegisterCollectTable();
		JSONObject registerInfo = JSON.parseObject(merchantRegisterInfo.getChannelRespResult());
		JSONObject order = JSONObject.parseObject(trade.getPayOrderInfoTable().getChannelRespResult());
    	//公共参数
    	String orgid = json.get("orgid").toString();//机构号
    	String appid = json.get("appid").toString();//APPID
    	String version = "11";//版本号
    	String randomstr = AlinPayUtils.getRandomSecretkey();//随机字符串
    	String key = json.get("key").toString();
    	
    	//私有参数
    	String cusid = registerInfo.get("cusid").toString();//商户号
    	String orderid = transOrder.getMerOrderId();//商户订单号
    	String trxid = order.get("trxid").toString();//平台交易流水号
    	String date = "";//交易日期
    	
    	TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
    	treeMap.put("orgid", orgid);
    	treeMap.put("appid", appid);
    	treeMap.put("version", version);
    	treeMap.put("randomstr", randomstr);
    	treeMap.put("cusid", cusid);
    	treeMap.put("orderid", orderid);
    	treeMap.put("key", key);
    	
    	if(StringUtils.isNoneBlank(trxid)) {
    		treeMap.put("trxid", trxid);
    	}else {
    		treeMap.put("date", date);
    	}
    	treeMap.put("sign", AlinPayUtils.getMd5Sign(treeMap));
		treeMap.remove("key");
    	return treeMap;
    }
    
    /**
     * 查询结果处理
     * @param content
     * @param trade
     * @return
     * @throws PayException
     * @throws UnsupportedEncodingException 
     */
    @SuppressWarnings("unchecked")
	private CrossResponseMsgDTO checkResult(String content, RequestCrossMsgDTO trade) throws PayException, UnsupportedEncodingException {
    	CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
    	if(StringUtils.isNotBlank(content)) {
    		JSONObject json = JSON.parseObject(content);
    		String retcode = json.getString("retcode");
    		if(retcode == "SUCCESS") {
    			 // 判断交易状态
    			String trxstatus = json.get("trxstatus").toString();
                switch(trxstatus){
                    case "0000":
                    	TreeMap<String,Object> result = JSON.parseObject(content, TreeMap.class);
            			String resultSign = result.get("sign").toString();
            			result.remove("sign");
            			String other = trade.getChannelExtraInfoTable().getChannelParam();
            	    	JSONObject param = JSON.parseObject(other);
            	    	result.put("key", param.getString("key"));
            			String sign = AlinPayUtils.getMd5Sign(result);
            			if(sign.equalsIgnoreCase(resultSign)) {
            				bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                            bankResult.setCrossResponseMsg("交易成功");
                            bankResult.setChannelResponseMsg(content);
            			}else {
            				bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                            bankResult.setCrossResponseMsg("交易查询异常:签名验证失败");
                            bankResult.setChannelResponseMsg(content);
            			}
                        break;
                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossResponseMsg("交易处理中。");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易查询异常:" + json.get("errmsg"));
                        bankResult.setChannelResponseMsg(content);
                        break;
                }
    			
    		}else {
    			 bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                 bankResult.setCrossResponseMsg("交易查询失败:" + json.get("retmsg"));
                 bankResult.setChannelResponseMsg(content);
    		}
    	}else {
    		bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易查询失败：支付返回结果为空！");
            bankResult.setChannelResponseMsg(content);
    	}
    	return bankResult;
    }
    
}
