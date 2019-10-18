package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.*;
import com.rxh.utils.AlinPayUtils;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.StringUtils;
import com.rxh.utils.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * 快捷小额免短信支付 /freeSMS
 * @author admin
 *
 */
@Controller
@RequestMapping("/allinPay")
public class AllinPayFreeSMS {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(AllinPayFreeSMS.class);
    
    @RequestMapping("/freeSMS")
    public BankResult trade(@RequestBody SquareTrade trade) throws UnsupportedEncodingException, PayException {
    	TreeMap<String, Object> params = getTradeParam(trade);
    	String other = trade.getChannelInfo().getOthers();
    	JSONObject json = JSON.parseObject(other);
    	logger.info("通联免短信支付请求参数"+JSONObject.toJSONString(params));
    	String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("freeUrl"), params);
//		String content = "{\"appid\":\"6666678\",\"fintime\":\""+new Date().getTime()+"\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"B64DFBAD0AFEEC07EBF5F88D7AF04677\",\"trxid\":\""+trade.getPayOrder().getPayId()+"\",\"trxstatus\":\"0000\"}";
		logger.info("通联免短信支付返回结果："+content);
    	return checkResult(content, trade);
    }
    
    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getTradeParam(SquareTrade trade) throws UnsupportedEncodingException {
    	PayOrder payOrder = trade.getPayOrder();
    	String other = trade.getChannelInfo().getOthers();
    	JSONObject json = JSON.parseObject(other);
		MerchantRegisterCollect merchantRegisterInfo = trade.getMerchantRegisterCollect();
		JSONObject registerInfo = JSON.parseObject(merchantRegisterInfo.getBackData());
		MerchantCard merchantCard = trade.getMerchantCard();
		JSONObject data = JSONObject.parseObject(merchantCard.getBackData());
//    	//公共参数
    	String orgid = json.get("orgid").toString();//机构号
    	String appid = registerInfo.get("appid").toString();//APPID
    	String version = "11";//版本号
    	String randomstr = AlinPayUtils.getRandomSecretkey();//随机字符串
    	String key = json.get("key").toString();
//    	//私有参数
//    	String cusid = json.get("cusid").toString();//商户号
		String cusid =registerInfo.get("cusid").toString();

    	String orderid = payOrder.getPayId();//平台订单号
    	String agreeid = data.get("agreeid").toString();//协议编号
    	String amount = payOrder.getAmount().toString();//金额单位元
    	String currency = "CNY";//币种
    	String subject = "小额免短信支付";//订单内容
    	Properties prop = new Properties();
        try {
             ClassPathResource classPathResource = new ClassPathResource("properties/acquirer-config.properties");   //这里的填写的参数是配置文件的相对路径
             prop.load(new InputStreamReader(classPathResource.getInputStream(),"utf-8"));     //文件流的编码方式
        }catch (IOException e){
             e.printStackTrace();
        }
    	String notifyurl = json.get("notifyurl").toString();//回调地址
    	
    	TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
    	treeMap.put("orgid", orgid);
    	treeMap.put("appid", appid);
    	treeMap.put("version", version);
    	treeMap.put("randomstr", randomstr);
    	treeMap.put("cusid", cusid);
    	treeMap.put("orderid", orderid);
    	treeMap.put("agreeid", agreeid);

		String amountStr=new BigDecimal(amount).multiply(new BigDecimal(100)).toString();
		treeMap.put("amount",amountStr.contains(".") ? amountStr.substring(0,amountStr.indexOf(".")): amountStr);
//    	treeMap.put("amount", amount.multiply(new BigDecimal(100)));//元转分

    	treeMap.put("currency", currency);
    	treeMap.put("subject", subject);
		if(trade.getTradeObjectSquare()!=null){
			TradeObjectSquare tradeObjectSquare = trade.getTradeObjectSquare();
			if(StringUtils.isNotBlank(tradeObjectSquare.getCity())){
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
	private BankResult checkResult(String content, SquareTrade trade) throws PayException, UnsupportedEncodingException {
    	BankResult bankResult = new BankResult();
    	bankResult.setBankTime(new Date());
    	if(StringUtils.isNotBlank(content)) {
    		JSONObject json = JSON.parseObject(content);
    		String retcode = json.getString("retcode");
    		if(retcode .equals("SUCCESS")) {
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
                            bankResult.setBankOrderId(result.get("trxid").toString());
            			}else {
            				bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                            bankResult.setBankResult("交易异常:签名验证失败");
                            bankResult.setParam(content);
            			}
                        break;
                    case "2000":
                        bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                        bankResult.setBankResult("支付交易请求已接受,等待交易结果通知。");
                        bankResult.setParam(content);
                        break;
                    default:
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("交易异常:" + json.get("errmsg"));
                        bankResult.setParam(content);
                        break;
                }
    			
    		}else {
    			 bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                 bankResult.setBankResult("交易失败:" + json.get("retmsg"));
                 bankResult.setParam(content);
    		}
    	}else {
    		bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("交易失败：支付返回结果为空！");
            bankResult.setParam(content);
    	}
    	return bankResult;
    }
    
}
