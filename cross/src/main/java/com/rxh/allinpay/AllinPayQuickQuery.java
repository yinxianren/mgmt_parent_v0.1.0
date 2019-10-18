package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.AlinPayUtils;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.StringUtils;
import com.rxh.utils.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.TreeMap;

/**
 * 快捷交易查询 /query
 * @author admin
 *
 */
@Controller
@RequestMapping("/allinPay")
public class AllinPayQuickQuery {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(AllinPayQuickQuery.class);
    
    @RequestMapping("/query")
    public BankResult trade(@RequestBody SquareTrade trade) throws UnsupportedEncodingException, PayException {
    	TreeMap<String, Object> params = getTradeParam(trade);
    	String other = trade.getExtraChannelInfo().getData();
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
    private TreeMap<String, Object> getTradeParam(SquareTrade trade) throws UnsupportedEncodingException {
    	TransOrder transOrder = trade.getTransOrder();
    	String other = trade.getExtraChannelInfo().getData();
    	JSONObject json = JSON.parseObject(other);
		MerchantRegisterInfo merchantRegisterInfo = trade.getMerchantRegisterInfo();
		JSONObject registerInfo = JSON.parseObject(merchantRegisterInfo.getBackData());
    	//公共参数
    	String orgid = json.get("orgid").toString();//机构号
    	String appid = json.get("appid").toString();//APPID
    	String version = "11";//版本号
    	String randomstr = AlinPayUtils.getRandomSecretkey();//随机字符串
    	String key = json.get("key").toString();
    	
    	//私有参数
    	String cusid = registerInfo.get("cusid").toString();//商户号
    	String orderid = transOrder.getMerOrderId();//商户订单号
    	String trxid = json.get("trxid").toString();//平台交易流水号
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
	private BankResult checkResult(String content, SquareTrade trade) throws PayException, UnsupportedEncodingException {
    	BankResult bankResult = new BankResult();
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
            			String other = trade.getExtraChannelInfo().getData();
            	    	JSONObject param = JSON.parseObject(other);
            	    	result.put("key", param.getString("key"));
            			String sign = AlinPayUtils.getMd5Sign(result);
            			if(sign.equalsIgnoreCase(resultSign)) {
            				bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                            bankResult.setBankResult("交易成功");
                            bankResult.setParam(content);
            			}else {
            				bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                            bankResult.setBankResult("交易查询异常:签名验证失败");
                            bankResult.setParam(content);
            			}
                        break;
                    case "2000":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                        bankResult.setBankResult("交易处理中。");
                        bankResult.setParam(content);
                        break;
                    default:
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("交易查询异常:" + json.get("errmsg"));
                        bankResult.setParam(content);
                        break;
                }
    			
    		}else {
    			 bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                 bankResult.setBankResult("交易查询失败:" + json.get("retmsg"));
                 bankResult.setParam(content);
    		}
    	}else {
    		bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("交易查询失败：支付返回结果为空！");
            bankResult.setParam(content);
    	}
    	return bankResult;
    }
    
}
